package CryptocurrencyPriceSentiments.controllers;

import CryptocurrencyPriceSentiments.exceptions.TableEmptyException;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.services.AsyncTasks;
import CryptocurrencyPriceSentiments.services.DataCollection;
import CryptocurrencyPriceSentiments.services.SentimentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.Future;

@RestController
public class CryptoController {

    @Autowired
    DataCollection dataCollection;

    @Autowired
    SentimentAnalysis sentimentAnalysis;

    // Other possible options are:
    //      aggregate (time period to aggregate over)
    //      limit (number of data points to return)
    //      toTs (last Unix timestamp to return data for)
    // So far, these are set to a default value by the developer.
    @GetMapping("/gethistory")
    public Future<GeneralResponse> addPriceHistorical(@RequestParam(value = "period") String period,
                                                      @RequestParam(value = "numrecords") int numRecords) throws Exception {

        AsyncTasks asyncTasks = new AsyncTasks();
        return asyncTasks.backloadData(period, numRecords);
    }

    @GetMapping("/news/add")
    public GeneralResponse addNews(@RequestParam(value = "categories") String categories) throws TableEmptyException {

        AsyncTasks asyncTasks = new AsyncTasks();
        asyncTasks.addNews(categories);
        return dataCollection.getNews();
    }

//    @RequestMapping("/missingvalues")
//    public ArrayList<Integer> seekMissingValues() {
//        return dataCollection.seekMissingMinuteValues();
//    }
}
