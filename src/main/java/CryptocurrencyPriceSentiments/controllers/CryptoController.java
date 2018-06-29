package CryptocurrencyPriceSentiments.controllers;

import CryptocurrencyPriceSentiments.models.Data;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.services.AsyncDataCollection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
public class CryptoController {

    // Other possible options are:
    //      aggregate (time period to aggregate over)
    //      limit (number of data points to return)
    //      toTs (last Unix timestamp to return data for)
    // So far, these are set to a default value by the developer.
    @RequestMapping("/gethistory")
    public Future<GeneralResponse> addPriceHistorical(@RequestParam(value = "period") String period,
                                                      @RequestParam(value = "numrecords") int numRecords) throws Exception {
        AsyncDataCollection asyncDataCollection = new AsyncDataCollection();
        return asyncDataCollection.backloadData(period, numRecords);
    }

//    @RequestMapping("/missingvalues")
//    public ArrayList<Integer> seekMissingValues() {
//        return dataCollection.seekMissingMinuteValues();
//    }
}
