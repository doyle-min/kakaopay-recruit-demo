package com.kakaopay.restapi.service;

import com.kakaopay.restapi.model.dto.AuthTokenResponse;
import com.kakaopay.restapi.model.dto.ResultResponse;
import com.kakaopay.restapi.model.dto.SignUpRequest;
import com.kakaopay.restapi.model.dto.SignUpResult;
import com.kakaopay.restapi.model.entity.User;
import com.kakaopay.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	private AuthTokenService tokenProvider;

	@Value("${auth.encrypt.strength}")
	private int encryptStrength;

	private BCryptPasswordEncoder encoder;

	public UserService(){
		this.encoder = new BCryptPasswordEncoder();
	}

	public ResultResponse signup(SignUpRequest request) {
		if(userRepository.findByUserId(request.getId()).isPresent())
			throw new IllegalArgumentException(messageService.getMessage("ACCOUNT.ID_ALREADY_EXISTS"));

		User user = signUpRequestToUserEntity(request);
		userRepository.save(user);
		SignUpResult result = new SignUpResult(user.getUserId(), user.getUserName(), user.getRegDttm());
		return new ResultResponse(result);
	}

	public ResultResponse signin(String userId, String password) {
		User findUser = getUserById(userId);

		if (!encoder.matches(password, findUser.getPassword())) {
			throw new IllegalArgumentException(messageService.getMessage("ACCOUNT.PASSWORD_IS_NOT_EQUAL"));
		}
		AuthTokenResponse response = tokenProvider.create(findUser);
		return new ResultResponse(response);
	}

	public User getUserById(String userId) {
		User findUser = userRepository.findByUserId(userId).orElseThrow(
				() -> new IllegalArgumentException(messageService.getMessage("ACCOUNT.USER_ID_IS_NOT_EXIST"))
		);
		return findUser;
	}

	public User getUserByUserNo(Long userNo) {
		User findUser = userRepository.findByUserNo(userNo).orElseThrow(
				() -> new IllegalArgumentException(messageService.getMessage("ACCOUNT.USER_NO_IS_NOT_EXIST"))
		);
		return findUser;
	}

	public User signUpRequestToUserEntity(SignUpRequest dto) {
		User user = new User();
		user.setUserId(dto.getId());
		user.setUserName(dto.getName());
		user.setPassword(encoder.encode(dto.getPassword()));
		user.setEmail(dto.getEmail());
		return user;
	}

}
