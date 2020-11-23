package com.kakaopay.restapi.exception;

import com.kakaopay.restapi.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
public class ExceptionAdvisor {

	private static final List<String> EMPTY_ARRAY= new ArrayList<>();

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity validationExceptionHandler(MethodArgumentNotValidException exception) {
		return getValidationErrorResponseEntity(exception.getBindingResult());
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity bindingExceptionHandler(BindException exception) {
		return getValidationErrorResponseEntity(exception.getBindingResult());
	}

	private ResponseEntity getValidationErrorResponseEntity(BindingResult bindingResult) {
		List<Map> errors = new ArrayList<>();
		bindingResult.getFieldErrors().forEach(fieldError -> {
			Map<String, String> error = new HashMap<>();
			error.put("field", fieldError.getField());
			error.put("defaultMessage", fieldError.getDefaultMessage());
			errors.add(error);
		});

		return new ResponseEntity(new ErrorResponse(
				(String) errors.get(0).get("defaultMessage"),
				(String) errors.get(0).get("field"),
				errors,
				400), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity defaultExceptionHandler(Exception exception) {
		return new ResponseEntity(new ErrorResponse(
				exception.getMessage(),
				exception.getClass().toString(),
				EMPTY_ARRAY,
				500), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
