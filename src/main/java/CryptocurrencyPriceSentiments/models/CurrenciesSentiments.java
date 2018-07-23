package CryptocurrencyPriceSentiments.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrenciesSentiments {

    int id;
    @JsonProperty("currency_symbol")
    String currencySymbol;
    @JsonProperty("published_on")
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
