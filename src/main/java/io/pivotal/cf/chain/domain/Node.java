package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.pivotal.cf.chain.Hasher;
import io.pivotal.cf.chain.MerkleException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node extends Child {

    private Child left;

    private Child right;

    Node() {
        super();
    }

    Node(String hash) {
        this();
        setHash(hash);
    }

    private void setLeft(Child left) {
        this.left = left;
    }

    private void setRight(Child right) {
        this.right = right;
    }

    @JsonProperty
    Child getLeft() {
        return left;
    }

    @JsonProperty
    Child getRight() {
        return right;
    }

    void addChild(Child child) throws MerkleException {
        if (getLeft() != null && getRight() != null) {
            throw new MerkleException("already full!");
        }
        if (getLeft() == null) {
            setLeft(child);
        } else {
            setRight(child);
        }

        child.setParent(this);
    }

    public int hashCode() {
        if (getHash() == null) {
            return -1;
        }
        return getHash().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }

        return hashCode() == o.hashCode();
    }

    public String toString() {
        String s = "Node:hash=" + getHash();
        if (getLeft() != null) {
            s += ",left=" + getLeft().getHash();
        }

        if (getRight() != null) {
            s += ",right=" + getRight().getHash();
        }
        return s;
    }

    void rehash() {
        //if node has no sibling, just inherit child hash
        if (getRight() == null) {
            setHash(getLeft().getHash());
        } else {
            //set hash to the hash of the concatenated child hashes
            setHash(concatHash());
        }
    }

    private String concatHash() {
        return Hasher.hashAndEncode(getLeft().getHash() + getRight().getHash());
    }

    public void verify() throws MerkleException {
        if (getLeft() == null && getRight() == null && getHash() == null) {
            return;
        }

        if (getRight() == null) {
            if (!getLeft().getHash().equals(getHash())) {
                throw new MerkleException("child hash inheritance verification failed: " + this);
            } else {
                return;
            }
        }

        if (!concatHash().equals(getHash())) {
            throw new MerkleException("hash concatenation failed: " + this);
        }
    }
}