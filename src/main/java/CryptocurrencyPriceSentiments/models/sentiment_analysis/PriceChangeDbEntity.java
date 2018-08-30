package CryptocurrencyPriceSentiments.models.sentiment_analysis;

public class PriceChangeDbEntity {

    String inCurrencyName;
    String inToneDirection;
    String outCurrencyName;
    String outToneDirection;
    Double outProportionSuccess;

    public PriceChangeDbEntity(String inCurrencyName, String inToneDirection, String outCurrencyName, String outToneDirection, Double outProportionSuccess) {
        this.inCurrencyName = inCurrencyName;
        this.inToneDirection = inToneDirection;
        this.outCurrencyName = outCurrencyName;
        this.outToneDirection = outToneDirection;
        this.outProportionSuccess = outProportionSuccess;
    }

    public PriceChangeDbEntity(String inCurrencyName, String inToneDirection) {
        this.inCurrencyName = inCurrencyName;
        this.inToneDirection = inToneDirection;
        this.outCurrencyName = null;
        this.outToneDirection = null;
        this.outProportionSuccess = null;
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

    public Double getOutProportionSuccess() {
        return outProportionSuccess;
    }

    public void setOutProportionSuccess(Double outProportionSuccess) {
        this.outProportionSuccess = outProportionSuccess;
    }
}
