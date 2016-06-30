package demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

class Leaf implements Child {

    private String key;
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String s) {
        this.key = s;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public String getHash() {
        return getKey();
    }
}