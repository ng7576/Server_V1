package com.RestAPI.v1.Server.Utils.JWTUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JWTUtility implements Serializable {

    public static final int JWT_VALIDITY = 3600;


//    @Value("123456")
    private String key = "String1234";


    public String getEmailFromToken(String token ) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final  Date exp = getExpirationDate(token);
        return exp.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return tokenGenerator( claims, userDetails.getUsername());
    }

    private  String tokenGenerator(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Boolean tokenValidator(String token,  UserDetails userDetails) {
        final String userEmail = getEmailFromToken(token);
        return (userEmail.equals((userDetails.getUsername())) && !isTokenExpired(token));
    }


}
