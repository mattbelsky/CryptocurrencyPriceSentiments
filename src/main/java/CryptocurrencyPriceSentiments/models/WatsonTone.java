package CryptocurrencyPriceSentiments.models;

public class WatsonTone {

    private int id;
    private String tone;
    private String direction;

    public int getId() {
        return id;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
