package demo.merkle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import demo.Hasher;

import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({"size", "levels", "lastAdded", "root"})
public class MerkleTree {

    private Hasher hasher = new Hasher();

    private MerkleUtil merkleUtil = new MerkleUtil();

    private final Map<String, Leaf> leaves = new HashMap<>();

    private Node root = new Node();

    public void clear() {
        getLeaves().clear();
        setRoot(new Node());
    }

    private Map<String, Leaf> getLeaves() {
        return leaves;
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
        char[] path = merkleUtil.calcPath(size());
        buildPath(path);
        addLeaf(path, l);
    }

    private void addLeaf(char[] path, Leaf l) {
        Node parent = getRoot();

        if (size() > 0) {
            for (char c : path) {
                if (c == '0') {
                    parent = (Node) parent.getLeft();
                }
                if (c == '1') {
                    parent = (Node) parent.getRight();
                }
            }
        }
        parent.setLeft(l);
        l.setParent(parent);
        registerLeaf(l);
    }

    private void buildPath(char[] path) throws MerkleException {
        //special case: no entries yet, the root  will be the parent
        if (size() < 1) {
            return;
        }

        //if tree is full we need a new root to start from
        if (treeIsFull()) {
            createNewRoot();
        }

        Node parent = getRoot();
        //start at root and build out the branch as needed
        for (char c : path) {
            if (c == '0') {
                if (parent.getLeft() == null) {
                    Node left = new Node();
                    parent.setLeft(left);
                    left.setParent(parent);
                }
                parent = (Node) parent.getLeft();
            }

            if (c == '1') {
                if (parent.getRight() == null) {
                    Node right = new Node();
                    parent.setRight(right);
                    right.setParent(parent);
                }
                parent = (Node) parent.getRight();
            }
        }
    }

    private void createNewRoot() {
        Node n = new Node();
        n.setLeft(getRoot());
        getRoot().setParent(n);
        setRoot(n);
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
    Node getRoot() {
        return root;
    }

    void setRoot(Node root) {
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