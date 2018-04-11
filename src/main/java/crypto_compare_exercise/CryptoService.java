package crypto_compare_exercise;

import crypto_compare_exercise.models.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CryptoService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CryptoMapper cryptoMapper;

    public ArrayList<PriceHistorical> addPriceHistorical(String fromCurrency, String toCurrency) {

        // the number of records to return, to be adjusted for calls for records/hour and records/minute
        int numRecords = 100;
        final int HOURS_IN_DAY = 24;
        final int MIN_IN_HOUR = 60;

        // the URLs for querying by date, hour, and minute
        // The PriceHistorical object containing the result of each query will be stored in an ArrayList of PriceHistorical
        // objects.
        String queryPriceDate = "https://min-api.cryptocompare.com/data/histoday?fsym="
                + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + numRecords;
        String queryPriceHour = "https://min-api.cryptocompare.com/data/histohour?fsym="
                + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + numRecords * HOURS_IN_DAY;
        String queryPriceMinute = "https://min-api.cryptocompare.com/data/histominute?fsym="
                + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + numRecords * HOURS_IN_DAY * MIN_IN_HOUR;

//        PriceHistorical priceHistorical = restTemplate.getForObject(queryPriceDate, PriceHistorical.class);
//        Data[] data = priceHistorical.getData();
//
//        for(Data datum : data) {
//            cryptoMapper.addPrices(datum);
//        }

        // Maps the results to an ArrayList of objects. They must be in this order for the rest of the code to work!
        ArrayList<PriceHistorical> priceHistorical = new ArrayList<>();
        priceHistorical.add(restTemplate.getForObject(queryPriceDate, PriceHistorical.class));
        priceHistorical.add(restTemplate.getForObject(queryPriceHour, PriceHistorical.class));
        priceHistorical.add(restTemplate.getForObject(queryPriceMinute, PriceHistorical.class));

//        PriceHistorical priceHistByDate = restTemplate.getForObject(queryPriceDate, PriceHistorical.class);
//        PriceHistorical priceHistByHour = restTemplate.getForObject(queryPriceHour, PriceHistorical.class);
//        PriceHistorical priceHistByMinute = restTemplate.getForObject(queryPriceMinute, PriceHistorical.class);

        /*  Need to isolate the Data objects within each PriceHistorical object, split the timestamp in each object into
            separate fields and feed the objects into the the database.
         */

        // Isolates the Data objects within each of the three PriceHistorical objects and splits the timestamps in each
        // Data object into separate fields (day, month, year, hour, & minute). Depending on whether it was queried by
        // day, hour, or minute, each Data object will be inserted into separate database tables.
        int size = priceHistorical.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < priceHistorical.get(i).getData().length; j++) {

                // the current Data object being acted on
                Data datum = priceHistorical.get(i).getData()[j];

                long msPerMin = 60 * 1000;
                long msPerHr = 60 * msPerMin;
                long msPerDay = 24 * msPerHr;


                // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
                long rawTime = (long) (datum.getTime()) * 1000;
//                long rawTimeTomorrow = (long) (priceHistorical.get(i).getData()[i].getTime()) * 1000;
//                long difference = rawTimeTomorrow - rawTime;
//                if (i == 0) {
//                    if (difference > msPerDay) {
//                        // How big is the gap?
//                        // If difference > msPerDay * k...
//                        for (int k = 1; difference > (msPerDay * k); k++) {
//
//                        }
//
//                    }
//                } else if (i == 1) {
//                    if (difference > msPerHr) {
//                        //
//                    }
//                } else {
//                    if (difference > msPerMin) {
//                        //
//                    }
//                }

                // Isolates and splits the timestamp within each Data object into separate fields.
                // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
                Timestamp timestamp = new Timestamp(rawTime);
                LocalDateTime dateTime = timestamp.toLocalDateTime();


                // Sets the date. Date is universal to all databases.
                int day = dateTime.getDayOfMonth();
                int month = dateTime.getMonthValue();
                int year = dateTime.getYear();
                /*  Is month < 1 or > 12?
                    Is each month 1 less than the month after it?
                    Is month 2 / 4, 6, 9, 11 / other?
                    Is day < 1 or > 29 / 30/ 31?
                    If not,
                 */
                datum.setDay(day);
                datum.setMonth(month);
                datum.setYear(year);
                cryptoMapper.addPriceByDate(datum);

                // Sets the hour for the for the object to be inserted into the hourly database.
                if (i >= 1) {
                    int hour = dateTime.getHour();
                    datum.setHour(hour);
                    cryptoMapper.addPriceByHour(datum);
                }

                // Sets the minute for the object to be inserted into the minutely database.
                if (i == 2) {
                    int minute = dateTime.getMinute();
                    datum.setMinute(minute);
                    cryptoMapper.addPriceByMin(datum);
                }
            }
        }

        return priceHistorical;


//        // Maps the URLs to arrays of objects.
//        Data[] dataByDate = restTemplate.getForObject(queryPriceDate, Data[].class);
//        Data[] dataByHour = restTemplate.getForObject(queryPriceHour, Data[].class);
//        Data[] dataByMin = restTemplate.getForObject(queryPriceMinute, Data[].class);

        // Separates these Data arrays into arrays of PriceByDate, PriceByHour, & PriceByMin objects.

        // Declares new arrays of these object types. Must declare them null here or the following loops won't work.
//        PriceByDate[] priceByDate = null;
//        PriceByHour[] priceByHour = null;
//        PriceByMin[] priceByMin = null;
//
//        // Initializes the arrays.
//        for (int i = 0; i < dataByDate.length; i++) {
//
//            Timestamp timestamp = new Timestamp(dataByDate[i].getTime());
//            LocalDateTime dateTime = timestamp.toLocalDateTime();
//            int date = dateTime.getDayOfMonth() +
//                       dateTime.getMonthValue() +
//                       dateTime.getYear();
//
//            priceByDate[i].setDate(date);
//            priceByDate[i].setClose(dataByDate[i].getClose());
//            priceByDate[i].setHigh(dataByDate[i].getHigh());
//            priceByDate[i].setLow(dataByDate[i].getLow());
//            priceByDate[i].setVolumefrom(dataByDate[i].getVolumefrom());
//            priceByDate[i].setVolumeto(dataByDate[i].getVolumeto());
//        }
//
//        for (int i = 0; i < dataByHour.length; i++) {
//
//            Timestamp timestamp = new Timestamp(dataByDate[i].getTime());
//            LocalDateTime dateTime = timestamp.toLocalDateTime();
//            int date = dateTime.getDayOfMonth() +
//                    dateTime.getMonthValue() +
//                    dateTime.getYear();
//            int hour = dateTime.getHour();
//
//            priceByHour[i].setDate(date);
//            priceByHour[i].setHour(hour);
//            priceByHour[i].setClose(dataByDate[i].getClose());
//            priceByHour[i].setHigh(dataByDate[i].getHigh());
//            priceByHour[i].setLow(dataByDate[i].getLow());
//            priceByHour[i].setVolumefrom(dataByDate[i].getVolumefrom());
//            priceByHour[i].setVolumeto(dataByDate[i].getVolumeto());
//        }
//
//        for (int i = 0; i < dataByMin.length; i++) {
//
//            Timestamp timestamp = new Timestamp(dataByDate[i].getTime());
//            LocalDateTime dateTime = timestamp.toLocalDateTime();
//            int date = dateTime.getDayOfMonth() +
//                    dateTime.getMonthValue() +
//                    dateTime.getYear();
//            int hour = dateTime.getHour();
//            int min = dateTime.getMinute();
//
//            priceByDate[i].setDate(date);
//            priceByHour[i].setHour(hour);
//            priceByMin[i].setMin(min);
//            priceByDate[i].setClose(dataByDate[i].getClose());
//            priceByDate[i].setHigh(dataByDate[i].getHigh());
//            priceByDate[i].setLow(dataByDate[i].getLow());
//            priceByDate[i].setVolumefrom(dataByDate[i].getVolumefrom());
//            priceByDate[i].setVolumeto(dataByDate[i].getVolumeto());
//        }
//
//        // Adds the mapped objects to the database.
//        cryptoMapper.addPriceByDate(priceByDate);
//        cryptoMapper.addPriceByHourAndDate(priceByHour);
//        cryptoMapper.addPriceByMinAndDate(priceByMin);
//
//        return cryptoMapper.getPrices();
//    }
    }
}
