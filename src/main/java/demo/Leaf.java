package demo;

class Leaf implements Child {

    private String value;
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public String getValue() {
        return value;
    }

    void setValue(String s) {
        this.value = s;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}