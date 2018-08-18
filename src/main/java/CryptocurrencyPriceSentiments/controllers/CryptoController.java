package CryptocurrencyPriceSentiments.controllers;

import CryptocurrencyPriceSentiments.CryptoMapper;
import CryptocurrencyPriceSentiments.exceptions.TableEmptyException;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.services.AsyncTasks;
import CryptocurrencyPriceSentiments.services.DataCollection;
import CryptocurrencyPriceSentiments.services.SentimentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @Autowired
    DataCollection dataCollection;

    @Autowired
    AsyncTasks asyncTasks;

    @Autowired
    SentimentAnalysis sentimentAnalysis;

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

    @GetMapping("/news/add")
    public GeneralResponse addNews(@RequestParam(value = "categories", required = false) String categories)
            throws TableEmptyException {

        if (categories == null) asyncTasks.addNews();
        else asyncTasks.addNews(categories);

        return dataCollection.getNews();
    }

    @GetMapping("/news/getsentiments")
    public GeneralResponse addSentimentsForCurrencies() throws TableEmptyException {

//        sentimentAnalysis.addSentimentsForCurrencies();
        return new GeneralResponse(HttpStatus.OK, "OK", cryptoMapper.getSentiments());
    }

//    @RequestMapping("/missingvalues")
//    public ArrayList<Integer> seekMissingValues() {
//        return dataCollection.seekMissingMinuteValues();
//    }
}
