package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Child {

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
    public String getHash() {
        return hash;
    }

    void setHash(String hash) {
        this.hash = hash;
    }
}