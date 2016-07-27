package io.pivotal.cf.chain;

import io.pivotal.cf.chain.domain.Chain;
import io.pivotal.cf.chain.domain.Leaf;
import io.pivotal.cf.chain.domain.MerkleTree;
import io.pivotal.cf.chain.domain.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private MapA mapA;

    @Autowired
    private MapB mapB;

    @Autowired
    private MerkleTree merkleTree;

    @Autowired
    private Chain chain;

    @Autowired
    Hasher hasher;

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @RequestMapping(value = "/mapA/add/{entry}")
    public ResponseEntity<String> addChainAEntry(@PathVariable String entry) {
        return new ResponseEntity<>(mapA.put(entry), httpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/mapA/find/{key}")
    public ResponseEntity<String> getChainAEntry(@PathVariable String key) {
        return new ResponseEntity<>(mapA.get(key), httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mapA/verify")
    public ResponseEntity<Boolean> verifyChainAEntry(@RequestParam(value = "entry") String entry, @RequestParam(value = "hash") String hash) {
        return new ResponseEntity<>(mapA.verify(entry, hash), httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mapA")
    public ResponseEntity<MapA> chainA() {
        return new ResponseEntity<>(mapA, httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mapB/add/{entry}")
    public ResponseEntity<String> addChainBEntry(@PathVariable String entry) {
        return new ResponseEntity<>(mapB.put(entry), httpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/mapB/find/{key}")
    public ResponseEntity<String> getChainBEntry(@PathVariable String key) {
        return new ResponseEntity<>(mapB.get(key), httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mapB/verify")
    public ResponseEntity<Boolean> verifyChainB(@RequestParam(value = "entry") String entry, @RequestParam(value = "key") String key) {
        return new ResponseEntity<>(mapB.verify(entry, key), httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mapB")
    public ResponseEntity<MapB> mapB() {
        return new ResponseEntity<>(mapB, httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/merkleTree/add/{entry}")
    public ResponseEntity<String> addMerkleEntry(@PathVariable String entry) throws VerificationException {
        return new ResponseEntity<>(merkleTree.addEntry(entry), httpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/merkleTree/find/{key}")
    public ResponseEntity<Leaf> getMerkleEntry(@PathVariable String key) {
        return new ResponseEntity<>(merkleTree.get(key), httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/merkleTree/verify")
    public ResponseEntity<Boolean> verifyMerkle(@RequestParam(value = "key", required = false) String key, @RequestParam(value = "entry", required = false) String entry) throws VerificationException {
        if (key == null && entry == null) {
            merkleTree.verify();
            return new ResponseEntity<>(Boolean.TRUE, httpHeaders(), HttpStatus.OK);
        }

        if (key == null || entry == null) {
            return new ResponseEntity<>(Boolean.FALSE, httpHeaders(), HttpStatus.OK);
        }

        merkleTree.verify(key, entry);
        return new ResponseEntity<>(Boolean.TRUE, httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/merkleTree")
    public ResponseEntity<MerkleTree> merkle() {
        return new ResponseEntity<>(merkleTree, httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/merkleTree/load/{numberOfEntries}")
    public ResponseEntity<MerkleTree> merkleLoad(@PathVariable String numberOfEntries) throws VerificationException {
        int i = 0;
        try {
            i = Integer.parseInt(numberOfEntries);
        } catch (NumberFormatException e) {
            //ignore
        }
        merkleTree.loadRandomEntries(i);
        return new ResponseEntity<>(merkleTree, httpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/merkleTree/clear")
    public ResponseEntity merkleClear() {
        merkleTree.clear();
        return new ResponseEntity(httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/chain/addBlock")
    public ResponseEntity<Chain> addChainBlock() throws VerificationException {
        chain.addBlock(merkleTree);
        merkleTree.clear();
        return new ResponseEntity<>(chain, httpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/chain/find/{key}")
    public ResponseEntity<Leaf> getChainEntry(@PathVariable String key) {
        return new ResponseEntity<>(chain.get(key), httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/chain/verify")
    public ResponseEntity<Boolean> verifyChain(@RequestParam(value = "key", required = false) String key, @RequestParam(value = "entry", required = false) String entry) throws VerificationException {
        if (key == null && entry == null) {
            chain.verify();
            return new ResponseEntity<>(Boolean.TRUE, httpHeaders(), HttpStatus.OK);
        }

        if (key == null || entry == null) {
            return new ResponseEntity<>(Boolean.FALSE, httpHeaders(), HttpStatus.OK);
        }

        chain.verify(key, entry);
        return new ResponseEntity<>(Boolean.TRUE, httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/chain")
    public ResponseEntity<Chain> chain() {
        return new ResponseEntity<>(chain, httpHeaders(), HttpStatus.OK);
    }


    @RequestMapping(value = "/chain/clear")
    public ResponseEntity chainClear() {
        chain.clear();
        return new ResponseEntity(httpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/hasher/hash/{entry}")
    public ResponseEntity<String> hasherHash(@PathVariable String entry) {
        return new ResponseEntity<>(hasher.hash(entry), httpHeaders(), HttpStatus.OK);
    }
}