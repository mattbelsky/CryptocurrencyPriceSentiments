package CryptocurrencyPriceSentiments.controllers;

import CryptocurrencyPriceSentiments.CryptoMapper;
import CryptocurrencyPriceSentiments.exceptions.InvalidDirectionException;
import CryptocurrencyPriceSentiments.exceptions.InvalidToneException;
import CryptocurrencyPriceSentiments.exceptions.TableEmptyException;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.PriceChangeDbEntity;
import CryptocurrencyPriceSentiments.services.AsyncTasks;
import CryptocurrencyPriceSentiments.services.DataCollection;
import CryptocurrencyPriceSentiments.services.HypothesisTest;
import CryptocurrencyPriceSentiments.services.SentimentAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    DataCollection dataCollection;
    AsyncTasks asyncTasks;
    SentimentAnalysis sentimentAnalysis;
    HypothesisTest hypothesisTest;

    @Autowired
    public CryptoController(DataCollection dataCollection, AsyncTasks asyncTasks, SentimentAnalysis sentimentAnalysis,
                            HypothesisTest hypothesisTest) {
        this.dataCollection = dataCollection;
        this.asyncTasks = asyncTasks;
        this.sentimentAnalysis = sentimentAnalysis;
        this.hypothesisTest = hypothesisTest;
    }

    @Autowired
    CryptoMapper cryptoMapper;

    // Other possible options are:
    //      aggregate (time period to aggregate over)
    //      limit (number of data points to return)
    //      toTs (last Unix timestamp to return data for)
    // So far, these are set to a default value by the developer.
    // TODO Handle exception better in secondary thread.
    @GetMapping("/gethistory")
    public GeneralResponse addPriceHistorical(@RequestParam(value = "period") String period,
                                              @RequestParam(value = "numrecords") int numRecords) throws Exception {

        asyncTasks.backloadData(period, numRecords);
        return new GeneralResponse(HttpStatus.OK, "Data loaded.");
    }

    @PostMapping("/news")
    public GeneralResponse addNews(@RequestParam(value = "categories", required = false) String categories)
            throws TableEmptyException {

        if (categories == null) asyncTasks.addNews();
        else asyncTasks.addNews(categories);

        return new GeneralResponse(HttpStatus.OK, "Adding news data.", dataCollection.getNews());
    }

    @GetMapping("/news")
    public GeneralResponse getNews(@RequestParam(value = "categories", required = false) String categories)
            throws TableEmptyException {

        return new GeneralResponse(HttpStatus.OK, "News data successfully retrieved.", dataCollection.getNews());
    }

    @PostMapping("/news/sentiments")
    public GeneralResponse addSentimentsForCurrencies() throws TableEmptyException {

        sentimentAnalysis.addSentimentsForCurrencies();
        return new GeneralResponse(HttpStatus.OK, "News sentiments successfully added.", dataCollection.getSentiments());
    }

    @GetMapping("/news/sentiments")
    public GeneralResponse getSentiments() throws TableEmptyException {
        return new GeneralResponse(HttpStatus.OK, "News sentiments successfully retrieved.", dataCollection.getSentiments());
    }

    @GetMapping("news/sentimentssummary")
    public GeneralResponse getProportionOfSuccesses() {
        return new GeneralResponse(HttpStatus.OK, "Sentiments summary data successfully retrieved.",
                hypothesisTest.calculateProportionOfSuccesses());
    }

    @GetMapping("/watsontones/all")
    public GeneralResponse getAllWatsonTones() {
        return new GeneralResponse(HttpStatus.OK, "Watson tones successfully returned.",
                sentimentAnalysis.getAllWatsonTones());
    }

    @GetMapping("watsontones/update")
    public GeneralResponse updateWatsonToneDirection(@RequestParam("tone") String tone,
                                                     @RequestParam("direction") String direction)
            throws InvalidDirectionException, InvalidToneException {

        return new GeneralResponse(HttpStatus.OK, "Watson tone direction successfully updated.",
                sentimentAnalysis.updateWatsonToneDirection(tone, direction));
    }

//    @RequestMapping("/missingvalues")
//    public ArrayList<Integer> seekMissingValues() {
//        return dataCollection.seekMissingMinuteValues();
//    }
}
