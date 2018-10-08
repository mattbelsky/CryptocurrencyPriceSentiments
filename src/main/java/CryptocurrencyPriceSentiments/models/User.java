package CryptocurrencyPriceSentiments.models;

public class User {

    int id;
    String apiKey;
    String ipAddress;
    String userAgent;

    public User(String apiKey, String ipAddress, String userAgent) {
        this.apiKey = apiKey;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public User(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
