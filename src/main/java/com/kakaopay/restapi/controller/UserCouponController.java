package com.kakaopay.restapi.controller;

import com.kakaopay.restapi.model.dto.ResultResponse;
import com.kakaopay.restapi.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserCouponController {

	@Autowired
	private UserCouponService userCouponService;

	@GetMapping(value = "/userCoupon/assign")
	public ResponseEntity<ResultResponse> assign(@RequestParam(value = "couponNo", required = true) Long couponNo) throws Exception {
		Long userNo = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(userCouponService.assign(userNo, couponNo));
	}

	@ResponseBody
	@GetMapping(value = "/userCoupon/list")
	public ResponseEntity<ResultResponse> list() throws Exception {

		Long userNo = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(userCouponService.getUserCoupons(userNo));
	}

	@PostMapping(value = "/userCoupon/use")
	public ResponseEntity<ResultResponse> useUserCoupon(@RequestParam(value = "couponNo", required = true) Long couponNo) throws Exception {

		Long userNo = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(userCouponService.useCoupon(userNo, couponNo));
	}

	@PostMapping(value = "/userCoupon/unuse")
	public ResponseEntity<ResultResponse> unuseUserCoupon(@RequestParam(value = "couponNo", required = true) Long couponNo) throws Exception {

		Long userNo = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(userCouponService.reactivateCoupon(userNo, couponNo));
	}
}
