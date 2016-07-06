package demo;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;

public class Hasher {

    public String hashAndEncode(String s) {
        if(s == null) {
            return null;
        }
        return new String(encode(hash(s)));
    }

    private byte[] hash(String s) {
        return DigestUtils.getSha256Digest().digest(s.getBytes());
    }

    private byte[] encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }
}
