package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ScheduledTasks {

    DataCollection dataCollection;
    AsyncTasks asyncTasks;
    private boolean cronHit;
    protected ArrayList<Integer> timestampDaily;
    protected ArrayList<Integer> timestampHourly;
    protected ArrayList<Integer> timestampMinutely;

    @Autowired
    public ScheduledTasks(DataCollection dataCollection, AsyncTasks asyncTasks) {
        this.dataCollection = dataCollection;
        this.asyncTasks = asyncTasks;
        this.cronHit = false;
        this.timestampDaily = new ArrayList<>();
        this.timestampHourly = new ArrayList<>();
        this.timestampMinutely = new ArrayList<>();
    }

    /**
     * Gets the timestamp of midnight of the current day at 0:02.
     */
    @Scheduled(cron = "0 2 0 * * *", zone = "GMT")
    private void queryTimestampDaily() {

        int now = (int) (System.currentTimeMillis() / 1000);
        int midnight = now - TimePeriod.SEC_IN_MIN.getValue() * 2;
        cronHit = true;

        timestampDaily.add(midnight);
        dataCollection.switchCronOps("day");

        // Resets the global variables.
        cronHit = false;
        timestampDaily.clear();

        // Adds news stories on a daily basis.
        asyncTasks.addNews();
    }

    /**
     * Gets the timestamp of the previous hour in the first minute of every hour and adds social media data for the
     * previous hour.
     */
    @Scheduled(cron = "0 1 * * * *", zone = "GMT")
    private void queryTimestampHourly() {

        int now = (int) (System.currentTimeMillis() / 1000);
        int hour = now - TimePeriod.SEC_IN_MIN.getValue();
        cronHit = true;

        timestampHourly.add(hour);
        dataCollection.switchCronOps("hour");

        // Resets the global variables.
        cronHit = false;
        timestampHourly.clear();
    }

    /**
     * Generates an array list of timestamps every five minutes for the previous five minutes.
     */
    @Scheduled(cron = "0 */5 * * * *", zone = "GMT")
    private void queryTimestampMinutely() {

        int now = (int) (System.currentTimeMillis() / 1000);
        cronHit = true;

        for (int j = 0; j < 5; j++) {
            timestampMinutely.add(now - TimePeriod.SEC_IN_MIN.getValue() * j);
        }

        dataCollection.switchCronOps("minute");

        // Resets the global variables.
        cronHit = false;
        timestampMinutely.clear();
    }

    public ArrayList<Integer> getTimestampDaily() { return timestampDaily; }

    public ArrayList<Integer> getTimestampHourly() {
        return timestampHourly;
    }

    public ArrayList<Integer> getTimestampMinutely() {
        return timestampMinutely;
    }

    public boolean isCronHit() {
        return cronHit;
    }

    public void setCronHit(boolean cronHit) {
        this.cronHit = cronHit;
    }
}
