package crypto_compare_exercise.models.social_stats;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

    @JsonProperty("General")
    General general;
    @JsonProperty("Twitter")
    Twitter twitter;
    @JsonProperty("Reddit")
    Reddit reddit;
    @JsonProperty("Facebook")
    Facebook facebook;
    @JsonProperty("CodeRepository")
    CodeRepository codeRepository;

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public Reddit getReddit() {
        return reddit;
    }

    public void setReddit(Reddit reddit) {
        this.reddit = reddit;
    }

    public Facebook getFacebook() {
        return facebook;
    }

    public void setFacebook(Facebook facebook) {
        this.facebook = facebook;
    }
}
