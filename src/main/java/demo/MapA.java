package demo;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
class MapA extends  HashMap<String, String>{

    String put(String entry) {
        String key = Hasher.hashAndEncode(entry);
        put(key, entry);
        return key;
    }

    boolean verify(String entry, String hash) {
        return containsKey(hash) && get(hash).equals(entry);
    }
}