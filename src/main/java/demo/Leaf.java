package demo;

class Leaf implements Child {

    private String key;
    private String hash;
    private Node parent;

    public Leaf(String key, String hash) {
        setKey(key);
        setHash(hash);
    }

    public String getKey() {
        return key;
    }

    private void setKey(String s) {
        this.key = s;
    }

    public String getHash() {
        return hash;
    }

    private void setHash(String s) {
        this.hash = s;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}