package crypto_compare_exercise.models.social_stats;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Facebook {

    int likes;
    @JsonProperty("is_closed")
    String isClosed;
    @JsonProperty("talking_about")
    int talkingAbout;
    String name;
    @JsonProperty("Points")
    int points;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public int getTalkingAbout() {
        return talkingAbout;
    }

    public void setTalkingAbout(int talkingAbout) {
        this.talkingAbout = talkingAbout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
