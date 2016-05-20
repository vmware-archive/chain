package demo;

public class Node {

    Node left, right, parent;

    String hash;

    public Node(String hash, Node left, Node right, Node parent) {
        this();
        this.hash = hash;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    public Node() {
        super();
    }

}
