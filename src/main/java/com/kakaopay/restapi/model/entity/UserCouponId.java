package com.kakaopay.restapi.model.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserCouponId implements Serializable {
	private Long userNo;
	private Long couponNo;
}