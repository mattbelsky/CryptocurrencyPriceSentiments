package crypto_compare_exercise.models.social_stats;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Twitter {

    int following;
    int account_creation;
    String name;
    int lists;
    int statuses;
    int favourites;
    int followers;
    @JsonProperty("Points")
    int points;

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getAccount_creation() {
        return account_creation;
    }

    public void setAccount_creation(int account_creation) {
        this.account_creation = account_creation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLists() {
        return lists;
    }

    public void setLists(int lists) {
        this.lists = lists;
    }

    public int getStatuses() {
        return statuses;
    }

    public void setStatuses(int statuses) {
        this.statuses = statuses;
    }

    public int getFavourites() {
        return favourites;
    }

    public void setFavourites(int favourites) {
        this.favourites = favourites;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
