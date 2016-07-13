package demo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class MapB {

    private final Map<String, String> map = new HashMap<>();

    Object addEntry(String entry) {
        if (entry == null) {
            return all();
        }

        String key = UUID.randomUUID().toString();
        String value = Hasher.hashAndEncode(entry);

        map.put(key, value);
        return key;
    }

    String getEntry(String key) {
        return map.get(key);
    }

    Map<String, String> all() {
        return map;
    }

    boolean verify(String entry, String key) {
        return map.containsKey(key) && map.get(key).equals(Hasher.hashAndEncode(entry));
    }
}