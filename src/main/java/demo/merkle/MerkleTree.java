package demo.merkle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import demo.Hasher;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonPropertyOrder({"size", "levels", "lastAdded", "root"})
public class MerkleTree {

    private Hasher hasher = new Hasher();

    private MerkleUtil merkleUtil = new MerkleUtil();

    private final Map<String, Leaf> leaves = new LinkedHashMap<>();

    private Child root;

    public void clear() {
        getLeaves().clear();
        setRoot(null);
    }

    private Map<String, Leaf> getLeaves() {
        return leaves;
    }

    Leaf getLastAdded() {
        if (getLeaves().size() < 1) {
            return null;
        }
        return (Leaf) getLeaves().values().toArray()[getLeaves().size() - 1];
    }

    public Leaf getEntry(String key) {
        return getLeaves().get(key);
    }

    public String addEntry(String entry) throws MerkleException {
        //create a leaf for the entry
        Leaf leaf = new Leaf(hasher.createId(), hasher.hashAndEncode(entry));

        //add the leaf to the tree
        addLeaf(leaf);

        //calculate hashes from newly added node up to root
        rehash(leaf);

        return leaf.getKey();
    }

    private void addLeaf(Leaf l) throws MerkleException {
        //no root?
        if (getRoot() == null) {
            setRoot(l);
        } else if (treeIsFull()) {
            //we need a new root
            addNewLevel().addChild(l);
        } else {

            //get an open slot to put this into
            Node parent = findNextParent();

            //add the new node to the tree
            parent.addChild(l);
        }

        //add the leaf to the leaves collection
        registerLeaf(l);
    }

    void registerLeaf(Leaf l) {
        getLeaves().put(l.getKey(), l);
    }

    private boolean treeIsFull() {
        return merkleUtil.isPowerOf2(size());
    }

    @JsonProperty
    int levels() {
        return merkleUtil.levels(size());
    }

    @JsonProperty
    private int size() {
        return getLeaves().size();
    }

    private Node addNewLevel() throws MerkleException {
        Node node = new Node();

        //create a new node branch n levels deep
        Node newRoot = createBranch(node, levels());

        //swap the new root's left to its right
        newRoot.setRight(newRoot.getLeft());

        //put the old root in the left of the newRoot
        newRoot.setLeft(getRoot());
        getRoot().setParent(newRoot);
        setRoot(newRoot);

        return node;
    }

    private Node createBranch(Node n, int levels) throws MerkleException {
        Node parent;
        Node child = n;
        for (int i = 0; i < levels; i++) {
            parent = new Node();
            parent.addChild(child);

            child = parent;
        }

        return child;
    }

    private Node findNextParent() throws MerkleException {
        Node n = getLastAdded().getParent();
        int level = 0;
        while (n.getRight() != null) {
            n = n.getParent();
            level++;
            if (n.equals(getRoot())) {
                throw new MerkleException("hit root, not supposed to happen!");
            }
        }

        if (level == 0) {
            return n;
        }

        Node bottom = new Node();
        Node top = createBranch(bottom, level - 1);

        n.addChild(top);

        return bottom;
    }

    private void rehash(Leaf leaf) {
        if (leaf.getParent() != null) {
            rehash(leaf.getParent());
        }
    }

    private void setHash(Node node) {
        //if node has no sibling, just inherit child hash
        if (node.getRight() == null) {
            node.setHash(node.getLeft().getHash());
        } else {
            //set hash to the hash of the concatenated child hashes
            node.setHash(merkleUtil.concatHash(node));
        }
    }

    private void rehash(Node node) {
        setHash(node);

        if (!getRoot().equals(node)) {
            //keep going if we are not at the root yet
            rehash(node.getParent());
        }
    }

    @JsonProperty
    Child getRoot() {
        return root;
    }

    void setRoot(Child root) {
        this.root = root;
    }

    //not exactly the most efficient way to do this.....
    public boolean verify() throws MerkleException {
        if (size() <= 1) {
            return true;
        }

        for (Leaf l : getLeaves().values()) {
            merkleUtil.verify(l.getParent());
        }
        return true;
    }

    //validate this entry up through the root
    public boolean verify(String key, String entry) throws MerkleException {
        if (key == null || entry == null) {
            throw new MerkleException("null key or entry not allowed.");
        }

        Leaf leaf = getLeaves().get(key);
        if (leaf == null) {
            throw new MerkleException("no entry found for key: " + key);
        }

        merkleUtil.verify(leaf, entry);

        Node parent = leaf.getParent();
        while (parent != null) {
            merkleUtil.verify(parent);
            parent = parent.getParent();
        }

        return true;
    }
}