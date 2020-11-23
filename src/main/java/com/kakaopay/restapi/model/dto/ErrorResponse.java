package com.kakaopay.restapi.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponse implements Serializable {
	private int status;
	private String error;
	private Object errors;
	private String message;
	private long timestamp;

	public ErrorResponse(String message, String error, Object errors, int status) {
		this.message = message;
		this.error = error;
		this.errors = errors;
		this.status = status;
		this.timestamp = System.currentTimeMillis();
	}
}