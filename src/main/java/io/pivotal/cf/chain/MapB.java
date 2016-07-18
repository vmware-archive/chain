package io.pivotal.cf.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
class MapB extends HashMap<String, String> {

    @Autowired
    Hasher hasher;

    String put(String entry) {

        String key = UUID.randomUUID().toString();
        String value = hasher.hashAndEncode(entry);

        put(key, value);
        return key;
    }

    boolean verify(String entry, String key) {
        return containsKey(key) && get(key).equals(hasher.hashAndEncode(entry));
    }
}