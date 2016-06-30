package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChainController {

    @Autowired
    private ChainA chainA;

    @Autowired
    private ChainB chainB;

    @Autowired
    private BlockChain merkle;

    @RequestMapping(value = "/chainA/add/{entry}")
    public Object addChainAEntry(@PathVariable String entry) {
        return chainA.addEntry(entry);
    }

    @RequestMapping(value = "/chainA/find/{key}")
    public String getChainAEntry(@PathVariable String key) {
        return chainA.getEntry(key);
    }

    @RequestMapping(value = "/chainA/verify")
    public boolean verifyChainA(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return chainA.verify(entry, hash);
    }

    @RequestMapping(value = "/chainA")
    public Map<String, String> chainA() {
        return chainA.all();
    }

    @RequestMapping(value = "/chainB/add/{entry}")
    public Object addChainBEntry(@PathVariable String entry) {
        return chainB.addEntry(entry);
    }

    @RequestMapping(value = "/chainB/find/{key}")
    public String getChainBEntry(@PathVariable String key) {
        return chainB.getEntry(key);
    }

    @RequestMapping(value = "/chainB/verify")
    public boolean verifyChainB(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return chainB.verify(entry, hash);
    }

    @RequestMapping(value = "/chainB")
    public Map<String, String> chainB() {
        return chainB.all();
    }

    @RequestMapping(value = "/merkle/add/{entry}")
    public Object addMerkleEntry(@PathVariable String entry) {
        return merkle.addEntry(entry);
    }

    @RequestMapping(value = "/merkle/find/{key}")
    public Child getMerkleEntry(@PathVariable String key) {
        return merkle.getEntry(key);
    }

    @RequestMapping(value = "/merkle/verify")
    public boolean verifyMerkle(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return merkle.verify(entry, hash);
    }

    @RequestMapping(value = "/merkle")
    public Node merkle() {
        return merkle.all();
    }
}