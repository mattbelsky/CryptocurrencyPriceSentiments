package crypto_compare_exercise.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceHistorical {

    @JsonProperty("Response")
    String response;
    @JsonProperty("Type")
    int type;
    @JsonProperty("Aggregated")
    boolean aggregated;
    @JsonProperty("Data")
    Data[] data;
    @JsonProperty("TimeTo")
    int timeTo;
    @JsonProperty("TimeFrom")
    int timeFrom;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAggregated() {
        return aggregated;
    }

    public void setAggregated(boolean aggregated) {
        this.aggregated = aggregated;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }
}
