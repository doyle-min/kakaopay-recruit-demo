package com.kakaopay.restapi.controller;

import com.kakaopay.restapi.model.dto.ResultResponse;
import com.kakaopay.restapi.model.dto.SignUpRequest;
import com.kakaopay.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@ResponseBody
public class UserController {

	@Autowired
	private UserService userService;


	@PostMapping(value = "/user/signup")
	public ResponseEntity<ResultResponse> signup(@Valid SignUpRequest request) {
		return ResponseEntity.ok(userService.signup(request));
	}


	@PostMapping(value = "/user/signin")
	public ResponseEntity<ResultResponse> signin(@RequestParam(value = "id", required = true) String id,
												@RequestParam(value = "password", required = true) String password) throws Exception {
		return ResponseEntity.ok(userService.signin(id, password));
	}

}
