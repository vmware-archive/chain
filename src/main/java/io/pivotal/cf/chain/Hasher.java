package io.pivotal.cf.chain;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Hasher {

    public String hashAndEncode(String s) {
        if (s == null) {
            return null;
        }
        return encode(hash(s));
    }

    public byte[] hash(String s) {
        return DigestUtils.getSha256Digest().digest(s.getBytes());
    }

    public String encode(byte[] b) {
        return Base64.encodeBase64URLSafeString(b);
    }

    public String createId() {
        return UUID.randomUUID().toString();
    }
}