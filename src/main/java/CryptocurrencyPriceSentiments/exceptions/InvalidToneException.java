package CryptocurrencyPriceSentiments.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidToneException extends Exception {

    HttpStatus httpStatus;
    String message;

    public InvalidToneException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
