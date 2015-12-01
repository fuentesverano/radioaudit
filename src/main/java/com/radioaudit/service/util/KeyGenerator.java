package com.radioaudit.service.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

	public String generateToken() {
		String token = UUID.randomUUID().toString();
		return token;
	}

	public String generateIdentifier(String token) {
		String identifier = UUID.randomUUID().toString();
		return identifier;
	}
}
