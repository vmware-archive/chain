package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.pivotal.cf.chain.Hasher;
import io.pivotal.cf.chain.MerkleException;

public class Leaf extends Child {

    private String key;

    Leaf(String key, String hash) {
        this();
        setKey(key);
        setHash(hash);
    }

    Leaf() {
        super();
    }

    @JsonProperty
    public String getKey() {
        return key;
    }

    void setKey(String s) {
        this.key = s;
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
        return "Leaf:key=" + getKey() + ",hash=" + getHash();
    }

    public void verify(String entry) throws MerkleException {
        if (getHash() == null || entry == null) {
            throw new MerkleException("unable to verify: null entry or hash encountered.");
        }

        if (!getHash().equals(Hasher.hashAndEncode(entry))) {
            throw new MerkleException("hash mismatch for entry: " + entry);
        }
    }
}