package ru.bft.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JwtUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JwtUtils.class);

    private static final String SECRET = "SGVsZW5hLkRhbmlzaC5TZWNyZXRLZXkuMjAwNjIwNC4yMDA0MTk5Mw";

    private static final long EXPIRATION = 600000L;

    public String getJwt(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        LOG.info("Authorization header " + token);
        if (token != null && token.startsWith("Bearer") && !token.equals("Bearer")) {
            //убираем префикс Bearer
            return token.substring(7);
        }
        return null;
    }

    //генерация токена
    public String generateTokenFromUserName(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder().
                subject(username).
                issuedAt(new Date()).
                expiration(new Date(new Date().getTime() + EXPIRATION)).
                signWith(key()).
                compact();
    }

    // получение пользователя из токена
    public String getUserNameFromJwtToken(String token) {

        Header header = Jwts.parser().
                verifyWith((SecretKey) key()).
                build().
                parseSignedClaims(token).getHeader();

        Date expiration = Jwts.parser().
                verifyWith((SecretKey) key()).
                build().
                parseSignedClaims(token).getPayload().getExpiration();

        Date dateAt = Jwts.parser().
                verifyWith((SecretKey) key()).
                build().
                parseSignedClaims(token).getPayload().getIssuedAt();

        String subject = Jwts.parser().
                verifyWith((SecretKey) key()).
                build().
                parseSignedClaims(token).
                getPayload().
                getSubject();

        LOG.info("token header " + header.toString());
        LOG.info("token creation " + dateAt);
        LOG.info("token expiration " + expiration);
        LOG.info("token subject " + subject);

        return subject;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    public boolean validate(String token) {
        try {
            LOG.info("validate");
            Jwts.parser().verifyWith((SecretKey) key()).
                    build().
                    parseSignedClaims(token).
                    getPayload().
                    getSubject();
            return true;
        } catch (MalformedJwtException e) {
            LOG.error("Invlalid token " + e.getMessage());
        } catch (ExpiredJwtException e) {
            LOG.error("Token is expired" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOG.error("Token is unsupported" + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOG.error("Jwt claims string is empty" + e.getMessage());
        }
        return false;
    }
}
