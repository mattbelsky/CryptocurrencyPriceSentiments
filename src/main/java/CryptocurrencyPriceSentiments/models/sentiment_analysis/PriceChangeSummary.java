package CryptocurrencyPriceSentiments.models.sentiment_analysis;

public class PriceChangeSummary {

    String currency;
    String direction;
    double proportion;

    public PriceChangeSummary(String currency, String direction, double proportion) {
        this.currency = currency;
        this.direction = direction;
        this.proportion = proportion;
    }

    public PriceChangeSummary() {}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }
}
