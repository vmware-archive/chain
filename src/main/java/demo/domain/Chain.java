package demo.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import demo.MerkleException;

@JsonPropertyOrder({"size", "root"})
public class Chain extends ChainableBase {

    public void addBlock(MerkleTree tree) throws MerkleException {
        if (size() == 0) {
            setRoot(tree.getRoot());
        } else {
            Node newRoot = new Node();
            newRoot.addChild(getRoot());
            newRoot.addChild(tree.getRoot());
            newRoot.rehash();
            setRoot(newRoot);
        }

        getLeaves().putAll(tree.getLeaves());
    }
}
