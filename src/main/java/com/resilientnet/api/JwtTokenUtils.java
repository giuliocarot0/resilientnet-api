package com.resilientnet.api;

import com.resilientnet.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtils implements Serializable{
    private static final long serialVersionUID = -2550185165626007488L ;
    public static final long JSON_TOKEN_VALIDITY = 5*60*60*60; //set token expiration in 1 hours

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token){
        return getClaimFromToken(token, Claims::getIssuedAt);
    }
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    private boolean isTokenExpired(String token){
        final Date exp = getExpirationDateFromToken(token);
        return exp.before(new Date());
    }
    private Boolean ignoreTokenExpiration(String token){
        //by default all token have to be unexpired
        //TODO: implement database table to store token which expiration has to be ignored
        return false;
    }
    public String generateToken(User user){
        Map<String, Object> claims  = new HashMap<>();
        return doGenerateToken(claims, user.getSubject());
    }
    private String doGenerateToken(Map <String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JSON_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public Boolean canTokenBeRefreshed(String token){
        return(!isTokenExpired(token) || ignoreTokenExpiration(token));
    }
    public Boolean validateToken(String token,String subject){
        final String username = getUsernameFromToken(token);
        return (username.equals(subject) && !isTokenExpired(token));
    }
}
