package com.strategy.api.persistence.oauth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Created on 2/21/22.
 */
@Slf4j
@Component
public class TokenEncryption {
    private final String secretKey;
    private final String salt;

    public TokenEncryption(final @Value("${token-encryption.secret}") String secretKey,
                           final @Value("${token-encryption.salt}")String salt) {
        this.secretKey = secretKey;
        this.salt = salt;
    }

    public String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = getCypher(Cipher.ENCRYPT_MODE);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Error while encrypting token", e);
        }
        return null;
    }

    private Cipher getCypher(int encryptMode) throws Exception {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(encryptMode, secretKey, ivspec);
        return cipher;
    }

    public String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = getCypher(Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.error(String.format("Error while decrypting token: %s", strToDecrypt));
            return strToDecrypt;
        }
    }
}
