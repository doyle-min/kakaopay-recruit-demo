package com.kakaopay.restapi.model.dto;

import com.kakaopay.restapi.model.entity.Coupon;
import com.kakaopay.restapi.model.entity.UserCoupon;
import com.kakaopay.restapi.model.entity.code.CouponType;
import com.kakaopay.restapi.model.entity.code.CouponUseStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCouponResponse {
	private Long couponNo;
	private CouponUseStatus status;
	private String couponName;
	private CouponType couponType;
	private Double discountAmt;
	private LocalDate expireDate;

	public UserCouponResponse(UserCoupon userCoupon) {
		Coupon coupon = userCoupon.getCoupon();
		this.couponNo = userCoupon.getCouponNo();
		this.status = userCoupon.getStatus();
		this.couponName = coupon.getCouponName();
		this.couponType = coupon.getCouponType();
		this.discountAmt = coupon.getDiscountAmt();
		this.expireDate = coupon.getExpireDate();
	}
}
