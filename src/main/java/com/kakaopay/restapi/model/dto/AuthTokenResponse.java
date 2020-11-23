package com.kakaopay.restapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class AuthTokenResponse implements Serializable {
	private String token;
	private Date tokenExpires;
}
