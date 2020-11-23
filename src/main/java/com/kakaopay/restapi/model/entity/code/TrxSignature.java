package com.kakaopay.restapi.model.entity.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TrxSignature {

	API("API:API"),
	USER("API:USER"),
	COUPON("API:COUPON"),
	COUPON_BULK("API:COUPON_BULK");

	private String signiture;

}
