package demo;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

public abstract class Hasher {

    public static String hashAndEncode(String s) {
        if (s == null) {
            return null;
        }
        return encode(hash(s));
    }

    private static byte[] hash(String s) {
        return DigestUtils.getSha256Digest().digest(s.getBytes());
    }

    private static String encode(byte[] b) {
        return Base64.encodeBase64URLSafeString(b);
    }

    public static String createId() {
        return UUID.randomUUID().toString();
    }
}