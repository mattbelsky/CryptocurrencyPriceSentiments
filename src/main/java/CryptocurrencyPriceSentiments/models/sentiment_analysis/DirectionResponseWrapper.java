package CryptocurrencyPriceSentiments.models.sentiment_analysis;

import java.util.ArrayList;

public class DirectionResponseWrapper {

    String direction;
    ArrayList<PriceChangeSummary> priceChangeSummary;

    public DirectionResponseWrapper(String direction, ArrayList<PriceChangeSummary> priceChangeSummary) {
        this.direction = direction;
        this.priceChangeSummary = priceChangeSummary;
    }

    public DirectionResponseWrapper() {
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ArrayList<PriceChangeSummary> getPriceChangeSummary() {
        return priceChangeSummary;
    }

    public void setPriceChangeSummary(ArrayList<PriceChangeSummary> priceChangeSummary) {
        this.priceChangeSummary = priceChangeSummary;
    }
}
