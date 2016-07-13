package demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import demo.MerkleException;

import java.util.HashMap;
import java.util.Map;

public class ChainableBase implements Chainable {

    private final Map<String, Leaf> leaves = new HashMap();

    private Node root = new Node();

    public void clear() {
        leaves.clear();
        setRoot(new Node());
    }

    Map<String, Leaf> getLeaves() {
        return leaves;
    }

    @JsonProperty
    public int size() {
        return leaves.size();
    }

    void rehash(Leaf leaf) {
        if (leaf.getParent() != null) {
            rehash(leaf.getParent());
        }
    }

    private void rehash(Node node) {
        node.rehash();

        if (!getRoot().equals(node)) {
            //keep going if we are not at the root yet
            rehash(node.getParent());
        }
    }

    @JsonProperty
    public Node getRoot() {
        return root;
    }

    protected void setRoot(Node root) {
        this.root = root;
    }

    //not exactly the most efficient way to do this.....
    public boolean verify() throws MerkleException {
        if (size() <= 1) {
            return true;
        }

        for (Leaf l : leaves.values()) {
            l.getParent().verify();
        }
        return true;
    }

    //validate this entry up through the root
    public boolean verify(String key, String entry) throws MerkleException {
        if (key == null || entry == null) {
            throw new MerkleException("null key or entry not allowed.");
        }

        Leaf leaf = leaves.get(key);
        if (leaf == null) {
            throw new MerkleException("no entry found for key: " + key);
        }

        leaf.verify(entry);

        Node parent = leaf.getParent();
        while (parent != null) {
            parent.verify();
            parent = parent.getParent();
        }

        return true;
    }

    public Leaf get(String key) {
        return leaves.get(key);
    }
}