package com.kakaopay.restapi.controller;

import com.kakaopay.restapi.exception.ExceptionAdvisor;
import com.kakaopay.restapi.util.ResponseParseUtil;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserCouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	CouponController CouponController;
	@Autowired
	UserCouponController userCouponController;


	@Test
	void assign() throws Exception {

		// 로그인 및 토큰 발급
		String token = joinLoginAndGetToken("assign");

		// 랜덤 쿠폰 생성
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("quantity", "100");

		MvcResult result =
				mockMvc.perform(MockMvcRequestBuilders.post("/coupon/make")
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.params(paramMap))
						.andExpect(status().is(200))
						.andReturn();

		// 쿠폰 발급
		paramMap = new LinkedMultiValueMap<>();
		paramMap.add("couponNo", "50");
		mockMvc.perform(MockMvcRequestBuilders.get("/userCoupon/assign")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.params(paramMap))
				.andExpect(status().is(200))
				.andReturn();

		// 조회
		paramMap = new LinkedMultiValueMap<>();
		paramMap.add("couponNo", "50");

		mockMvc.perform(MockMvcRequestBuilders.get("/userCoupon/list")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.params(paramMap))
				.andExpect(status().is(200))
				.andReturn();

		// 사용
		paramMap = new LinkedMultiValueMap<>();
		paramMap.add("couponNo", "50");

		mockMvc.perform(MockMvcRequestBuilders.post("/userCoupon/use")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.params(paramMap))
				.andExpect(status().is(200))
				.andReturn();


		// 사용 취소
		paramMap = new LinkedMultiValueMap<>();
		paramMap.add("couponNo", "50");

		mockMvc.perform(MockMvcRequestBuilders.post("/userCoupon/unuse")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.params(paramMap))
				.andExpect(status().is(200))
				.andReturn();
	}

	public String joinLoginAndGetToken(String testId) throws Exception {
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		// 이미 만들어져있어도 새로 만들기 시도
		paramMap.add("id", testId);
		paramMap.add("name", "정상입니다");
		paramMap.add("password", "alpha123!@#");
		paramMap.add("email", "test@test.com");
		mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
				.accept(MediaType.APPLICATION_JSON)
				.params(paramMap));

		paramMap.clear();
		paramMap.add("id", testId);
		paramMap.add("password", "alpha123!@#");
		MvcResult result =
				mockMvc.perform(MockMvcRequestBuilders.post("/user/signin")
						.accept(MediaType.APPLICATION_JSON)
						.params(paramMap))
						.andExpect(status().is(200))
						.andReturn();

		JSONObject response = ResponseParseUtil.getDataAsJson(result);
		String token = (String) response.get("token");
		return token;
	}
}