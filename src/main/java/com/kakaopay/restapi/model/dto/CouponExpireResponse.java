package com.kakaopay.restapi.model.dto;

import com.kakaopay.restapi.model.entity.Coupon;
import com.kakaopay.restapi.model.entity.User;
import com.kakaopay.restapi.model.entity.UserCoupon;
import com.kakaopay.restapi.model.entity.code.CouponType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponExpireResponse {
	private Long couponNo;
	private String couponName;
	private CouponType couponType;
	private Double discountAmt;
	private LocalDate expireDate;
	private String email;
	private String name;

	public CouponExpireResponse(UserCoupon userCoupon) {
		Coupon coupon = userCoupon.getCoupon();
		User user = userCoupon.getUser();
		this.couponNo = coupon.getCouponNo();
		this.couponName = coupon.getCouponName();
		this.couponType = coupon.getCouponType();
		this.discountAmt = coupon.getDiscountAmt();
		this.expireDate = coupon.getExpireDate();
		this.email = user.getUserName();
	}
}
