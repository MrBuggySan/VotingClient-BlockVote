package com.blockvote.security;

public interface ITokenRequest {
	// The message (blind) to be signed by the registrar
	byte[] getMessage();
}
