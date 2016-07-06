package demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

abstract class Child {

    private Node parent;
    private String hash;

    @JsonIgnore
    Node getParent() {
        return parent;
    }

    void setParent(Node n) {
        this.parent = n;
    }

    @JsonProperty
    String getHash() {
        return hash;
    }

    void setHash(String hash) {
        this.hash = hash;
    }
}