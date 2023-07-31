package website.booking_homestay.service;

import org.springframework.security.core.Authentication;

import java.security.Key;

public interface IJwtService {
    public String generateJwtToken(Authentication authentication, int timeExpiration);
    public Key key();
    public String getUserNameFromJwtToken(String token);
    public boolean validateJwtToken(String authToken);

    int getJwtExpirationMs();
    int getJwtExpirationMsRefresh();
}
