package com.kakaopay.restapi.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ToString
@Setter
@Getter
public class SignUpRequest implements Serializable {

	@NotBlank(message = "{ACCOUNT.ID_NOT_BLANK}")
	private String id;

	@NotBlank(message = "{ACCOUNT.PASSWORD_NOT_BLANK}")
	@Pattern(
			regexp = "(?=.*\\d{1,20})(?=.*[~`!@#$%\\^&*()-+=]{1,20})(?=.*[a-zA-Z]{2,20}).{8,20}$",
			message = "{ACCOUNT.PASSWORD_IS_NOT_VALID}")
	private String password;

	@NotBlank(message = "{ACCOUNT.NAME_NOT_BLANK}")
	private String name;

	@NotBlank(message = "{ACCOUNT.EMAIL_NOT_BLANK}")
	@Email(message = "{ACCOUNT.EMAIL_IS_NOT_VALID}")
	private String email;
}