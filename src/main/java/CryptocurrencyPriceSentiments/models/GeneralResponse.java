package CryptocurrencyPriceSentiments.models;

import org.springframework.http.HttpStatus;

public class GeneralResponse {

    HttpStatus httpStatus;
    String message;
    Object obj;

    public GeneralResponse(HttpStatus httpStatus, String message, Object obj) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.obj = obj;
    }

    public GeneralResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Object getObj() {
        return obj;
    }
}
