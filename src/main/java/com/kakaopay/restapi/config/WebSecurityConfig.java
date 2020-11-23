package com.kakaopay.restapi.config;

import com.kakaopay.restapi.filter.TokenAuthenticationFilter;
import com.kakaopay.restapi.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthTokenService tokenProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.csrf().disable();

		http.authorizeRequests()
				// h2-console 관련
				.antMatchers(
						"/user/**"
				).permitAll()
				.anyRequest().authenticated();

		http.addFilterBefore(tokenAuthenticationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);
	}

	private TokenAuthenticationFilter tokenAuthenticationFilter(AuthenticationManager authenticationManagerBean){
		return new TokenAuthenticationFilter(authenticationManagerBean, tokenProvider);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}