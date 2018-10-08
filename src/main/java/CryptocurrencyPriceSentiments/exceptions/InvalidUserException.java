package CryptocurrencyPriceSentiments.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidUserException extends Exception {

    HttpStatus httpStatus;
    String message;

    public InvalidUserException(HttpStatus httpStatus, String message) {
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
