package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.CryptoMapper;
import CryptocurrencyPriceSentiments.exceptions.TableEmptyException;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.models.news.Data;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SentimentAnalysis {

    @Value("${watson.url}")
    private String url;

    @Value("${watson.username}")
    private String username;

    @Value("${watson.password}")
    private String password;

    @Autowired
    CryptoMapper cryptoMapper;

    public ArrayList<ToneAnalysis> analyzeTone(String categories) throws TableEmptyException {

        ArrayList<ToneAnalysis> toneList = new ArrayList<>();
        Data[] stories = cryptoMapper.getNewsByCategory(categories);

        for (Data story : stories) {
            String newsBody = story.getBody();
            ToneAnalyzer service = new ToneAnalyzer("2017-09-21");
            service.setUsernameAndPassword(username, password);

            // Call the service and get the tone
            ToneOptions toneOptions = new ToneOptions.Builder()
                    .html(newsBody)
                    .build();

            ToneAnalysis tone = service.tone(toneOptions).execute();
            toneList.add(tone);
        }

        return toneList;
    }
}
