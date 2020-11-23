package com.kakaopay.restapi.service;

import com.kakaopay.restapi.model.dto.BulkInsertResponse;
import com.kakaopay.restapi.model.dto.CouponExpireResponse;
import com.kakaopay.restapi.model.dto.CouponResponse;
import com.kakaopay.restapi.model.dto.ResultResponse;
import com.kakaopay.restapi.model.entity.Coupon;
import com.kakaopay.restapi.model.entity.UserCoupon;
import com.kakaopay.restapi.model.entity.code.CouponType;
import com.kakaopay.restapi.model.entity.csvEntityAdapter.CsvCouponAdapter;
import com.kakaopay.restapi.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CouponService {

	@Autowired
	private CouponRepository couponRepository;

	private final CsvCouponAdapter couponAdapter = new CsvCouponAdapter();

	public ResultResponse randomMake(int quantity) {
		List<Coupon> couponList = new ArrayList<>();

		for (int i = 0; i < quantity; i++) {
			Coupon coupon = new Coupon();
			double discountRatio = Math.floor(Math.random()*20) + 10;
			StringBuilder nameBuilder = new StringBuilder("랜덤 발행 쿠폰 ").append(discountRatio).append("% 할인");
			coupon.setExpireDate(LocalDate.now().plusWeeks(1L));
			coupon.setCouponType(CouponType.AMOUNT);
			coupon.setDiscountAmt(discountRatio);	// 최대 30% random 쿠폰
			coupon.setCouponName(nameBuilder.toString());
			couponList.add(coupon);
		}

		couponRepository.saveAll(couponList);
		couponRepository.flush();
		List<CouponResponse> resultList = new ArrayList<>();
		couponList.forEach(coupon -> resultList.add(new CouponResponse(coupon)));
		return new ResultResponse(resultList);
	}

	public ResultResponse findByExpiresToday() {
		List<Coupon> couponList = couponRepository.findByExpireDate(LocalDate.now());
		return new ResultResponse(couponList.stream().map(coupon->new CouponResponse(coupon)).collect(Collectors.toList()));
	}

	public ResultResponse messageAboutExpiresBetween(LocalDate startDate, LocalDate endDate) {
		List<Coupon> couponList = couponRepository.findByExpireDateBetween(startDate, endDate);
		List<CouponExpireResponse> resultList = new ArrayList<>();
		couponList.stream().map(coupon->getCouponExpireResponseList(coupon)).forEach(list-> resultList.addAll(list));
		return new ResultResponse(resultList);
	}

	public List<CouponExpireResponse> getCouponExpireResponseList(Coupon coupon) {
		List<UserCoupon> userCoupons = coupon.getUserCoupons();
		List<CouponExpireResponse> results = new ArrayList<>();
		userCoupons.forEach(userCoupon -> {
			CouponExpireResponse response = new CouponExpireResponse(userCoupon);
			results.add(response);
			StringBuilder sb = new StringBuilder();
			sb.append("to : ").append(response.getEmail()).append("\n")
					.append("message : 쿠폰이 곧 만료됩니다.").append("\n")
					.append("만료일 : ").append(response.getExpireDate().toString())
					.append("쿠폰번호 : ").append(response.getCouponNo())
					.append("쿠폰이름 : ").append(response.getCouponName());
		});
		return results;
	}

	public ResultResponse uploadCoupons(List<MultipartFile> files) throws IOException {
		int rowCount = 0;
		int partCount = 0;
		long start = System.currentTimeMillis();
		for (MultipartFile file : files) {
			List<Coupon> couponList = couponAdapter.parseList(file);
			log.debug("part : " + (++partCount) + "/ size : " + couponList.size());
			rowCount+= couponList.size();
			couponRepository.saveAll(couponList);
			couponRepository.flush();
		}
		long estimatedTimeMillies = (System.currentTimeMillis() - start);
		log.debug("rowCount : " + rowCount);
		log.debug("ms : " + estimatedTimeMillies);
		return new ResultResponse(new BulkInsertResponse(rowCount, estimatedTimeMillies));
	}

}
