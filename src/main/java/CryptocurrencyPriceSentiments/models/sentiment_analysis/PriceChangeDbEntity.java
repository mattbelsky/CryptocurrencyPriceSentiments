package CryptocurrencyPriceSentiments.models.sentiment_analysis;

public class PriceChangeDbEntity {

    String inCurrencyName;
    String inToneDirection;
    String outCurrencyName;
    String outToneDirection;
    double outProportionSuccess;

    public PriceChangeDbEntity(String inCurrencyName, String inToneDirection) {
        this.inCurrencyName = inCurrencyName;
        this.inToneDirection = inToneDirection;
    }

    public PriceChangeDbEntity() {
    }

    public String getInCurrencyName() {
        return inCurrencyName;
    }

    public void setInCurrencyName(String inCurrencyName) {
        this.inCurrencyName = inCurrencyName;
    }

    public String getInToneDirection() {
        return inToneDirection;
    }

    public void setInToneDirection(String inToneDirection) {
        this.inToneDirection = inToneDirection;
    }

    public String getOutCurrencyName() {
        return outCurrencyName;
    }

    public void setOutCurrencyName(String outCurrencyName) {
        this.outCurrencyName = outCurrencyName;
    }

    public String getOutToneDirection() {
        return outToneDirection;
    }

    public void setOutToneDirection(String outToneDirection) {
        this.outToneDirection = outToneDirection;
    }

    public double getOutProportionSuccess() {
        return outProportionSuccess;
    }

    public void setOutProportionSuccess(double outProportionSuccess) {
        this.outProportionSuccess = outProportionSuccess;
    }
}
