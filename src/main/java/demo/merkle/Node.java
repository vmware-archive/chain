package demo.merkle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
class Node extends Child {

    private Child left;

    private Child right;

    void setLeft(Child left) {
        this.left = left;
    }

    void setRight(Child right) {
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
        if(getLeft() != null) {
            s += ",left=" + getLeft().getHash();
        }

        if(getRight() != null) {
            s += ",right=" + getRight().getHash();
        }
        return s;
    }
}