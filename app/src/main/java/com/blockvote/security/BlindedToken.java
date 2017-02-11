package com.blockvote.security;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.digests.SHA1Digest;
import org.spongycastle.crypto.engines.RSABlindingEngine;
import org.spongycastle.crypto.generators.RSABlindingFactorGenerator;
import org.spongycastle.crypto.params.RSABlindingParameters;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.signers.PSSSigner;

public class BlindedToken {
	private final byte[] tokenID;
	private final RSABlindingParameters blindingParams;

	public BlindedToken(RSAKeyParameters pub) {
		// Create a 128-bit globally unique ID for the coin.
		tokenID = getRandomBytes(16);

		// Generate a blinding factor using the bank's public key.
		RSABlindingFactorGenerator blindingFactorGenerator
			= new RSABlindingFactorGenerator();
		blindingFactorGenerator.init(pub);

		BigInteger blindingFactor
			= blindingFactorGenerator.generateBlindingFactor();

		blindingParams = new RSABlindingParameters(pub, blindingFactor);
	}

	public TokenRequest generateTokenRequest() throws CryptoException {
		// "Blind" the coin and generate a coin request to be signed by the
		// bank.
		PSSSigner signer = new PSSSigner(new RSABlindingEngine(),
				new SHA1Digest(), 20);
		signer.init(true, blindingParams);

		signer.update(tokenID, 0, tokenID.length);

		byte[] sig = signer.generateSignature();

		return new TokenRequest(sig);
	}
	
	public Token unblindToken(byte[] signature) {
		// "Unblind" the bank's signature (so to speak) and create a new coin
		// using the ID and the unblinded signature.
		RSABlindingEngine blindingEngine = new RSABlindingEngine();
		blindingEngine.init(false, blindingParams);

		byte[] s = blindingEngine.processBlock(signature, 0, signature.length);

		return new Token(tokenID, s);
	}
	
	private static byte[] getRandomBytes(int count) {
		byte[] bytes = new byte[count];
		new SecureRandom().nextBytes(bytes);
		return bytes;
	}
}
