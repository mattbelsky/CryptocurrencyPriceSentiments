package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.CryptoMapper;
import CryptocurrencyPriceSentiments.exceptions.TableEmptyException;
import CryptocurrencyPriceSentiments.models.Data;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.models.PriceHistorical;
import CryptocurrencyPriceSentiments.models.news.News;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;

@Service
public class DataCollection {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ScheduledTasks scheduledTasks;

    @Autowired
    CryptoMapper cryptoMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // The time periods to query for
    private static String[] periods = {
            "day",
            "hour",
            "minute"
    };

    final static int HOURS_IN_DAY = 24;
    final static int MIN_IN_HOUR = 60;
    final static int SEC_IN_MIN = 60;

    /**
     * Gets a list of currency pairs being traded.
     * @return
     */
    @Cacheable(value = "TradingPairs")
    public String[] getTradingPairs() {

        String[] tradingPairs = cryptoMapper.getCurrencyPairs();
        return tradingPairs;
    }

    /**
     * Executes the appropriate tasks depending on which cron job has just run.
     * @param period the time period to query for
     * @return a response object containing the data in the database
     */
    public GeneralResponse switchCronOps(String period) {

        String[] tradingPairs = getTradingPairs();
        ArrayList<Integer> timestampsDaily = scheduledTasks.getTimestampDaily();
        ArrayList<Integer> timestampsHourly = scheduledTasks.getTimestampHourly();
        ArrayList<Integer> timestampsMinutely = scheduledTasks.getTimestampMinutely();
        ArrayList<Integer> timestamps = new ArrayList<>();

        // Cycles through each trading pair.
        for (String pair : tradingPairs) {

            String from = pair.substring(0, pair.indexOf("/"));
            String to = pair.substring(pair.indexOf("/") + 1);

            switch (period) {
                case "day":
                    queryMissingHistoricalData(timestampsDaily, period, from, to);
                    timestamps = timestampsDaily;
                    continue;
                case "hour":
                    queryMissingHistoricalData(timestampsHourly, period, from, to);
                    timestamps = timestampsHourly;
                    continue;
                case "minute":
                    queryMissingHistoricalData(timestampsMinutely, period, from, to);
                    timestamps = timestampsMinutely;
                    continue;
            }
        }

        ArrayList<Data[]> responseData = getResponseData(period, timestamps);
        logger.info("Data successfully added.", responseData);

        return new GeneralResponse(HttpStatus.OK, "Data successfully added.", responseData);
    }

    /**
     * Queries for historical data.
     * @param period the time period to query for
     * @param fromCurrency
     * @param toCurrency
     * @param numRecords the number of records to query for
     */
    public void queryHistoricalData(String period, String fromCurrency, String toCurrency, int numRecords) {

        String query = "https://min-api.cryptocompare.com/data/histo" + period + "?" +
                "fsym=" + fromCurrency +
                "&tsym=" + toCurrency +
                "&aggregate=1&limit=" + numRecords;

        PriceHistorical historicalData = restTemplate.getForObject(query, PriceHistorical.class);

        // Adds the pair names, the exchange name, the average trading price, and the queried historical
        // data to the desired database table depending on the time period.
        int length = historicalData.getData().length;

        for (int i = 0; i < length; i++) {

            Data data = historicalData.getData()[i];

            // Adds the data to the database.
            addHistoricalData(data, period, fromCurrency, toCurrency);
        }
    }

    /**
     * Queries for the missing historical data.
     * @param missingTimestamps the timestamps to query for
     * @param period
     * @param fromCurrency
     * @param toCurrency
     */
    public void queryMissingHistoricalData(ArrayList<Integer> missingTimestamps, String period, String fromCurrency,
                                           String toCurrency) {

        for (Integer timestamp : missingTimestamps) {

            String query = "https://min-api.cryptocompare.com/data/histo" + period + "?" +
                    "fsym=" + fromCurrency +
                    "&tsym=" + toCurrency +
                    "&toTs=" + timestamp +
                    "&aggregate=1&limit=1";
            PriceHistorical historicalData = restTemplate.getForObject(query, PriceHistorical.class);

            // The Data object being acted upon. Want the second element in the Data array because the CryptoCompare
            // API returns at least 2, even if the limit is set to 1, and the second is the desired element.
            Data data = historicalData.getData()[1];

            // Adds the data to the database.
            addHistoricalData(data, period, fromCurrency, toCurrency);
        }
    }

    /**
     * Adds historical data.
     * @param data the object containing historical data for a time period -- maps to database tables
     * @param period the time period to query for
     * @param fromCurrency
     * @param toCurrency
     */
    public void addHistoricalData(Data data, String period, String fromCurrency, String toCurrency) {

        Timestamp ts = new Timestamp(data.getTime());
        String time = ts.toString();

        // Adds the pair names to the Data object.
        data.setFromCurrency(fromCurrency);
        data.setToCurrency(toCurrency);

        // Adds the historical data depending on the time period.
        if (period.equals("day")) cryptoMapper.addPriceByDate(data);
        else if (period.equals("hour")) cryptoMapper.addPriceByHour(data);
        else if (period.equals("minute")) cryptoMapper.addPriceByMinute(data);

        logger.info("Added data for period \"" + period + "\" and currency pair \"" + fromCurrency + "/" + toCurrency + "\".");
    }

    /**
     * Finds gaps in the database.
     * @param period the time period to query for
     * @param fromCurrency
     * @param toCurrency
     */
    public void findHistoricalGaps(String period, String fromCurrency, String toCurrency) throws Exception {

        // The array list of missing timestamps, if any
        ArrayList<Integer> missingTimestamps = new ArrayList<>();
        int periodLength;

        // Gets an array of timestamps depending on the pair/exchange combination.
        Integer[] timestamps = getTimestampsByPeriod(period, fromCurrency, toCurrency);

        periodLength = getPeriodLength(period);

        // Finds missing timestamps and adds them to an array list.
        for (int i = 0; i < timestamps.length - 1; i++) {

            int difference = timestamps[i + 1] - timestamps[i];
            int periodsMissing = difference / periodLength;

            // Adds the missing timestamps, if any, to an array list.
            if (periodsMissing > 0) {

                // Must be less than the number of periods missing as this value is the next found value in
                // the table.
                for (int j = 1; j < periodsMissing; j++) {
                    missingTimestamps.add(timestamps[i] + periodLength * j);
                }
            }
        }

        // Adds the missing data to the proper table.
        if (missingTimestamps.size() > 0) queryMissingHistoricalData(missingTimestamps, period, fromCurrency,
                toCurrency);
    }

    /**
     * Counts the number of records by period and other criteria.
     * @param period the time period to query for
     * @param fromCurrency
     * @param toCurrency
     * @return the number of records in the database for the specified combination of parameters
     */
    public int countRecordsByPeriod(String period, String fromCurrency, String toCurrency) throws Exception {

        checkValidPeriod(period);
        return cryptoMapper.countRecordsByPeriod(period, fromCurrency, toCurrency);
    }

    /**
     * Gets the latest timestamp in the database table by time period and other criteria.
     * @param period the time period to query for
     * @param fromCurrency
     * @param toCurrency
     * @return the latest timestamp
     */
    public int getLastTimestampByPeriod(String period, String fromCurrency, String toCurrency) throws Exception {

        checkValidPeriod(period);
        return cryptoMapper.getLastTimestamp(period, fromCurrency, toCurrency);
    }

    /**
     * Gets news data from the database.
     * @return a response object containing news data
     * @throws TableEmptyException a custom exception thrown when a database table is empty
     */
    public GeneralResponse getNews() throws TableEmptyException {

        // Throws an exception if the news table is empty.
        if (cryptoMapper.getNews().size() == 0) {

            throw new TableEmptyException(HttpStatus.NO_CONTENT, "No data found");

        } else /*if (categories == null)*/{

            ArrayList<CryptocurrencyPriceSentiments.models.news.Data> newsData = cryptoMapper.getNews();
            return new GeneralResponse(HttpStatus.OK, "News data successfully queried.", newsData);
//        } else {
//
//            // Gets a list of news stories by each of the user-input categories.
//            // NOTE: Can only query the database for one category at a time for now, not because multiple categories
//            // aren't possible, but because I'm not sure how to do it at the moment. Will look into it though.
//
//            // Holds the arrays of news stories by category.
//            ArrayList<komodocrypto.model.cryptocompare.news.Data[]> newsData = new ArrayList<>();
//
//            // Splits the user-input category string along commas. If the string was not delimited by commas or contained
//            // nonsense, a set of news articles will still be returned, just not the desired ones.
//            // NOTE: Look at MySQL stored procedures.
//            String[] categoryArray = categories.split(",");
//            for (String category : categoryArray) {
//
//                newsData.add(cryptoMapper.getNewsByCategory(category));
//            }
//
//            // Removes duplicate elements.
//            // Do this later.
//
//            return new GeneralResponse(newsData);
        }
    }

    /**
     * Gets an array of timestamps depending on the pair/exchange combination.
     * @param period the time period to query for
     * @param fromCurrency
     * @param toCurrency
     * @return an array of timestamps
     */
    public Integer[] getTimestampsByPeriod(String period, String fromCurrency, String toCurrency) throws Exception {

        checkValidPeriod(period);
        return cryptoMapper.getTimestampsByPeriod(period, fromCurrency, toCurrency);
    }

    /**
     * Gets the number of seconds in a specified period.
     * @param period the specified time period
     * @return the number of seconds in said period
     */
    public int getPeriodLength(String period) {

        switch (period) {
            case "day":
                return HOURS_IN_DAY * MIN_IN_HOUR * SEC_IN_MIN;
            case "hour":
                return MIN_IN_HOUR * SEC_IN_MIN;
            default:
                return MIN_IN_HOUR;
        }
    }

    /**
     * Checks if the specified period corresponds to a time period recorded in the database.
     * @param period the specified period
     */
    public void checkValidPeriod(String period) throws Exception {

        if (!"datehourminute".contains(period)) {

            String message = "\"" + period + "\" is not a valid time period";
            logger.error(message);
            throw new Exception(message);
        }
    }

    /**
     * Gets all the data for the specified time period.
     * @param period the time period to get data for
     * @return the data
     */
    public Data[] getResponseData(String period) {

        switch (period) {
            case "day": return cryptoMapper.getDataByDay();
            case "hour": return cryptoMapper.getDataByHour();
            default: return cryptoMapper.getDataByMinute();
        }
    }

    public ArrayList<Data[]> getResponseData(String period, ArrayList<Integer> timestamps) {

        ArrayList<Data[]> data = new ArrayList<>();

        for (Integer ts : timestamps) {

            switch (period) {
                case "day": data.add(cryptoMapper.getDailyDataByTime(ts));
                case "hour": data.add(cryptoMapper.getHourlyDataByTime(ts));
                default: data.add(cryptoMapper.getMinutelyDataByTime(ts));
            }
        }

        return data;
    }
}
