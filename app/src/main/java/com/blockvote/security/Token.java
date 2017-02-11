package com.blockvote.security;

public class Token implements IToken {
	private final byte[] id;
	private final byte[] signature;

	public Token(byte[] id, byte[] signature) {
		this.id = id;
		this.signature = signature;
	}

	public byte[] getID() {
		return id;
	}

	public byte[] getSignature() {
		return signature;
	}
}

