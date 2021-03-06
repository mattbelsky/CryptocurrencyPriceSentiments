package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.mappers.CryptoMapper;
import CryptocurrencyPriceSentiments.exceptions.InvalidDirectionException;
import CryptocurrencyPriceSentiments.exceptions.InvalidToneException;
import CryptocurrencyPriceSentiments.exceptions.TableEmptyException;
import CryptocurrencyPriceSentiments.models.WatsonTone;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.CurrencySentiment;
import CryptocurrencyPriceSentiments.models.news.Data;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SentimentAnalysis {

    @Value("${watson.username}")
    private String username;

    @Value("${watson.password}")
    private String password;

    @Autowired
    CryptoMapper cryptoMapper;

    @Cacheable(value = "WatsonTones")
    public ArrayList<WatsonTone> getAllWatsonTones() {
        return cryptoMapper.getAllWatsonTones();
    }

    public ArrayList<String> getWatsonToneNames() {
        return cryptoMapper.getToneNames();
    }

    /**
     * Calls Watson Tone Analyzer to analyze the tone of a news story.
     * @param story the story to analyze
     * @return the tone of the story, as analyzed by Watson
     * @throws TableEmptyException
     */
    public ArrayList<ToneAnalysis> analyzeTone(Data story) throws TableEmptyException {

        ArrayList<ToneAnalysis> toneList = new ArrayList<>();
        String newsBody = story.getBody();
        ToneAnalyzer service = new ToneAnalyzer("2017-09-21");
        service.setUsernameAndPassword(username, password);

        // Call the service and get the tone
        ToneOptions toneOptions = new ToneOptions.Builder()
                .html(newsBody)
                .build();

        ToneAnalysis tone = service.tone(toneOptions).execute();
        toneList.add(tone);

        return toneList;
    }

    /**
     * Gets a list of news stories associated with each currency, calls the analyzeTone() method to return a sentiment
     * for each story, and persists data related to each story and its sentiment to the database.
     * NOTE: Articles will often return multiple sentiments, which is why there are more sentiment values for a
     * particular currency than articles.
     * @throws TableEmptyException
     */
    public void addSentimentsForCurrencies() throws TableEmptyException {

        ArrayList<String> currencies = cryptoMapper.getCurrencies();

        for (String currency : currencies) {

            ArrayList<Data> stories = cryptoMapper.getNewsByCategory(currency);

            for (Data story : stories) {

                ArrayList<ToneAnalysis> toneAnalyses = analyzeTone(story);
                int publishedOn = story.getPublishedOn();

                for (ToneAnalysis toneAnalysis : toneAnalyses) {

                    List<ToneScore> tones = toneAnalysis.getDocumentTone().getTones();

                    for (ToneScore tone : tones) {

                        String sentiment = tone.getToneId();
                        double score = tone.getScore();

                        cryptoMapper.addSentiments(new CurrencySentiment(currency, publishedOn, sentiment, score));
                    }
                }
            }
        }
    }

    /**
     * Updates the specified Watson tone with the specified tone. Throws exceptions if the tone or direction are invalid.
     * @param direction
     * @param tone
     * @return the updated list of Watson tones
     * @throws InvalidToneException
     * @throws InvalidDirectionException
     */
    public ArrayList<WatsonTone> updateWatsonToneDirection(String tone, String direction)
            throws InvalidToneException, InvalidDirectionException {

        if (!isValidTone(tone)) throw new InvalidToneException(HttpStatus.BAD_REQUEST, "Invalid tone: " + tone.toUpperCase());

        if (!direction.equals("positive") && !direction.equals("neutral") && !direction.equals("negative"))
            throw new InvalidDirectionException(HttpStatus.BAD_REQUEST, "Invalid direction: " + direction.toUpperCase());

        cryptoMapper.updateWatsonToneDirection(tone, direction);
        return cryptoMapper.getAllWatsonTones();
    }

    /**
     * Checks if the specified Watson tone is valid.
     * @param tone -- the tone to check
     * @return
     */
    public boolean isValidTone(String tone) {

        if (getWatsonToneNames().contains(tone)) return true;
        else return false;
    }
}
