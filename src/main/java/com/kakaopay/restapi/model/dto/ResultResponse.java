package com.kakaopay.restapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResultResponse {
	private int status = 200;
	private String message = "success";
	private final Object data;
}
