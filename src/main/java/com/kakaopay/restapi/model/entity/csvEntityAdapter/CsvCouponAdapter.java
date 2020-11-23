package com.kakaopay.restapi.model.entity.csvEntityAdapter;

import com.kakaopay.restapi.model.entity.code.CouponType;
import com.kakaopay.restapi.model.entity.code.TrxSignature;
import com.kakaopay.restapi.model.entity.Coupon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvCouponAdapter extends CsvEntityAdapter<Coupon> {

	@Override
	public Coupon mapRow(String row) {
		String split[] = row.split(",");

		Coupon coupon = new Coupon();
		if (split.length > 0) {
			String couponName = split.length>0?split[0]:null;
			coupon.setCouponName(couponName);
		}
		if(split.length>1){
			String expireDateStr = split.length>1?split[1]:null;
			LocalDate expireDate = LocalDate.parse(expireDateStr, DateTimeFormatter.ISO_DATE);
			coupon.setExpireDate(expireDate);
		}
		CouponType type = null;
		if (split.length > 3) {
			String couponType = split.length>2?split[2]:null;
			type = CouponType.valueOf(couponType);
			coupon.setCouponType(type);
		}
		Integer value = null;
		if (split.length > 3) {
			String discountValue = split.length>3?split[3]:null;
			value = Integer.parseInt(discountValue);
			coupon.setDiscountAmt((double)value);
		}

		coupon.setRegrId(TrxSignature.COUPON_BULK.getSigniture());
		coupon.setModrId(TrxSignature.COUPON_BULK.getSigniture());
		return coupon;
	}
}
