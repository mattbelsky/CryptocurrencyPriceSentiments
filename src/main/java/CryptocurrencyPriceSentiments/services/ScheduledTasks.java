//package crypto_compare_exercise.services;
//
//import crypto_compare_exercise.CryptoMapper;
//import crypto_compare_exercise.models.Data;
//import crypto_compare_exercise.models.PriceHistorical;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//@Service
//public class ScheduledTasks {
//
//    @Autowired
//    CryptoService cryptoService;
//
//    @Autowired
//    CryptoMapper cryptoMapper;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    // Queries the CryptoCompare API for minutely data every 5 minutes and persists the data into the local database.
//    @Scheduled(fixedRate = 300000)
//    public void getLastFiveMinutes() {
//
//        /*  What is current timestamp?
//            var = Divide current timestamp by 1000 for querying.
//            Perform query
//                limit = 5
//                toTs = var
//            Get Data from response.
//            cryptoMapper.addPriceByMinute()
//
//         */
//        String fromCurrency = "BTC";
//        String toCurrency = "USD";
//        int numRecords = 5;
//        int currentTimeSeconds = (int) (System.currentTimeMillis() / 1000);
//        String queryPriceMinute = "https://min-api.cryptocompare.com/data/histominute?fsym="
//                + fromCurrency + "&tsym=" + toCurrency + "&aggregate=1&limit=" + numRecords + "&toTs=" + currentTimeSeconds;
//        PriceHistorical phMinute = restTemplate.getForObject(queryPriceMinute, PriceHistorical.class);
//
//        // Adds data to the data_by_minute database.
//        for (int i = 0; i < phMinute.getData().length; i++) {
//
//            // The Data object being acted upon.
//            Data datum = phMinute.getData()[i];
//
//            // Multiplies the time integer by 1000 because it measures seconds rather than milliseconds.
//            long rawTime = (long) (datum.getTime()) * 1000;
//
//            // Isolates and splits the timestamp within each Data object into separate fields.
//            // Converts the timestamp into a LocalDateTime object, which has the methods to further split it up.
//            Timestamp timestamp = new Timestamp(rawTime);
//            LocalDateTime dateTime = timestamp.toLocalDateTime();
//
//            // Sets the date and time.
//            int minute = dateTime.getMinute();
//            int hour = dateTime.getHour();
//            int day = dateTime.getDayOfMonth();
//            int month = dateTime.getMonthValue();
//            int year = dateTime.getYear();
//
//            datum.setMinute(minute);
//            datum.setHour(hour);
//            datum.setDay(day);
//            datum.setMonth(month);
//            datum.setYear(year);
//            cryptoMapper.addPriceByMinute(datum);
//        }
//    }
//
//    /*  Scheduled tasks hourly:
//            "0 0 * * * *" = the top of every hour of every day. (cron expression)
//            Get current time.
//                Issues:
//                    System.currentTimeMillis() will be after the hour -- I want the timestamp of the last minute of the prev hour
//                        Solution:
//                            int timestamp = (int) (System.currentTimeMillis() / 1000);
//                            while (timestamp % 3600 > 0)
//                                timestamp -= 60;
//                            timestamp -= 60
//            Fill in gaps in minutely database -- cryptoService.seekMissingMinuteValues()
//            Query minutely database
//                close = where (time = timestamp)
//                high = max(high)
//                low = min(low)
//                volumeFrom = sum(volumeFrom)
//                volumeTo = sum(volumeTo)
//                where hour = get hour of timestamp, day = get day of timestamp, etc.
//
//     */
//    @Scheduled(cron = "0 0 * * * *")
//    public void aggregateHourly() {
//
//        // Gets the timestamp for the last minute of the previous hour
//        // Current timestamp will be just after the hour, so it needs to be decremented by at least one minute.
//        int timestamp = (int) (System.currentTimeMillis() / 1000);
//        while (timestamp % 3600 > 0) {
//            timestamp -= 60;
//        }
//        timestamp -= 60;
//
//        // Fills in the gaps in the minutely database.
//        cryptoService.seekMissingMinuteValues();
//
//
//
//        // Queries the minutely database for aggregate data for the last hour.
//        cryptoMapper.
//    }
//
//}
