package com.kakaopay.restapi.controller;

import com.kakaopay.restapi.model.dto.ResultResponse;
import com.kakaopay.restapi.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@ResponseBody
public class CouponController {

	@Autowired
	private CouponService couponService;

	@PostMapping(value = "/coupon/make")
	public ResponseEntity<ResultResponse> make(@RequestParam(value = "quantity", required = true) int quantity) {

		return ResponseEntity.ok(couponService.randomMake(quantity));
	}

	@GetMapping(value = "/coupon/expiresToday")
	public ResponseEntity<ResultResponse> expiresToday() {
		return ResponseEntity.ok(couponService.findByExpiresToday());
	}

	@GetMapping(value = "/coupon/expires3DaysAfter")
	public ResponseEntity<ResultResponse> expires3DaysAfter() {
		return ResponseEntity.ok(couponService.messageAboutExpiresBetween(LocalDate.now(), LocalDate.now().plusDays(3)));
	}

	@PostMapping("/coupon/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResultResponse> upload(@RequestPart List<MultipartFile> files) throws Exception {
		return ResponseEntity.ok(couponService.uploadCoupons(files));
	}
}
