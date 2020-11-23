package com.kakaopay.restapi.model.dto;

import com.kakaopay.restapi.model.entity.Coupon;
import com.kakaopay.restapi.model.entity.code.CouponType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponResponse {
	private Long couponNo;
	private String couponName;
	private CouponType couponType;
	private Double discountAmt;
	private LocalDate expireDate;

	public CouponResponse(Coupon coupon) {
		this.couponNo = coupon.getCouponNo();
		this.couponName = coupon.getCouponName();
		this.couponType = coupon.getCouponType();
		this.discountAmt = coupon.getDiscountAmt();
		this.expireDate = coupon.getExpireDate();
	}
}
