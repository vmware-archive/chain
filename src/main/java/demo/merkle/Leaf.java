package demo.merkle;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}