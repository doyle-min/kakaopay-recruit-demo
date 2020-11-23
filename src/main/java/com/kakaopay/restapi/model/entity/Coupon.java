package com.kakaopay.restapi.model.entity;

import com.kakaopay.restapi.model.entity.code.CouponType;
import com.kakaopay.restapi.model.entity.code.TrxSignature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="coupons", indexes = {@Index(columnList="expireDate"), @Index(columnList="discountAmt")})
@Getter@Setter
@NoArgsConstructor
public class Coupon {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable=false)
	private Long couponNo;

	@Column(nullable = true)
	private String couponName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private CouponType couponType = CouponType.RATIO;

	@Column(nullable = true)
	private Double discountAmt = 5.0d;

	@Column(nullable = false)
	@Setter
	private LocalDate expireDate = LocalDate.now().plusDays(7);

	@Column(nullable = false)
	private LocalDateTime regDttm = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime modDttm = LocalDateTime.now();

	@Column(nullable = false)
	private String regrId = TrxSignature.COUPON.getSigniture();

	@Column(nullable = false)
	private String modrId = TrxSignature.COUPON.getSigniture();

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="couponNo", insertable=false, updatable=false)
	@ToString.Exclude
	private List<UserCoupon> userCoupons = new ArrayList<>();

}
