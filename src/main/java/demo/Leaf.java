package demo;


class Leaf implements Child {

    private String value;
    private Node parent;

    Leaf(String value, Node parent) {
        this.value = value;
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public String getValue() {
        return value;
    }
}