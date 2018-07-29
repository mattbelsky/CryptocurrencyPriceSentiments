package CryptocurrencyPriceSentiments;

import CryptocurrencyPriceSentiments.services.DataCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DataCollection dataCollection;

    public AppRunner(DataCollection dataCollection) {
        this.dataCollection = dataCollection;
    }

    /**
     * Queries the database for the list of traded currency pairs on application startup.
     * @param args required to properly implement the CommandLineRunner interface
     */
    @Override
    public void run(String[] args) {
        dataCollection.getTradingPairs();
        logger.info("Trading pairs retrieved and cached.");
    }
}
