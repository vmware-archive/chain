package demo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by jared on 5/18/16.
 */

public class Chain2 {

    @Autowired
    private Hasher hasher;

    private static final Logger LOG = Logger.getLogger(Chain2.class);

    private final Map<String, String> chain = new HashMap<String, String>();


    public String addEntry(String entry) {
        if(entry == null) {
            return null;
        }

        String key = UUID.randomUUID().toString();
        String value = hasher.hash(entry);

        chain.put(key, value);
        return key;
    }

    public boolean verify(String entry, String key) {
        return chain.containsKey(key) && chain.get(key).equals(hasher.hash(entry));
    }
}
