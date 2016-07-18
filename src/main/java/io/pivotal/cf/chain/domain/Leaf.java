package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.pivotal.cf.chain.Hasher;
import org.springframework.http.HttpStatus;

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

    public void verify(String entry, Hasher hasher) throws VerificationException {
        if (getHash() == null || entry == null) {
            throw new VerificationException("unable to verify: null entry or hash encountered", this, HttpStatus.BAD_REQUEST);
        }

        if (!getHash().equals(hasher.hash(entry))) {
            throw new VerificationException("hash mismatch for entry: " + entry, this, HttpStatus.CONFLICT);
        }
    }
}