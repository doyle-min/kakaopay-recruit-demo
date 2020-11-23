package com.kakaopay.restapi.model.entity;

import com.kakaopay.restapi.model.entity.code.CouponUseStatus;
import com.kakaopay.restapi.model.entity.code.TrxSignature;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="user_coupons")
@IdClass(UserCouponId.class)
@NoArgsConstructor
@Getter @Setter
public class UserCoupon {

	@Id
	@Column(updatable=false)
	private Long userNo;
	@Id
	@Column(updatable=false)
	private Long couponNo;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private CouponUseStatus status;

	@Column(nullable = false)
	private LocalDateTime regDttm = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime modDttm = LocalDateTime.now();

	@Column(nullable = false)
	private String regrId = TrxSignature.USER_COUPON.getSigniture();

	@Column(nullable = false)
	private String modrId = TrxSignature.USER_COUPON.getSigniture();

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="couponNo", insertable=false, updatable=false)
	private Coupon coupon;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userNo", insertable=false, updatable=false)
	private User user;

	@Override
	public String toString(){
		return new StringBuilder(userNo.toString()).append("-").append(couponNo.toString()).toString();
	}

}
