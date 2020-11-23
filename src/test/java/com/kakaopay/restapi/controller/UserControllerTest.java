package com.kakaopay.restapi.controller;

import com.kakaopay.restapi.exception.ExceptionAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	private MockMvc mockMvc;

	@Autowired
	UserController userController;

	@BeforeEach
	void init(){
		mockMvc = MockMvcBuilders.standaloneSetup(userController)
				.setControllerAdvice(new ExceptionAdvisor())
				.build();
	}

	@Test
	@DisplayName("회원가입 : 비밀번호, email Validation")
	void signupInvalid() throws Exception {

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("id", "signupInvalid");
		paramMap.add("name", "짧은비밀번호테스트");
		paramMap.add("password", "short");
		paramMap.add("email", "testest.com");

		mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
				.accept(MediaType.APPLICATION_JSON)
				.params(paramMap))
				.andExpect(status().is(400))
				.andDo(print());

	}

	@Test
	@DisplayName("회원가입 : 정상 가입 및 로그인")
	void signupValid() throws Exception {

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

		paramMap.add("id", "signupValid");
		paramMap.add("name", "정상입니다");
		paramMap.add("password", "alpha123!@#");
		paramMap.add("email", "test@test.com");

		mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
				.accept(MediaType.APPLICATION_JSON)
				.params(paramMap))
				.andExpect(status().is(200))
				.andDo(print());


		paramMap.clear();
		paramMap.add("id", "test2");
		paramMap.add("password", "alpha123!@#");

		mockMvc.perform(MockMvcRequestBuilders.post("/user/signin")
				.accept(MediaType.APPLICATION_JSON)
				.params(paramMap))
				.andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("로그인 : 비번 미입력")
	void signinNoPassword() throws Exception {

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("id", "signinNoPassword");

		mockMvc.perform(MockMvcRequestBuilders.post("/user/signin")
				.accept(MediaType.APPLICATION_JSON)
				.params(paramMap))
				.andExpect(status().is(500))
				.andDo(print());

	}

	@Test
	@DisplayName("로그인 : 없는 id")
	void signinIdNotExists() throws Exception {

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("id", "signinIdNotExists");
		paramMap.add("password", "alpha123!@#");

		mockMvc.perform(MockMvcRequestBuilders.post("/user/signin")
				.accept(MediaType.APPLICATION_JSON)
				.params(paramMap))
				.andExpect(status().is(500))
				.andDo(print());

	}
}