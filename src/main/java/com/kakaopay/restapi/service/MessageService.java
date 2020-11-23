package com.kakaopay.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String messageKey) {
		return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
	}
}
