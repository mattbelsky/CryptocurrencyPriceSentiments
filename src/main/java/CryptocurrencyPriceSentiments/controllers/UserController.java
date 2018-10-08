package CryptocurrencyPriceSentiments.controllers;

import CryptocurrencyPriceSentiments.exceptions.InvalidUserException;
import CryptocurrencyPriceSentiments.models.GeneralResponse;
import CryptocurrencyPriceSentiments.models.User;
import CryptocurrencyPriceSentiments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static CryptocurrencyPriceSentiments.security.SecurityConstants.SIGNUP_URL;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(SIGNUP_URL)
    public GeneralResponse getApiKey(@RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
                                     HttpServletRequest httpServletRequest) {

        String ipAddress = httpServletRequest.getRemoteAddr();
        String apiKey = userService.generateAPIKey();

        // This array list will eventually be removed and the return type will be void. It just returns a response for
        // development purposes.
        ArrayList<String> securityData = new ArrayList<>();
        securityData.add(ipAddress);
        securityData.add(userAgent);
        securityData.add(apiKey);

        userService.addUser(apiKey, ipAddress, userAgent);

        return new GeneralResponse(HttpStatus.OK, "API key generated.", securityData);
    }

    @GetMapping("/authenticate")
    public GeneralResponse authenticateUser(@RequestParam("key") String apiKey) throws InvalidUserException {
        User user = userService.validateUser(apiKey);
        return new GeneralResponse(HttpStatus.OK, "User exists.", user);
    }

    @GetMapping("/yourkey")
    public GeneralResponse yourKey(@RequestParam("key") String key,
                                   HttpServletRequest request) {
        return new GeneralResponse(HttpStatus.OK, "OK", request.getParameter("key"));
    }
}
