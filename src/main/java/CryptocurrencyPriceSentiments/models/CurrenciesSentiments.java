package CryptocurrencyPriceSentiments.models;

public class CurrenciesSentiments {

    // NOTE: @JsonProperty annotation does not work here, and I don't know why.
    // JSON result returns "currencySymbol" and "publishedOn" as null and 0, respectively.
    // I fixed the issue in the mapper by adding a custom result map, but it's not an ideal fix if this app is scaled.
    int id;
    String currencySymbol;
    int publishedOn;
    String sentiment;
    double score;

    public CurrenciesSentiments(String currencySymbol, int publishedOn, String sentiment, double score) {
        this.currencySymbol = currencySymbol;
        this.publishedOn = publishedOn;
        this.sentiment = sentiment;
        this.score = score;
    }

    public CurrenciesSentiments() {}

    public int getId() {
        return id;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public int getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(int publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
