package com.kakaopay.restapi.model.entity;

import com.kakaopay.restapi.model.entity.code.TrxSignature;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="users")
@Getter@Setter
@NoArgsConstructor
public class User implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable=false)
	private Long userNo;
	@Column(unique=true, updatable=false)
	private String userId;
	private String userName;
	private String password;
	private String email;

	@Column(nullable = false)
	private LocalDateTime regDttm = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime modDttm = LocalDateTime.now();

	@Column(nullable = false)
	private String regrId = TrxSignature.USER.getSigniture();

	@Column(nullable = false)
	private String modrId = TrxSignature.USER.getSigniture();

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="userNo", insertable=false, updatable=false)
	@MapKeyColumn(name="couponNo")
	@ToString.Exclude
	private Map<Long,UserCoupon> userCoupons;

}