package CryptocurrencyPriceSentiments.security;

import com.auth0.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static CryptocurrencyPriceSentiments.security.SecurityConstants.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super("/");
        this.authenticationManager = authenticationManager;
    }

    /**
     * Overridden method from the parent class that authenticates the API key the user entered in the URL as a request
     * parameter. Referencing the following usage:
     * https://www.programcreek.com/java-api-examples/?code=RBGKew/eMonocot/eMonocot-master/emonocot-portal/src/main/java/org/emonocot/portal/auth/RestAPIKeyAuthenticationFilter.java#
     * @param req -- the request object
     * @param res -- the response object
     * @return an Authentication object to be passed to the successfulAuthentication method -- contains the API key as
     * well as an empty credentials value
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {

        // Gets the API key from the request parameter in the URL named "key".
        String apiKey = req.getParameter("key");
        RestAuthenticationToken authRequest = createRequest(apiKey, null);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Creates a JSON web token if the API key has been successfully authenticated and adds it to the HTTP servlet
     * response object's header.
     * @param req -- the request object
     * @param res -- the response object
     * @param chain -- the Spring Security filter chain
     * @param auth -- the Authentication object created by the attemptAuthentication method, which contains the API key
     */
    @Override
    public void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                         Authentication auth) {

        // Creates the JWT
        String token = JWT.create()
                // Must cast the result of the getPrincipal method to the desired type, as the Authentication interface
                // declares it as an Object.
                .withSubject((String) auth.getPrincipal())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        // Adds the JWT to the header of the response object.
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

    /**
     * Creates an authentication token object with the API key as the principal and a blank credentials object to be
     * fed to the AuthenticationManager.authenticate() method.
     * @param apiKey -- the principal
     * @param credentials -- the credentials object
     * @return a custom implementation of the AbstractAuthenticationToken class that the authentication manager can use
     */
    public RestAuthenticationToken createRequest(String apiKey, RestCredentials credentials) {
        return new RestAuthenticationToken(apiKey, credentials);
    }
}
