package com.blockvote.crypto;

import java.math.BigInteger;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.digests.SHA1Digest;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.signers.PSSSigner;

import com.blockvote.crypto.IToken;
import com.blockvote.crypto.ITokenRequest;

public class Registrar implements IRegistrar{
    private final RSAKeyParameters publicKey;
    private final RSAKeyParameters privateKey;
    public final String name;

    public Registrar(BigInteger publicModulus, BigInteger publicExponent, BigInteger privateModulus, BigInteger privateExponent, String registrarName) {
        publicKey = new RSAKeyParameters(false, publicModulus, publicExponent);
        privateKey = new RSAKeyParameters(true, privateModulus, privateExponent);
        name = registrarName;
    }

    public RSAKeyParameters getPublic() {
        AsymmetricCipherKeyPair keys = new AsymmetricCipherKeyPair(publicKey, privateKey);
        return (RSAKeyParameters) keys.getPublic();
    }

    public byte[] sign(ITokenRequest tokenRequest) {
        // Sign the coin request using our private key.
        byte[] message = tokenRequest.getMessage();

        RSAEngine engine = new RSAEngine();
        engine.init(true, privateKey);

        return engine.processBlock(message, 0, message.length);
    }

    public boolean verify(IToken coin) {
        // Verify that the coin has a valid signature using our public key.
        byte[] id = coin.getID();
        byte[] signature = coin.getSignature();

        PSSSigner signer = new PSSSigner(new RSAEngine(), new SHA1Digest(), 20);
        signer.init(false, publicKey);

        signer.update(id, 0, id.length);

        return signer.verifySignature(signature);
    }

}

