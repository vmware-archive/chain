package demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@JsonPropertyOrder({"size", "levels", "lastAdded", "root"})
class MerkleTree {

    private final Hasher hasher = new Hasher();

    private final Map<String, Leaf> leaves = new LinkedHashMap<>();

    private Child root;

    void clear() {
        getLeaves().clear();
        setRoot(null);
    }

    private Map<String, Leaf> getLeaves() {
        return leaves;
    }

    @JsonProperty
    Leaf getLastAdded() {
        if(getLeaves().size() < 1) {
            return null;
        }
        return (Leaf) getLeaves().values().toArray()[getLeaves().size() -1];
    }

    Leaf getEntry(String key) {
        return  getLeaves().get(key);
    }

    private String createId() {
        return UUID.randomUUID().toString();
    }

    String addEntry(String entry) {
        //create a leaf for the entry
        Leaf leaf = new Leaf(createId(), hasher.hashAndEncode(entry));

        //add the leaf to the tree
        addLeaf(leaf);

        //calculate hashes from newly added node up to root
        rehash(leaf);

        return leaf.getKey();
    }

    private void addLeaf(Leaf l) {
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
        return isPowerOf2( getLeaves().size());
    }

    private boolean isPowerOf2(int i) {
        return (i != 0) && ((i & (i - 1)) == 0);
    }

    @JsonProperty
    int levels() {
        return levels(size());
    }

    @JsonProperty
    int size() {
        return  getLeaves().size();
    }

    private int levels(int i) {
        if (i <= 0) {
            return 0;
        }

        //calculate base2 log
        double d = Math.log(i) / Math.log(2);

        //round up to next whole number
        return (int) Math.ceil(d);
    }

    private Node addNewLevel() {
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

    private Node createBranch(Node n, int levels) {
        Node parent;
        Node child = n;
        for (int i = 0; i < levels; i++) {
            parent = new Node();
            parent.addChild(child);

            child = parent;
        }

        return child;
    }

    private Node findNextParent() {
        Node n = getLastAdded().getParent();
        int level = 0;
        while (n.getRight() != null) {
            n = n.getParent();
            level++;
            if (n.equals(getRoot())) {
                throw new RuntimeException("hit root, not supposed to happen!");
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
            node.setHash(concatHash(node));
        }
    }

    private String concatHash(Node node) {
        return hasher.hashAndEncode(node.getLeft().getHash() + node.getRight().getHash());
    }

    private void rehash(Node node) {
        setHash(node);

        if (! getRoot().equals(node)) {
            //keep going if we are not at the root yet
            rehash(node.getParent());
        }
    }

    @JsonProperty
    Child getRoot() {
        return root;
    }

    private void setRoot(Child root) {
        this.root = root;
    }

    //validate this entry up through the root
    boolean verify(String key, String entry) {
        Leaf leaf =  getLeaves().get(key);
        if (leaf == null) {
            return false;
        }

        if (!verify(leaf, entry)) {
            return false;
        }

        Node parent = leaf.getParent();
        while (parent != null) {
            if (!verify(parent)) {
                return false;
            }
            parent = parent.getParent();
        }

        return true;
    }

    private boolean verify(Leaf l, String entry) {
        if (l == null || entry == null) {
            return false;
        }

        return l.getHash().equals(hasher.hashAndEncode(entry));
    }

    private boolean verify(Node n) {
        if (n.getLeft() == null && n.getRight() == null) {
            return true;
        }

        if (n.getRight() == null) {
            return n.getLeft().getHash().equals(n.getHash());
        }

        return concatHash(n).equals(n.getHash());
    }

    //not exactly the most efficient way to do this.....
    boolean verify() {
        if (size() <= 1) {
            return true;
        }

        for (Leaf l :  getLeaves().values()) {
            if (!verify(l.getParent())) {
                return false;
            }
        }
        return true;
    }

    MerkleTree loadRandomEntries(String numberOfEntries) {
        int i = 0;
        try {
            i = Integer.parseInt(numberOfEntries);
        } catch (NumberFormatException e) {
            //ignore
        }

        for (int j = 0; j < i; j++) {
            addEntry(createId());
        }

        return this;
    }

    static MerkleTree load(String json) throws IOException {

        MerkleTree mt = new MerkleTree();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Leaf.class, new LeafDeserializer(mt));
        module.addDeserializer(Node.class, new NodeDeserializer(mt));
        module.addDeserializer(Child.class, new ChildDeserializer(mt));

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jp.setCodec(mapper);
        TreeNode jsonNode = jp.readValueAsTree().get("root");

        String s = jsonNode.toString();

        Child n = mapper.readValue(s, Child.class);

        mt.setRoot(n);

        return mt;
    }
}