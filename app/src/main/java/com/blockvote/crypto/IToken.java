package com.blockvote.crypto;

public interface IToken {
	// The token's globally unique ID
	byte[] getID();

	// The issuing registrar's signature on the token
	byte[] getSignature();

}
