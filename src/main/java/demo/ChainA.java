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

@RestController
public class ChainA {

    @Autowired
    private Hasher hasher;

    private static final Logger LOG = Logger.getLogger(ChainA.class);

    private final Map<String, String> map = new HashMap<String, String>();


    @RequestMapping(value = "/chainA")
    public Object addEntry(@RequestParam(value = "entry", required = false) String entry) {
        if (entry == null) {
            return all();
        }

        String key = hasher.hash(entry);
        map.put(key, entry);
        return key;
    }

    @RequestMapping(value = "/chainA/{key}")
    public String getEntry(@PathVariable String key) {
        return map.get(key);
    }

    public Set<Map.Entry<String, String>> all() {
        return map.entrySet();
    }

    @RequestMapping(value = "/chainA/verify")
    public boolean verify(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return map.containsKey(hash) && map.get(hash).equals(entry);
    }
}
