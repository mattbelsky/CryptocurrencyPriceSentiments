package crypto_compare_exercise.controllers;

import crypto_compare_exercise.models.social_stats.SocialStats;
import crypto_compare_exercise.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocialController {

    @Autowired
    SocialService socialService;

    @RequestMapping("/socialstats")
    public ResponseEntity<SocialStats> getSocialStats() {

        return socialService.getSocialStats();
    }
}
