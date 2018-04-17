package crypto_compare_exercise;

import crypto_compare_exercise.models.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CryptoService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CryptoMapper cryptoMapper;

    public Data[] addPriceHistorical(String fromCurrency, String toCurrency) {

        // the number of records to return, to be adjusted for calls for records/hour and records/minute
        // min 2, returns 1+ the specified amount -- i.e. if 5, returns 6
        int numRecords = 5;
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

        PriceHistorical phDate = restTemplate.getForObject(queryPriceDate, PriceHistorical.class);
        PriceHistorical phHour = restTemplate.getForObject(queryPriceHour, PriceHistorical.class);
        PriceHistorical phMinute = restTemplate.getForObject(queryPriceMinute, PriceHistorical.class);

        // Adds to the data_by_date database.
        for (int i = 0; i < phDate.getData().length; i++) {

            // The Data object being acted upon.
            Data datum = phDate.getData()[i];

            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
            long rawTime = (long) (datum.getTime()) * 1000;

            // Isolates and splits the timestamp within each Data object into separate fields.
            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
            Timestamp timestamp = new Timestamp(rawTime);
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Sets the date. Date is universal to all databases.
            int day = dateTime.getDayOfMonth();
            int month = dateTime.getMonthValue();
            int year = dateTime.getYear();

            datum.setDay(day);
            datum.setMonth(month);
            datum.setYear(year);
            cryptoMapper.addPriceByDate(datum);
        }

        // Adds data to the data_by_hour database.
        for (int i = 0; i < phHour.getData().length; i++) {

            // The Data object being acted upon.
            Data datum = phHour.getData()[i];

            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
            long rawTime = (long) (datum.getTime()) * 1000;

            // Isolates and splits the timestamp within each Data object into separate fields.
            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
            Timestamp timestamp = new Timestamp(rawTime);
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Sets the date and time.
            int hour = dateTime.getHour();
            int day = dateTime.getDayOfMonth();
            int month = dateTime.getMonthValue();
            int year = dateTime.getYear();

            datum.setHour(hour);
            datum.setDay(day);
            datum.setMonth(month);
            datum.setYear(year);
            cryptoMapper.addPriceByHour(datum);
        }

        // Adds data to the data_by_minute database.
        for (int i = 0; i < phMinute.getData().length; i++) {

            // The Data object being acted upon.
            Data datum = phMinute.getData()[i];

            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
            long rawTime = (long) (datum.getTime()) * 1000;

            // Isolates and splits the timestamp within each Data object into separate fields.
            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
            Timestamp timestamp = new Timestamp(rawTime);
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Sets the date and time.
            int minute = dateTime.getMinute();
            int hour = dateTime.getHour();
            int day = dateTime.getDayOfMonth();
            int month = dateTime.getMonthValue();
            int year = dateTime.getYear();

            datum.setMinute(minute);
            datum.setHour(hour);
            datum.setDay(day);
            datum.setMonth(month);
            datum.setYear(year);
            cryptoMapper.addPriceByMinute(datum);
        }

        return cryptoMapper.getPriceByDate();


//        PriceHistorical priceHistorical = restTemplate.getForObject(queryPriceDate, PriceHistorical.class);
//        Data[] data = priceHistorical.getData();
//
//        for(Data datum : data) {
//            cryptoMapper.addPrices(datum);
//        }



//        // Maps the results to an ArrayList of objects. They must be in this order for the rest of the code to work as
//        // upcoming code will perform specific operations based on the object's index in the ArrayList!
//        ArrayList<PriceHistorical> priceHistorical = new ArrayList<>();
//        priceHistorical.add(restTemplate.getForObject(queryPriceDate, PriceHistorical.class));
//        priceHistorical.add(restTemplate.getForObject(queryPriceHour, PriceHistorical.class));
//        priceHistorical.add(restTemplate.getForObject(queryPriceMinute, PriceHistorical.class));
//
//        // Isolates the Data objects within each of the three PriceHistorical objects and splits the timestamps in each
//        // Data object into separate fields (day, month, year, hour, & minute). Depending on whether it was queried by
//        // day, hour, or minute, each Data object will be inserted into separate database tables.
//        int size = priceHistorical.size();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < priceHistorical.get(i).getData().length; j++) {
//
//                // the current Data object being acted on
//                Data datum = priceHistorical.get(i).getData()[j];
//
//                // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
//                long rawTime = (long) (datum.getTime()) * 1000;
//
//                // Isolates and splits the timestamp within each Data object into separate fields.
//                // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
//                Timestamp timestamp = new Timestamp(rawTime);
//                LocalDateTime dateTime = timestamp.toLocalDateTime();
//
//                // Sets the date. Date is universal to all databases.
//                int day = dateTime.getDayOfMonth();
//                int month = dateTime.getMonthValue();
//                int year = dateTime.getYear();
//
//                datum.setDay(day);
//                datum.setMonth(month);
//                datum.setYear(year);
//                cryptoMapper.addPriceByDate(datum);
//
////                // Sets the hour for the for the object to be inserted into the hourly database.
////                if (i >= 1) {
////                    int hour = dateTime.getHour();
////                    datum.setHour(hour);
////                    cryptoMapper.addPriceByHour(datum);
////                }
////
////                // Sets the minute for the object to be inserted into the minutely database.
////                if (i == 2) {
////                    int minute = dateTime.getMinute();
////                    datum.setMinute(minute);
////                    cryptoMapper.addPriceByMin(datum);
////                }
//
//                switch (i) {
//                    // Sets the hour for the for the object to be inserted into the hourly database.
//                    case 1:
//                        int hour = dateTime.getHour();
//                        datum.setHour(hour);
//                        cryptoMapper.addPriceByHour(datum);
//                    // Sets the minute for the object to be inserted into the minutely database.
//                    case 2:
//                        int minute = dateTime.getMinute();
//                        datum.setMinute(minute);
//                        cryptoMapper.addPriceByMin(datum);
//                }
//            }
//        }
//
//        // ARE THERE MISSING VALUES???
//
//        return priceHistorical;
//
//
//
//    }
    }

    /********************************* 3 SETS OF METHODS THAT FIND & FILL GAPS *********************************/
    /*  Currently separated by the data they are referencing (by date, by hour, and by minute) for debugging purposes.
        Daily, hourly, and minutely data are inserted into separate databases.
     */

    /********************************* DATE DATABASE - FINDS & FILLS GAPS *********************************/
    // Checks the databases to see if there are missing values.
    // Gets an array of all timestamps, sorts the array, and finds missing values, which are added to a new array which
    // is then passed to a method that will query for those timestamp values and add them to the database.
    public ArrayList<Integer> seekMissingDateValues() {

        // Finds gaps in the data_by_dates database.
//        Data[] dataByDate = cryptoMapper.getPriceByDate();
        Integer[] timestampsByDate = cryptoMapper.getTimestampByDate();
        int SECONDS_PER_DAY = 86400;
        int difference;
        ArrayList<Integer> missingTimestamps = new ArrayList<>();

        // Sort timestampsByDate.
        MergeSort.mergeSort(timestampsByDate);

        // Finds missing values in the timestamp array.
        for (int i = 0; i < timestampsByDate.length - 1; i++) {
            difference = timestampsByDate[i+1] - timestampsByDate[i];
            int numDaysMissing = difference / SECONDS_PER_DAY;
            if (numDaysMissing > 1) {

                // Must be < numDaysMissing as this value is the next found value in the database.
                for (int j = 1; j < numDaysMissing; j++) {
                    missingTimestamps.add(timestampsByDate[i] + SECONDS_PER_DAY * j);
                }
            }
        }

        if (missingTimestamps.size() > 0) {
            addMissingDateValues(missingTimestamps);
        }

        return missingTimestamps;
    }

    public void addMissingDateValues(ArrayList<Integer> missingTimestamps) {

        // Queries the CryptoCompareAPI to fill in the missing gaps.
        for (Integer ts : missingTimestamps) {

            String fromCurrency = "BTC";
            String toCurrency = "USD";

            String queryPriceDate = "https://min-api.cryptocompare.com/data/histoday?fsym="
                    + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + 1
                    + "&toTs=" + ts;
            PriceHistorical phDate = restTemplate.getForObject(queryPriceDate, PriceHistorical.class);

            // The Data object being acted upon. Want the first one because the CryptoCompare API returns at
            // least 2, even if the limit is set to 1.
            Data datum = phDate.getData()[1];

            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
            long rawTime = (long) (datum.getTime()) * 1000;

            // Isolates and splits the timestamp within each Data object into separate fields.
            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
            Timestamp timestamp = new Timestamp(rawTime);
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Sets the date.
            int day = dateTime.getDayOfMonth();
            int month = dateTime.getMonthValue();
            int year = dateTime.getYear();

            datum.setDay(day);
            datum.setMonth(month);
            datum.setYear(year);

            // Adds the data to the database.
            cryptoMapper.addPriceByDate(datum);
        }
    }

    /********************************* HOUR DATABASE - FINDS & FILLS GAPS *********************************/
    // Checks the databases to see if there are missing values.
    // Gets an array of all timestamps, sorts the array, and finds missing values, which are added to a new array which
    // is then passed to a method that will query for those timestamp values and add them to the database.
    public ArrayList<Integer> seekMissingHourValues() {

        // Finds gaps in the data_by_dates database.
//        Data[] dataByDate = cryptoMapper.getPriceByDate();
        Integer[] timestampsByHour = cryptoMapper.getTimestampByHour();
        int SECONDS_PER_HOUR = 3600;
        int difference;
        ArrayList<Integer> missingTimestamps = new ArrayList<>();

        // Sort timestampsByDate.
        MergeSort.mergeSort(timestampsByHour);

        // Finds missing values in the timestamp array.
        for (int i = 0; i < timestampsByHour.length - 1; i++) {
            difference = timestampsByHour[i+1] - timestampsByHour[i];
            int numDaysMissing = difference / SECONDS_PER_HOUR;
            if (numDaysMissing > 1) {

                // Must be < numDaysMissing as this value is the next found value in the database.
                for (int j = 1; j < numDaysMissing; j++) {
                    missingTimestamps.add(timestampsByHour[i] + SECONDS_PER_HOUR * j);
                }
            }
        }

        if (missingTimestamps.size() > 0) {
            addMissingHourValues(missingTimestamps);
        }

        return missingTimestamps;
    }

    public void addMissingHourValues(ArrayList<Integer> missingTimestamps) {

        // Queries the CryptoCompareAPI to fill in the missing gaps.
        for (Integer ts : missingTimestamps) {

            String fromCurrency = "BTC";
            String toCurrency = "USD";

            String queryPriceHour = "https://min-api.cryptocompare.com/data/histohour?fsym="
                    + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + 1
                    + "&toTs=" + ts;
            PriceHistorical phHour = restTemplate.getForObject(queryPriceHour, PriceHistorical.class);

            // The Data object being acted upon. Want the first one because the CryptoCompare API returns at
            // least 2, even if the limit is set to 1.
            Data datum = phHour.getData()[1];

            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
            long rawTime = (long) (datum.getTime()) * 1000;

            // Isolates and splits the timestamp within each Data object into separate fields.
            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
            Timestamp timestamp = new Timestamp(rawTime);
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Sets the date.
            int hour = dateTime.getHour();
            int day = dateTime.getDayOfMonth();
            int month = dateTime.getMonthValue();
            int year = dateTime.getYear();

            datum.setHour(hour);
            datum.setDay(day);
            datum.setMonth(month);
            datum.setYear(year);

            // Adds the data to the database.
            cryptoMapper.addPriceByHour(datum);
        }
    }

    /********************************* DATE DATABASE - FINDS & FILLS GAPS *********************************/
    // Checks the databases to see if there are missing values.
    // Gets an array of all timestamps, sorts the array, and finds missing values, which are added to a new array which
    // is then passed to a method that will query for those timestamp values and add them to the database.
    public ArrayList<Integer> seekMissingMinuteValues() {

        // Finds gaps in the data_by_dates database.
//        Data[] dataByDate = cryptoMapper.getPriceByDate();
        Integer[] timestampsByMinute = cryptoMapper.getTimestampByMinute();
        int SECONDS_PER_MINUTE = 60;
        int difference;
        ArrayList<Integer> missingTimestamps = new ArrayList<>();

        // Sort timestampsByDate.
        MergeSort.mergeSort(timestampsByMinute);

        // Finds missing values in the timestamp array.
        for (int i = 0; i < timestampsByMinute.length - 1; i++) {
            difference = timestampsByMinute[i+1] - timestampsByMinute[i];
            int numDaysMissing = difference / SECONDS_PER_MINUTE;
            if (numDaysMissing > 1) {

                // Must be < numDaysMissing as this value is the next found value in the database.
                for (int j = 1; j < numDaysMissing; j++) {
                    missingTimestamps.add(timestampsByMinute[i] + SECONDS_PER_MINUTE * j);
                }
            }
        }

        if (missingTimestamps.size() > 0) {
            addMissingMinuteValues(missingTimestamps);
        }

        return missingTimestamps;
    }

    public void addMissingMinuteValues(ArrayList<Integer> missingTimestamps) {

        // Queries the CryptoCompareAPI to fill in the missing gaps.
        for (Integer ts : missingTimestamps) {

            String fromCurrency = "BTC";
            String toCurrency = "USD";

            String queryPriceMinute = "https://min-api.cryptocompare.com/data/histominute?fsym="
                    + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + 1
                    + "&toTs=" + ts;
            PriceHistorical phMinute = restTemplate.getForObject(queryPriceMinute, PriceHistorical.class);

            // The Data object being acted upon. Want the first one because the CryptoCompare API returns at
            // least 2, even if the limit is set to 1.
            Data datum = phMinute.getData()[1];

            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
            long rawTime = (long) (datum.getTime()) * 1000;

            // Isolates and splits the timestamp within each Data object into separate fields.
            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
            Timestamp timestamp = new Timestamp(rawTime);
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            // Sets the minute.
            int minute = dateTime.getMinute();
            int hour = dateTime.getHour();
            int day = dateTime.getDayOfMonth();
            int month = dateTime.getMonthValue();
            int year = dateTime.getYear();

            datum.setMinute(minute);
            datum.setHour(hour);
            datum.setDay(day);
            datum.setMonth(month);
            datum.setYear(year);

            // Adds the data to the database.
            cryptoMapper.addPriceByMinute(datum);
        }
    }


    // Searches each database for gaps in the data.
//    public void findGaps() {
//        int msPerMin = 60 * 1000;
//        int msPerHour = 60 * msPerMin;
//        int msPerDay = 24 * msPerHour;
//
//        ArrayList<PriceHistorical> priceHistorical = new ArrayList();
////        int size = priceHistorical.size();
//        for (int i = 0; i < 3; i++) {
//            PriceHistorical element = userMapper.getAllByDate();
//            priceHistorical.add(element);
//            for (int j = 0; j < element.get(i).getData().length; j++) {
//
//            }
/*
            for each database, call a cryptoMapper method find all, add to a PriceHistorical object.

            for data_by_date database, a for loop iterates through each Data object beginning with i = 1
                difference = timestamp[i] - timestamp[i-1] // measures the difference between the current timestamp and the previous
                if (difference > msPerDay) // if the difference is greater than one day
                    daysMissing = difference / msPerDay // how many days are missing?
                    limit = daysMissing // the parameter to request the number of data points to return
                    toTs = timestamp[i] - msPerDay; // the last datapoint to return is the one day before the current timestamp
                    call a method that performs the query for data_by_date and inserts into our database


            for data by hour database
                for each day
                    if
            for data by min database
         */
//    }
}
