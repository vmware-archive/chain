package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.pivotal.cf.chain.MerkleException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChain implements Chainable {

    private final Map<String, Leaf> leaves = new HashMap<>();

    private Node root = new Node();

    public void clear() {
        leaves.clear();
        setRoot(new Node());
    }

    public String getHash() {
        if(getRoot() == null) {
            return null;
        }

        return getRoot().getHash();
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
            verifyToRoot(l);
        }
        return true;
    }

    private void verifyToRoot(Leaf l) throws MerkleException {
        Node parent = l.getParent();
        while (parent != null) {
            parent.verify();
            parent = parent.getParent();
        }
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
        verifyToRoot(leaf);

        return true;
    }

    public Leaf get(String key) {
        return leaves.get(key);
    }

    public static Chainable load(String json) throws MerkleException {
        SimpleModule module = new SimpleModule();

        LeafTranslator ld = new LeafTranslator();
        module.addDeserializer(Leaf.class, ld);
        module.addDeserializer(Node.class, new NodeTranslator());
        module.addDeserializer(Child.class, new ChildTranslator());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MerkleTree mt = new MerkleTree();

        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jp = jsonFactory.createParser(json);
            jp.setCodec(mapper);
            TreeNode jsonNode = jp.readValueAsTree().get("root");
            mt.setRoot((Node) mapper.readValue(jsonNode.toString(), Child.class));
            mt.getLeaves().putAll(ld.leaves);
            return mt;
        } catch (IOException e) {
            throw new MerkleException(e);
        }
    }
}