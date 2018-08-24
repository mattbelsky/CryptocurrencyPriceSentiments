package CryptocurrencyPriceSentiments.models.sentiment_analysis;

import java.util.ArrayList;

public class DirectionResponseWrapper {

    String direction;
    ArrayList<PriceChangeDbEntity> priceChangeSummary;

    public DirectionResponseWrapper(String direction, ArrayList<PriceChangeDbEntity> priceChangeSummary) {
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

    public ArrayList<PriceChangeDbEntity> getPriceChangeSummary() {
        return priceChangeSummary;
    }

    public void setPriceChangeSummary(ArrayList<PriceChangeDbEntity> priceChangeSummary) {
        this.priceChangeSummary = priceChangeSummary;
    }
}
