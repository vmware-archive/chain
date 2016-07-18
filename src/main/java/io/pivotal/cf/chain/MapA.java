package io.pivotal.cf.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
class MapA extends HashMap<String, String> {

    @Autowired
    private Hasher hasher;

    String put(String entry) {
        String key = hasher.hash(entry);
        put(key, entry);
        return key;
    }

    boolean verify(String entry, String hash) {
        return containsKey(hash) && get(hash).equals(entry);
    }
}