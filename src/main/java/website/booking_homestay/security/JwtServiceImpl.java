package website.booking_homestay.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import website.booking_homestay.exception.BaseException;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

//    @Value("${jwtSercet}")
    public static String jwtSecret = "yLaoAc0BMzK9tvO+wnCueAJl2v/UcPFejN4ltGQZbs87";
//    @Value("${jwtExpirationMs}")
//    public static int jwtExpirationMs = 3600000;//one hour
    public static int jwtExpirationMs = 604800000;//one day
    //    @Value("${jwtExpirationMsRefresh}")
    public static int jwtExpirationMsRefresh = 604800000;// 7 days

//    @Override
    public String generateJwtToken(String username, Date timeExpiration) {
//        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(timeExpiration)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

//    @Override
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    @Override
    public String getUserNameFromJwtToken(String token) {
        return extractClaim(token,Claims::getSubject);
    }

//    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT token: {}",e.getMessage());
//            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"Invalid format 3 part of token!");
            throw new BaseException(HttpStatus.UNAUTHORIZED,"Invalid format 3 part of token!");
        }catch (ExpiredJwtException e){
            logger.error("token expiration: {}",e.getMessage());
//            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"Token expiration!");
            throw new BaseException(HttpStatus.UNAUTHORIZED,"Token expiration!");
        }catch (UnsupportedJwtException e){
            logger.error("Token type is unsupported: {}",e.getMessage());
//            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"Token's not supported!");
            throw new BaseException(HttpStatus.UNAUTHORIZED,"Token's not supported!");
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}",e.getMessage());
//            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"JWT claims string is empty!");
            throw new BaseException(HttpStatus.UNAUTHORIZED,"JWT claims string is empty!");
        }catch (SignatureException e){
            logger.error("Invalid format token: {}",e.getMessage());
//            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"Invalid format token!");
            throw new BaseException(HttpStatus.UNAUTHORIZED,"Invalid format token!");
        }

    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
