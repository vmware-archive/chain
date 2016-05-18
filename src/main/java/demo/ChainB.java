package demo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
public class ChainB {

    @Autowired
    private Hasher hasher;

    private static final Logger LOG = Logger.getLogger(ChainB.class);

    private final Map<String, String> map = new HashMap<String, String>();

    @RequestMapping(value = "/chainB")
    public Object addEntry(@RequestParam(value = "entry", required = false) String entry) {
        if (entry == null) {
            return all();
        }

        String key = UUID.randomUUID().toString();
        String value = hasher.hash(entry);

        map.put(key, value);
        return key;
    }

    @RequestMapping(value = "/chainB/{key}")
    public String getEntry(@PathVariable String key) {
        return map.get(key);
    }

    public Set<Map.Entry<String, String>> all() {
        return map.entrySet();
    }

    @RequestMapping(value = "/chainB/verify")
    public boolean verify(String entry, String key) {
        return map.containsKey(key) && map.get(key).equals(hasher.hash(entry));
    }
}
