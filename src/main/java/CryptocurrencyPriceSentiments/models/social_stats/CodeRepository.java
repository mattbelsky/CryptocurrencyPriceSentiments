package crypto_compare_exercise.models.social_stats;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CodeRepository {

    @JsonProperty("List")
    List[] list;
    @JsonProperty("Points")
    int points;

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
