package com.kakaopay.restapi.service;

import com.kakaopay.restapi.model.dto.UserCouponResponse;
import com.kakaopay.restapi.model.entity.code.CouponUseStatus;
import com.kakaopay.restapi.model.entity.Coupon;
import com.kakaopay.restapi.model.entity.User;
import com.kakaopay.restapi.model.entity.UserCoupon;
import com.kakaopay.restapi.model.dto.ResultResponse;
import com.kakaopay.restapi.repository.CouponRepository;
import com.kakaopay.restapi.repository.UserCouponRepository;
import com.kakaopay.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserCouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserCouponRepository userCouponRepository;

	public ResultResponse assign(Long userNo, Long couponNo) {
		// validation
		User user = getUserByUserNo(userNo);
		if(user==null) throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));
		Optional<Coupon> findCoupon = couponRepository.findById(couponNo);
		if(!findCoupon.isPresent())	throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));

		// set
		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setUserNo(user.getUserNo());
		userCoupon.setCouponNo(couponNo);
		userCoupon.setStatus(CouponUseStatus.UNUSED);
		Coupon coupon = findCoupon.get();
		userCoupon.setCoupon(coupon);

		user.getUserCoupons().put(userCoupon.getCouponNo(), userCoupon);
		// upsert
		userCouponRepository.save(userCoupon);
		return new ResultResponse(new UserCouponResponse(userCoupon));
	}

	public ResultResponse getUserCoupons(Long userNo) {
		List<UserCoupon> userCoupons = new ArrayList<>(getUserByUserNo(userNo).getUserCoupons().values());
		return new ResultResponse(userCoupons.stream().map(userCoupon->new UserCouponResponse(userCoupon)).collect(Collectors.toList()));
	}

	public ResultResponse useCoupon(Long userNo, Long couponNo) {
		// validation
		User user = getUserByUserNo(userNo);
		if(user==null) throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));

		UserCoupon userCoupon = user.getUserCoupons().get(couponNo);
		if(userCoupon==null) throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));

		Coupon coupon = userCoupon.getCoupon();
		if(coupon.getExpireDate().isBefore(LocalDate.now()))	throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));

		userCoupon.setCoupon(coupon);
		userCoupon.setStatus(CouponUseStatus.USED);
		userCouponRepository.save(userCoupon);

		return new ResultResponse(new UserCouponResponse(userCoupon));
	}

	public ResultResponse reactivateCoupon(Long userNo, Long couponNo) {
		// validation
		User user = getUserByUserNo(userNo);
		if(user==null) throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));

		UserCoupon userCoupon = user.getUserCoupons().get(couponNo);
		if(userCoupon==null) throw new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"));

		userCoupon.setStatus(CouponUseStatus.UNUSED);
		userCouponRepository.save(userCoupon);

		return new ResultResponse(new UserCouponResponse(userCoupon));
	}

	public User getUserByUserNo(Long userNo) {
		User findUser = userRepository.findByUserNo(userNo).orElseThrow(
				() -> new IllegalArgumentException(messageService.getMessage("VALIDATION.NO_SUCH_DATA_EXISTS"))
		);
		return findUser;
	}
}
