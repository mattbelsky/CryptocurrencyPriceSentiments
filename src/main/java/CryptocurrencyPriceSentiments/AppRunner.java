package CryptocurrencyPriceSentiments;

import CryptocurrencyPriceSentiments.services.DataCollection;
import CryptocurrencyPriceSentiments.services.SentimentAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DataCollection dataCollection;
    private final CryptoMapper cryptoMapper;

    public AppRunner(DataCollection dataCollection, CryptoMapper cryptoMapper) {
        this.dataCollection = dataCollection;
        this.cryptoMapper = cryptoMapper;
    }

    /**
     * Queries the database for the list of traded currency pairs on application startup.
     * @param args required to properly implement the CommandLineRunner interface
     */
    @Override
    public void run(String[] args) {

        dataCollection.getTradingPairs();
        logger.info("Trading pairs retrieved and cached.");

        cryptoMapper.getToneNames();
        logger.info("Watson tones retrieved and cached.");
    }
}
