package demo;

class Node implements Child {

    private String value;

    private Child left;
    private Child right;
    private Node parent;

    void setValue(String s) {
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

    public void setParent(Node parent) {
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
