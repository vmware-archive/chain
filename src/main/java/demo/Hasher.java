package demo;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;
import java.util.UUID;

public abstract class Hasher {

    public static String hashAndEncode(String s) {
        if(s == null) {
            return null;
        }
        return new String(encode(hash(s)));
    }

    private static byte[] hash(String s) {
        return DigestUtils.getSha256Digest().digest(s.getBytes());
    }

    private static byte[] encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }

    public static String createId() {
        return UUID.randomUUID().toString();
    }
}
