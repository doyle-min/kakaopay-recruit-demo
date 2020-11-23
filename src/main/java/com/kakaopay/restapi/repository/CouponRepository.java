package com.kakaopay.restapi.repository;

import com.kakaopay.restapi.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
	List<Coupon> findByExpireDateBetween(LocalDate startDate, LocalDate endDate);
	List<Coupon> findByExpireDate(LocalDate expireDate);
}
