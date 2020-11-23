package com.kakaopay.restapi.repository;

import com.kakaopay.restapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserId(String userId);
	Optional<User> findByUserNo(Long userNo);
}
