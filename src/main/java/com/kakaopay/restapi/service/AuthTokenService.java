package com.kakaopay.restapi.service;

import com.kakaopay.restapi.model.entity.User;
import com.kakaopay.restapi.model.dto.AuthTokenResponse;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthTokenService {

	@Value("${auth.jwt.secretKey}")
	private String secretKey;

	@Value("${auth.jwt.expireTimeSeconds}")
	private int expireTimeSeconds;


	public AuthTokenResponse create(User user){
		Date expireDate = DateUtils.addSeconds(new Date(), expireTimeSeconds);
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256")
				.setHeaderParam("regDate", System.currentTimeMillis())
				.setAudience(user.getUserId())
				.setExpiration(expireDate)
				.claim("authGroup", "default")
				.setSubject(user.getUserNo().toString());

		String token = builder.signWith(SignatureAlgorithm.HS256, secretKey).compact();

		AuthTokenResponse loginResponse = new AuthTokenResponse(token, expireDate);

		return loginResponse;
	}

	public boolean verifyToken(String token) throws RuntimeException {
		try{
			Jwts.parser().setSigningKey(secretKey).parse(token);
		} catch (Exception e) {

		}
		return true;
	}

	public Long getUserNoFromToken(String token) throws NumberFormatException {
		Jws<Claims> jws = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token);

		Long userNo = Long.parseLong(jws.getBody().getSubject());
		return userNo;
	}

	public String getUserIdFromToken(String token) throws NumberFormatException {
		Jws<Claims> jws = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token);

		String userId = jws.getBody().getAudience();
		return userId;
	}
}
