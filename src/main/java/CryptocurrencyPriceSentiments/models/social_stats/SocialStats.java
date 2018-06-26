package crypto_compare_exercise.models.social_stats;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SocialStats {

    @JsonProperty("Data")
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
