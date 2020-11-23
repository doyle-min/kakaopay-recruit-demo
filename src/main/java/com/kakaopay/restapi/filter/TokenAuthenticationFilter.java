package com.kakaopay.restapi.filter;

import com.kakaopay.restapi.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

	@Autowired
	private AuthTokenService tokenProvider;

	public TokenAuthenticationFilter(AuthenticationManager authenticationManager, AuthTokenService tokenProvider) {
		super(authenticationManager);
		this.tokenProvider = tokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = getJwtFormRequest(request);
			if (StringUtils.hasText(token) && tokenProvider.verifyToken(token)) {
				Long userNo = tokenProvider.getUserNoFromToken(token);
				tokenProvider.getUserNoFromToken(token);

				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userNo, null, null);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception e) {

		}

		filterChain.doFilter(request, response);
	}

	public String getJwtFormRequest(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
}
