package demo;

public class Node implements BTree {

    String hash;
    BTree left;
    BTree right;

    public Node(String value, BTree left, BTree right) {
        this(value);
        this.left = left;
        this.right = right;
    }

    Node(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public BTree getLeft() {
        return left;
    }

    public BTree getRight() {
        return right;
    }
}
