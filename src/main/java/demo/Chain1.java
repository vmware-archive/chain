package demo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jared on 5/18/16.
 */

public class Chain1 {

    @Autowired
    private Hasher hasher;

    private static final Logger LOG = Logger.getLogger(Chain1.class);

    private final Map<String, String> chain = new HashMap<String, String>();


    public String addEntry(String entry) {
        if(entry == null) {
            return null;
        }

        String key = hasher.hash(entry);
        chain.put(key, entry);
        return key;
    }

    public boolean verify(String entry, String hash) {
        return chain.containsKey(hash) && chain.get(hash).equals(entry);
    }
}
