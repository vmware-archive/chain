package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.pivotal.cf.chain.Hasher;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"hash", "leaves", "root"})
public class Chain extends AbstractChain {

    public Chain(Hasher hasher) {
        super(hasher);
    }

    public void addBlock(MerkleTree tree) throws VerificationException {
        if (leaves() == 0) {
            setRoot(tree.getRoot());
        } else {
            if (leaves() >= MAX_CHAIN_ENTRIES) {
                throw new VerificationException("chain max size: " + MAX_CHAIN_ENTRIES + " reached.", HttpStatus.INSUFFICIENT_STORAGE);
            }
            Node newRoot = new Node();
            newRoot.addChild(getRoot());
            newRoot.addChild(tree.getRoot());
            newRoot.rehash(getHasher());
            setRoot(newRoot);
        }

        getLeaves().putAll(tree.getLeaves());
    }
}
