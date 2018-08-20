package CryptocurrencyPriceSentiments.models.sentiment_analysis;

public class CurrencySentiment {

    /*  NOTE: @JsonProperty annotation does not work here, and I don't know why.
        JSON result returns "currencySymbol" and "publishedOn" as null and 0, respectively, despite annotating these two
        properties with this format:
            @JsonProperty("currency_symbol")
            String currencySymbol;
        I fixed the issue in the mapper by adding a custom result map, but it may not be the ideal solution.
        */

    int id;
    String currencySymbol;
    int publishedOn;
    String sentiment;
    double score;
    String direction;

    public CurrencySentiment(String currencySymbol, int publishedOn, String sentiment, double score) {
        this.currencySymbol = currencySymbol;
        this.publishedOn = publishedOn;
        this.sentiment = sentiment;
        this.score = score;
    }

    public CurrencySentiment() {}

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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
