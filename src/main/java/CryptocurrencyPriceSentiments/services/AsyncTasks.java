package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.CryptoMapper;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.models.news.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Async
public class AsyncTasks {

    RestTemplate restTemplate;
    DataCollection dataCollection;
    CryptoMapper cryptoMapper;
    Logger logger;

    @Autowired
    public AsyncTasks(RestTemplate restTemplate, DataCollection dataCollection, CryptoMapper cryptoMapper) {
        this.restTemplate = restTemplate;
        this.dataCollection = dataCollection;
        this.cryptoMapper = cryptoMapper;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Loads a specified number of records from the CryptoCompare API into the database by period. This is potentially
     * a very long-running task, so, while it returns a response containing the data currently available in the database,
     * the task continues executing in the background.
     * @param period the time period to backload for -- corresponds to a database table
     * @param numRecords the number of records to backload
     * @return a response object containing the data already extant in the database
     */
    public void backloadData(String period, int numRecords) throws Exception {

        String[] tradingPairs = dataCollection.getTradingPairs();

        // Cycles through each trading pair.
        for (String pair : tradingPairs) {
            /*  if (query count(id)) >= numRecords
                    continue
                else is count(id) < numRecords?
                    what is the last timestamp where count(id) = numRecords? (last)
                    for (i = 1; i <= numRecords - count(id); i++)
                        add i * last * secsInPeriod to an arraylist
                    query for missing historcal data
             */

            // Queries by the period specified in the method signature if the number of time periods exceeds
            // the number of records for this particular period, pair, and exchange in the database.

            // Timestamps to query for
            ArrayList<Integer> missingTimestamps = new ArrayList<>();

            // The number of records in the database for this period, pair, and exchange.
            String from = pair.substring(0, pair.indexOf("/"));
            String to = pair.substring(pair.indexOf("/") + 1);
            int countRecords = dataCollection.countRecordsByPeriod(period, from, to);
            logger.info("Found " + countRecords + " records for period " + "\"day\" for trading pair " + pair + ".");

            // Checks if the number of records in the database exceeds the number of records requested or if there are
            // no records in the table.
            if (countRecords >= numRecords) continue;
            else if (countRecords > 0) {

                int counter = 0;

                // Gets the last timestamp in the database table.
                int lastTimestamp = dataCollection.getLastTimestampByPeriod(period, from, to);

                // The difference between the number of records requested and the number of records in the DB.
                int diffNumRecords = numRecords - countRecords;

                // Adds the timestamps for the net number of requested records for an array list.
                for (int i = 1; i <= diffNumRecords; i++) {

                    int missingTimestamp = lastTimestamp - i * dataCollection.getPeriodLength(period);
                    missingTimestamps.add(missingTimestamp);
                }

                // Queries for the missing timestamps, adds the results to the database.
                dataCollection.queryMissingHistoricalData(missingTimestamps, period, from, to);

                // Clears the array list of missing timestamps.
                missingTimestamps.clear();

            } else dataCollection.queryHistoricalData(period, from, to, numRecords);

            // Finds and fills any gaps in the data.
            dataCollection.findHistoricalGaps(period, from, to);
        }
    }

    /**
     * Queries for news stories for all currency pairs and adds them to the database.
     */
    public void addNews() {

        String[] tradingPairs = dataCollection.getTradingPairs();

        for (String pair : tradingPairs) {

            String from = pair.substring(0, pair.indexOf("/"));
            String to = pair.substring(pair.indexOf("/") + 1);

            logger.info("Querying CryptoCompare for news stories about " + from + " and " + to + ".");
            String query = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN&categories=" + from + "," + to;
            News news = restTemplate.getForObject(query, News.class);
            CryptocurrencyPriceSentiments.models.news.Data[] newsData = news.getData();

            for (CryptocurrencyPriceSentiments.models.news.Data story : newsData) {
                cryptoMapper.addNews(story);
                logger.info("Added story about " + from + " and " + to + ".");
            }
        }

        logger.info("Finished adding news stories.");
    }

    /**
     * Queries for news stories of specified categories and adds them to the database.
     * @param categories the categories to query the CryptoCompare API for
     */
    public void addNews(String categories) {

        logger.info("Querying CryptoCompare for news stories about category/ies " + categories + ".");
        String query = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN&categories=" + categories;
        News news = restTemplate.getForObject(query, News.class);
        CryptocurrencyPriceSentiments.models.news.Data[] newsData = news.getData();

        for (CryptocurrencyPriceSentiments.models.news.Data story : newsData) {
            cryptoMapper.addNews(story);
            logger.info("Added story about category/ies " + categories + ".");
        }

        logger.info("Finished adding news stories.");
    }
}
