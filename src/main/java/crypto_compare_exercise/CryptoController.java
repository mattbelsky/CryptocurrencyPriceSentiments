package crypto_compare_exercise;

import crypto_compare_exercise.models.Data;
import crypto_compare_exercise.models.PriceHistorical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

    @RequestMapping("/gethistory")
    public ArrayList<PriceHistorical> addPriceHistorical(@RequestParam(value = "fsym") String fromCurrency,
                                                         @RequestParam(value = "tsym") String toCurrency) {

        return cryptoService.addPriceHistorical(fromCurrency, toCurrency);
    }
}
