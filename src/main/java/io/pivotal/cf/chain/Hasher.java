package io.pivotal.cf.chain;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class Hasher {

    private static final String DIGEST_TYPE = "SHA-256";

    private MessageDigest digest;

    public String createId() {
        return UUID.randomUUID().toString();
    }

    public String hash(String s) {
        if (s == null) {
            return null;
        }

        byte[] hash = getDigest().digest(s.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private MessageDigest getDigest() {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance(DIGEST_TYPE);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return digest;
    }
}