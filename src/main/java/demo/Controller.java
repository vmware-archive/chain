package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private MapA mapA;

    @Autowired
    private MapB mapB;

    @Autowired
    private MerkleTree merkleTree;

    @RequestMapping(value = "/mapA/add/{entry}")
    public Object addChainAEntry(@PathVariable String entry) {
        return mapA.addEntry(entry);
    }

    @RequestMapping(value = "/mapA/find/{key}")
    public String getChainAEntry(@PathVariable String key) {
        return mapA.getEntry(key);
    }

    @RequestMapping(value = "/mapA/verify")
    public boolean verifyChainA(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return mapA.verify(entry, hash);
    }

    @RequestMapping(value = "/mapA")
    public Map<String, String> chainA() {
        return mapA.all();
    }

    @RequestMapping(value = "/mapB/add/{entry}")
    public Object addChainBEntry(@PathVariable String entry) {
        return mapB.addEntry(entry);
    }

    @RequestMapping(value = "/mapB/find/{key}")
    public String getChainBEntry(@PathVariable String key) {
        return mapB.getEntry(key);
    }

    @RequestMapping(value = "/mapB/verify")
    public boolean verifyChainB(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return mapB.verify(entry, hash);
    }

    @RequestMapping(value = "/mapB")
    public Map<String, String> chainB() {
        return mapB.all();
    }

    @RequestMapping(value = "/merkleTree/add/{entry}")
    public Object addMerkleEntry(@PathVariable String entry) {
        return merkleTree.addEntry(entry);
    }

    @RequestMapping(value = "/merkleTree/find/{key}")
    public Leaf getMerkleEntry(@PathVariable String key) {
        return merkleTree.getEntry(key);
    }

    @RequestMapping(value = "/merkleTree/verify")
    public boolean verifyMerkle(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return merkleTree.verify(entry, hash);
    }

    @RequestMapping(value = "/merkleTree")
    public MerkleTree merkle() {
        return merkleTree;
    }

    @RequestMapping(value = "/merkleTree/load/{entries}")
    public Child merkleLoad(@PathVariable String entries) {
        return merkleTree.load(entries);
    }

    @RequestMapping(value = "/merkleTree/clear")
    public void merkleClear() {
        merkleTree.clear();
    }
}