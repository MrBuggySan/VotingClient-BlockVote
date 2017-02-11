package com.blockvote.security;

public class TokenRequest implements ITokenRequest{
	private final byte[] message;

	public TokenRequest(byte[] message) {
		this.message = message;
	}

	public byte[] getMessage() {
		return message;
	}
}
