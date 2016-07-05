package demo;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;

class Hasher {

    String hashAndEncode(String s) {
        if(s == null) {
            return null;
        }
        return new String(encode(hash(s)));
    }

    byte[] hash(String s) {
        return DigestUtils.getSha256Digest().digest(s.getBytes());
    }

    byte[] encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }
}
