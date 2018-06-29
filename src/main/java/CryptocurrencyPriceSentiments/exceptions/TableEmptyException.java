package CryptocurrencyPriceSentiments.exceptions;

import org.springframework.http.HttpStatus;

public class TableEmptyException extends Exception {

    HttpStatus httpStatus;
    String message;

    public TableEmptyException(HttpStatus httpStatus, String message) {
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
