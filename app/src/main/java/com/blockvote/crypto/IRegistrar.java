package com.blockvote.crypto;

import org.spongycastle.crypto.params.RSAKeyParameters;

public interface IRegistrar {
	// The registrar's RSA public key
	RSAKeyParameters getPublic();

	// Sign a token request
	byte[] sign(ITokenRequest tokenRequest);

	// Verify a token
	boolean verify(IToken token);

}
