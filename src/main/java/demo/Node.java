package demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class Node implements Child {

    private String hash;

    private Child left;
    private Child right;
    private Node parent;

    void setHash(String s) {
        this.hash = s;
    }

    void setLeft(Child left) {
        this.left = left;
    }

    void setRight(Child right) {
        this.right = right;
    }

    @JsonIgnore
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getHash() {
        return hash;
    }

    public Child getLeft() {
        return left;
    }

    public Child getRight() {
        return right;
    }

    //TODO better exception
    void addChild(Child child) {
        if (getLeft() != null && getRight() != null) {
            throw new RuntimeException("already full!");
        }
        if (getLeft() == null) {
            setLeft(child);
        } else {
            setRight(child);
        }

        child.setParent(this);
    }
}
