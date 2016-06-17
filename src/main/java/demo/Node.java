package demo;

class Node implements Child {

    private String value;

    private Child left;
    private Child right;
    private Node parent;

    public void setValue(String s) {
        this.value = s;
    }

    void setLeft(Child left) {
        this.left = left;
    }

    void setRight(Child right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    Child getLeft() {
        return left;
    }

    Child getRight() {
        return right;
    }
}
