package com.kakaopay.restapi.controller;

import com.kakaopay.restapi.util.ResponseParseUtil;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	CouponController controller;

	@Test
	@DisplayName("랜덤 쿠폰 생성")
	void make() throws Exception {
		String token = joinLoginAndGetToken("makeTest");

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("quantity", "100");

		MvcResult result =
				mockMvc.perform(MockMvcRequestBuilders.post("/coupon/make")
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.params(paramMap))
						.andExpect(status().is(200))
						.andReturn();

	}

	@Test
	@DisplayName("인증 토큰 없이 랜덤 쿠폰 생성 시도 시 오류 확인 ")
	void makeWithOutToken() throws Exception {
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("quantity", "10");

		MvcResult result =
				mockMvc.perform(MockMvcRequestBuilders.post("/coupon/make")
						.accept(MediaType.APPLICATION_JSON)
						.params(paramMap))
						.andExpect(status().is(403))
						.andReturn();

	}

	@Test
	@DisplayName("오늘 만료되는 쿠폰 목록 조회")
	void expiresToday() throws Exception {
		String token = joinLoginAndGetToken("expiresToday");
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

		MvcResult result =
				mockMvc.perform(MockMvcRequestBuilders.get("/coupon/expiresToday")
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.params(paramMap))
						.andExpect(status().is(200))
						.andReturn();

	}


	@Test
	@DisplayName("3일 내 만료되는 쿠폰과 유저들 조회")
	void expires3DaysAfter() throws Exception {
		String token = joinLoginAndGetToken("expire3daysAfter");
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

		MvcResult result =
				mockMvc.perform(MockMvcRequestBuilders.get("/coupon/expires3DaysAfter")
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.params(paramMap))
						.andExpect(status().is(200))
						.andReturn();

	}

	@Test
	@DisplayName("CSV 파일 업로드")
	void upload() throws Exception {
		String token = joinLoginAndGetToken("upload");
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("csv_import_test.csv");
		MockMultipartFile firstFile = new MockMultipartFile("files",
				"csv_import_test.csv",
				"text/plain",
				is);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/coupon/upload")
				.file(firstFile)
				.file(firstFile)
				.file(firstFile)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", token))
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