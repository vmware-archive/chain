package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.pivotal.cf.chain.Hasher;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChain implements Chainable {

    private Hasher hasher;

    private final Map<String, Leaf> leaves = new HashMap<>();

    private Node root = new Node();

    AbstractChain(Hasher hasher) {
        super();
        setHasher(hasher);
    }

    public void clear() {
        leaves.clear();
        setRoot(new Node());
    }

    public String getHash() {
        if (getRoot() == null) {
            return null;
        }

        return getRoot().getHash();
    }

    Map<String, Leaf> getLeaves() {
        return leaves;
    }

    @JsonProperty
    public int leaves() {
        return leaves.size();
    }

    void rehash(Leaf leaf) {
        if (leaf.getParent() != null) {
            rehash(leaf.getParent());
        }
    }

    private void rehash(Node node) {
        node.rehash(getHasher());

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
    public void verify() throws VerificationException {
        if (leaves() <= 1) {
            return;
        }

        for (Leaf l : leaves.values()) {
            verifyToRoot(l);
        }
    }

    private void verifyToRoot(Leaf l) throws VerificationException {
        Node parent = l.getParent();
        while (parent != null) {
            parent.verify(getHasher());
            parent = parent.getParent();
        }
    }

    //validate this entry up through the root
    public void verify(String key, String entry) throws VerificationException {
        if (key == null || entry == null) {
            throw new VerificationException("null key or entry not allowed.", HttpStatus.BAD_REQUEST);
        }

        Leaf leaf = leaves.get(key);
        if (leaf == null) {
            throw new VerificationException("no entry found for key: " + key, HttpStatus.NOT_FOUND);
        }

        leaf.verify(entry, getHasher());
        verifyToRoot(leaf);
    }

    public Leaf get(String key) {
        return leaves.get(key);
    }

    protected Hasher getHasher() {
        return hasher;
    }

    private void setHasher(Hasher hasher) {
        this.hasher = hasher;
    }

    public Chainable load(String json) throws IOException {
        SimpleModule module = new SimpleModule();

        LeafTranslator ld = new LeafTranslator();
        module.addDeserializer(Leaf.class, ld);
        module.addDeserializer(Node.class, new NodeTranslator());
        module.addDeserializer(Child.class, new ChildTranslator());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser(json);
        jp.setCodec(mapper);
        TreeNode jsonNode = jp.readValueAsTree().get("root");

        MerkleTree mt = new MerkleTree(getHasher());
        mt.setRoot((Node) mapper.readValue(jsonNode.toString(), Child.class));
        mt.getLeaves().putAll(ld.leaves);
        return mt;
    }
}