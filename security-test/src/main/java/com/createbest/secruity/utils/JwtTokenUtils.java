package com.createbest.secruity.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtils {


    @Value("${token.secret}")
    private  String secret ;

    @Value("${token.expiration}")
    public  Long expiration;

	
	public  String generateToken(String data) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", data);
        claims.put("created", generateCurrentDate());
        return generateToken(claims);
    }

	private  String generateToken(Map<String, Object> claims) 
	{
		System.out.println("secret = " + secret);
    
		try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            //didn't want to have this method throw the exception, would rather log it and sign the token like it was before
           
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }
    }

    /**
     * token 过期时间
     *
     * @return
     */
    private   Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    private  Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    
    /**
     * 从 token 中拿到 username
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 解析 token 的主体 Claims
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

	public boolean validateToken(String authToken, UserDetails userDetails) {
		// TODO Auto-generated method stub
		String username = userDetails.getUsername();
		if(username.equals(getUsernameFromToken(authToken)))
			return true;
		
		return false;
	}





}
