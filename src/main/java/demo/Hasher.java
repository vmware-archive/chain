package demo;

import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    private static final Logger LOG = Logger.getLogger(Hasher.class);

    private MessageDigest md;

    public String hash(String entry) {
        if (entry == null) {
            return null;
        }

        byte[] bytes = getDigest().digest(entry.getBytes(StandardCharsets.UTF_8));

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private MessageDigest getDigest() {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                LOG.error("Error getting message digest", e);
            }
        }

        return md;
    }
}
