package io.pivotal.cf.chain;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
class MapB extends HashMap<String, String> {

    String put(String entry) {

        String key = UUID.randomUUID().toString();
        String value = Hasher.hashAndEncode(entry);

        put(key, value);
        return key;
    }

    boolean verify(String entry, String key) {
        return containsKey(key) && get(key).equals(Hasher.hashAndEncode(entry));
    }
}