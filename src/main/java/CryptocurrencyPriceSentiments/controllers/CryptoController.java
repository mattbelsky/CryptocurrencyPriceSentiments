package crypto_compare_exercise.controllers;

import crypto_compare_exercise.models.Data;
import crypto_compare_exercise.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

    // Other possible options are:
    //      aggregate (time period to aggregate over)
    //      limit (number of data points to return)
    //      toTs (last Unix timestamp to return data for)
    // So far, these are set to a default value by the developer.
    @RequestMapping("/gethistory")
    public Data[] addPriceHistorical(@RequestParam(value = "fsym") String fromCurrency,
                                                         @RequestParam(value = "tsym") String toCurrency) {
        return cryptoService.addPriceHistorical(fromCurrency, toCurrency);
    }

    @RequestMapping("/missingvalues")
    public ArrayList<Integer> seekMissingValues() {
        return cryptoService.seekMissingMinuteValues();
    }
}
