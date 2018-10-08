package CryptocurrencyPriceSentiments.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RestAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;

    public RestAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public RestAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
