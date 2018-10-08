package CryptocurrencyPriceSentiments.services;

import CryptocurrencyPriceSentiments.exceptions.InvalidUserException;
import CryptocurrencyPriceSentiments.mappers.UserMapper;
import CryptocurrencyPriceSentiments.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Collections.emptyList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    /**
     * Generates an API key for the user.
     * @return the API key
     */
    public String generateAPIKey() {
        String apiKey = UUID.randomUUID().toString();
        apiKey = apiKey.replaceAll("-", "");
        return apiKey;
    }

    /**
     * Adds a new user to the database.
     * @param apiKey -- the generated API key
     * @param ipAddress -- the user's IP address
     * @param userAgent -- the user's browser information
     */
    public void addUser(String apiKey, String ipAddress, String userAgent) {
        userMapper.addUser(new User(apiKey, ipAddress, userAgent));
    }

    public User validateUser(String apiKey) throws InvalidUserException {

        User user;

        // If the API key cannot be found in the database, this catches the NullPointerException and throws a custom
        // exception.
        try {
            user = userMapper.validateUser(apiKey);
        } catch (NullPointerException e) {
            throw new InvalidUserException(HttpStatus.NO_CONTENT, "User does not exist.");
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String apiKey) {

        return new org.springframework.security.core.userdetails.User(
                userMapper.validateUser(apiKey).getApiKey(), null, emptyList()
        );
    }
}
