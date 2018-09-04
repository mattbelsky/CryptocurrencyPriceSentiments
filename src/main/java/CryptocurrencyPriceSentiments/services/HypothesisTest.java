package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.CryptoMapper;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.DirectionResponseWrapper;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.PriceChangeDbEntity;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.PriceChangeSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class HypothesisTest {

    /*  Organize sentiments by positive or negative
        Get all CurrencySentiment by positive/negative sentiment and currency.
        Organized by sentiment and currency, get each timestamp for each story
        For each timestamp, get daily data where timestamp <= 24 hours greater than timestamp of story
            and where fromCurrency = currency.

        Hypothesis to test: A positive or negative sentiment will move the closing price vs USD up or down within one
        day > 60% of the time.
            H0:
                p <= 0.6

            Ha:
                p > 0.6

        countPosStories
        countNegStories
        ArrayList newsData = get all news stories
        numStories = newsData.size

        need an arraylist of resultsPositive (currency, direction, proportion successful)
        arraylist resultsNegative(...)
        how many currency/sentiment results are there for each currency?
        how many currency/sentiment results have a positive direction?
        results.add(currency, direction, proportion)

        REMINDER: I want to see the proportion of cases where the closing price moves up or down significantly (define
        significant) within one day of a positive or negative news story.

        significance -- should be initially set to 1
            Want to see whether price moves up or down at all, then later I can refine the significance level based on
            prior data.
        for each currency
            Can use a stored procedure -- faster as multiple db calls happening for one calculation

        alpha = 0.05
        p = 0.5
     */

    @Autowired
    CryptoMapper cryptoMapper;

    Logger logger = LoggerFactory.getLogger(HypothesisTest.class);

    /**
     * Calculates the proportion of times a positively or negatively toned news story results in a positive or negative
     * price change, respectively. The tones are the result of analysis performed by Watson Tone Analyzer and persisted
     * in the database, and each tone's direction (positive, negative, or neutral) is decided initially by whomever
     * sets up the watson_tones table in the database, although they can be modified later by the user.
     * @return an array of response wrapper objects that contain proportional data for each currency
     */
    public DirectionResponseWrapper[] calculateProportionOfSuccesses() {

        ArrayList<String> currencies = cryptoMapper.getCurrencies();
        ArrayList<PriceChangeSummary> priceChangePositive = new ArrayList<>();
        ArrayList<PriceChangeSummary> priceChangeNegative = new ArrayList<>();
        DirectionResponseWrapper[] responseWrapper = new DirectionResponseWrapper[2];

        for (String currency : currencies) {

            // Price change for each currency is measured as a result of it's change against the USD, therefore USD/USD
            // is an irrelevant result.
            if (currency.equals("USD")) continue;

            // Because multiple SELECT statements are required for the result, the following calls a MySQL stored
            // procedure, feeding it the input parameters contained in the PriceChangeDbEntity and assigning the output
            // parameters to the same object. The rather verbose data contained in this object is streamlined into a
            // summary object, which is added to the PriceChangeSummary array list.
            PriceChangeDbEntity priceChangeDbEntityPositive = new PriceChangeDbEntity(currency, "positive");
            cryptoMapper.getPriceChangeByCurrencyAndToneDirection(priceChangeDbEntityPositive);
            priceChangePositive.add(convertPriceChangeDbEntityToSummary(priceChangeDbEntityPositive));

            PriceChangeDbEntity priceChangeDbEntityNegative = new PriceChangeDbEntity(currency, "negative");
            cryptoMapper.getPriceChangeByCurrencyAndToneDirection(priceChangeDbEntityNegative);
            priceChangeNegative.add(convertPriceChangeDbEntityToSummary(priceChangeDbEntityNegative));
        }

        responseWrapper[0] = new DirectionResponseWrapper("positive", priceChangePositive);
        responseWrapper[1] = new DirectionResponseWrapper("negative", priceChangeNegative);

        return responseWrapper;
    }

    /**
     * Gets the desired information from the object associated with the MySQL stored procedure and adds it to a summary
     * object for returning to the user.
     * @param dbEntity -- the object associated with the stored procedure containing verbose repetitive data
     * @return the object containing the summarized data
     */
    public PriceChangeSummary convertPriceChangeDbEntityToSummary(PriceChangeDbEntity dbEntity) {
        return new PriceChangeSummary(dbEntity.getOutCurrencyName(), dbEntity.getOutToneDirection(),
                dbEntity.getOutProportionSuccess());
    }
}
