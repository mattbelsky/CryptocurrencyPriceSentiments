package crypto_compare_exercise.services;

import crypto_compare_exercise.models.social_stats.SocialStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.springframework.http.HttpStatus.*;

@Service
public class SocialService {

    /*  CryptoCompare Coin ID List
            BTC: 1182
            ETH: 7605
            LTH: 3808
            IOT: 127356
            BCH: 202330
            XMR: 5038
            NEO: 27368
            TRX: 310829
            ZEC: 24854
            REBL: 370889
            ETC: 5324
     */

    /* Possible CryptoCompare SocialStats API query parameters
            id: the coin id
     */

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<SocialStats> getSocialStats() {

        int[] coinId = {1182, 7605, 3808, 127356, 202330, 5038, 27368, 310829, 24854, 370889};
        SocialStats[] stats = new SocialStats[coinId.length];

        for (int i = 0; i < coinId.length; i++) {
            String query = "https://www.cryptocompare.com/api/data/socialstats/?id=" + coinId[i];
            stats[i] = restTemplate.getForObject(query, SocialStats.class);
        }

        ResponseEntity<SocialStats> response = new ResponseEntity(stats, HttpStatus.ACCEPTED);
        return response;
    }
}
