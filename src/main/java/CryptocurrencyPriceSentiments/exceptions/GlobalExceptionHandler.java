package CryptocurrencyPriceSentiments.exceptions;

import CryptocurrencyPriceSentiments.models.GeneralResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = TableEmptyException.class)
    protected @ResponseBody GeneralResponse tableEmptyError(TableEmptyException ex) {
        return new GeneralResponse(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(value = InvalidToneException.class)
    protected @ResponseBody GeneralResponse invalidToneException(InvalidToneException ex) {
        return new GeneralResponse(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(value = InvalidDirectionException.class)
    protected @ResponseBody GeneralResponse invalidDirectionException(InvalidDirectionException ex) {
        return new GeneralResponse(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(value = InvalidUserException.class)
    protected @ResponseBody GeneralResponse invalidUserException(InvalidUserException ex) {
        return new GeneralResponse(ex.getHttpStatus(), ex.getMessage());
    }
}
