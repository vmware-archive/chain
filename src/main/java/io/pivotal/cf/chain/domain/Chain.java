package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.pivotal.cf.chain.Hasher;

@JsonPropertyOrder({"hash", "size", "root"})
public class Chain extends AbstractChain {

    public Chain(Hasher hasher) {
        super(hasher);
    }

    public void addBlock(MerkleTree tree) {
        if (size() == 0) {
            setRoot(tree.getRoot());
        } else {
            Node newRoot = new Node();
            newRoot.addChild(getRoot());
            newRoot.addChild(tree.getRoot());
            newRoot.rehash(getHasher());
            setRoot(newRoot);
        }

        getLeaves().putAll(tree.getLeaves());
    }
}
