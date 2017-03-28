package com.blockvote.fragments;

import org.junit.Test;
import com.blockvote.crypto.*;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.crypto.params.RSAKeyParameters;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Dave on 3/28/2017.
 */

public class CryptoUnitTest {
    @Test
    public void testValidSignature() throws Exception {
        // Generate a 2048-bit RSA key pair.
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters(
                new BigInteger("10001", 16), new SecureRandom(), 2048,
                80));

        AsymmetricCipherKeyPair keys = generator.generateKeyPair();
        RSAKeyParameters publicKey=(RSAKeyParameters) keys.getPublic();
        RSAKeyParameters privateKey=(RSAKeyParameters) keys.getPrivate();

        //Create registrar object
        IRegistrar registrar=new Registrar(publicKey.getModulus(), publicKey.getExponent(), privateKey.getModulus(), privateKey.getExponent(), "reg1");

        // Create a "blinded token" using the registrar's public key. The blinded token
        // contains an internal blinding factor that is used to blind the
        // message to be signed by the registrar.
        BlindedToken blindedToken = new BlindedToken(registrar.getPublic());

        // Generate a token request.
        TokenRequest tokenRequest = blindedToken.generateTokenRequest();

        // Ask the registrar to sign the token request.

        byte[] signature = registrar.sign(tokenRequest);

        // Create a new token using the registrar's signature.
        Token token = blindedToken.unblindToken(signature);

        // Verify token after unblinding
        assertEquals(true, registrar.verify(token));
    }

    @Test
    public void testInvalidSignature() throws Exception {
        // Generate a 2048-bit RSA key pair.
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters(
                new BigInteger("10001", 16), new SecureRandom(), 2048,
                80));

        AsymmetricCipherKeyPair keys = generator.generateKeyPair();
        RSAKeyParameters publicKey=(RSAKeyParameters) keys.getPublic();
        RSAKeyParameters privateKey=(RSAKeyParameters) keys.getPrivate();

        //Create registrar object
        IRegistrar registrar1=new Registrar(publicKey.getModulus(), publicKey.getExponent(), privateKey.getModulus(), privateKey.getExponent(), "reg1");

        keys = generator.generateKeyPair();
        publicKey=(RSAKeyParameters) keys.getPublic();
        privateKey=(RSAKeyParameters) keys.getPrivate();

        //Create registrar object
        IRegistrar registrar2=new Registrar(publicKey.getModulus(), publicKey.getExponent(), privateKey.getModulus(), privateKey.getExponent(), "reg2");

        // Use a different key to genereate the tokens
        BlindedToken blindedToken = new BlindedToken(registrar2.getPublic());

        // Generate a token request.
        TokenRequest tokenRequest = blindedToken.generateTokenRequest();

        // Ask the registrar to sign the token request.

        byte[] signature = registrar1.sign(tokenRequest);

        // Create a new token using the registrar's signature.
        Token token = blindedToken.unblindToken(signature);

        // Verify token after unblinding
        assertEquals(false, registrar1.verify(token));
    }
}
