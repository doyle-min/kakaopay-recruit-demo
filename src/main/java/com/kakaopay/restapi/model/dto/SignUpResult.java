package com.kakaopay.restapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SignUpResult {
	private String userId;
	private String userName;
	private LocalDateTime regstered;
}
