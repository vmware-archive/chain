package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class MerkleTree {

    @Autowired
    private Hasher hasher;

    private final Map<String, Leaf> leaves = new HashMap<>();

    private Node root;

    private Node lastAdded;

    void clear() {
        leaves.clear();
        root = null;
        lastAdded = null;
    }

    Child getEntry(String key) {
        return leaves.get(key);
    }

    Node all() {
        print();
        return root;
    }

    String addEntry(String entry) {

        //hash the entry
        String hash = hasher.hash(entry);

        //create a new node for the leaf
        Node node = new Node();
        node.setValue(hash);

        //create guid for leaf entry
        String key = UUID.randomUUID().toString();

        //create a leaf for the entry
        Leaf leaf = new Leaf();
        leaf.setValue(key);
        node.addChild(leaf);

        //add the node to the tree
        addNode(node);

        //add the leaf to the leaves collection
        leaves.put(key, leaf);

        //return the key to the new entry
        return key;
    }

    private void addNode(Node n) {
        //is there a root?
        if (root == null) {
            root = n;
        } else if (treeIsFull()) {
            //we need a new root
            addNewLevel(n);
        } else {

            //get an open slot to put this into
            Node parent = findNextParent();

            //add the new node to the tree
            parent.addChild(n);
        }

        //calculate hashes from newly added node up to root
        rehash(n);

        //hang onto the node for the next round
        lastAdded = n;
    }

    private boolean treeIsFull() {
        return isPowerOf2(leaves.size());
    }

    private boolean isPowerOf2(int i) {
        return (i != 0) && ((i & (i - 1)) == 0);
    }

    int levels() {
        return levels(leaves.size());
    }

    private int levels(int i) {
        if (i <= 0) {
            return 0;
        }

        //calculate base2 log of the number
        double d = Math.log(i) / Math.log(2);

        //round up to next whole number, and add 1 to represent root
        return (int) Math.ceil(d) + 1;
    }

    private void addNewLevel(Node node) {
        //create a new node branch n levels deep
        Node newRoot = createBranch(node, levels());

        //swap the new root's left to its right
        newRoot.setRight(newRoot.getLeft());

        //put the old root in the left of the newRoot
        newRoot.setLeft(root);

        root = newRoot;
    }

    private Node createBranch(Node n, int levels) {
        Node parent;
        Node child = n;
        for (int i = 0; i < levels; i++) {
            parent = new Node();
            parent.addChild(child);

            child = parent;
        }

        return child;
    }

    private Node findNextParent() {
        return findNextOpening(lastAdded);
    }

    private Node findNextOpening(Node start) {

        Node n = start;
        int level = 0;
        while (n.getParent().getRight() != null) {
            n = n.getParent();
            level++;
            if (n.equals(root)) {
                throw new RuntimeException("hit root, not supposed to happen!");
            }
        }

        //if we'e at the lowest level, return the parent of this node
        if (level == 0) {
            return n.getParent();
        }

        //otherwise we're at a higher level,
        // Fill in nodes from here to one above the leaf level.
        Node bottom = new Node();
        Node top = createBranch(bottom, level - 1);

        n.getParent().addChild(top);

        return bottom;
    }

    private void rehash(Node node) {
        //node has no sibling, just inherit child hash
        if (node.getRight() == null) {
            node.setValue(node.getLeft().getValue());
        } else {
            //set hash to the hash of the concatenated child hashes
            node.setValue(hasher.hash(node.getLeft().getValue() + node.getRight().getValue()));
        }

        if (!root.equals(node)) {
            //keep going if we are not at the root yet
            rehash(node.getParent());
        }
    }

    //is this the right entry for this id?
    boolean verify(String id, String entry) {
        Leaf leaf = leaves.get(id);
        if (leaf == null) {
            return false;
        }

        return leaf.getParent().equals(hasher.hash(entry));
    }

    void print() {
        System.out.println("\n");
        printContentsIndented("  ", root);
    }

    private void printContentsIndented(String indent, Child c) {
        if (c instanceof Leaf) {
            System.out.println(indent + "Leaf: value = " + c.getValue());
        } else {
            Node n = (Node) c;
            System.out.println(indent + "Node--------");
            String new_indent = indent + "| ";
            // the  toString  method concocts a string value for an object:
            System.out.println(new_indent + "value = " + n.getValue());

            if (n.getLeft() != null) {
                System.out.println(new_indent + "left =");
                printContentsIndented(new_indent + "  ", n.getLeft());
            }

            if (n.getRight() != null) {
                System.out.println(new_indent + "right =");
                printContentsIndented(new_indent + "  ", n.getRight());
            }
        }
    }

//    //validate the tree, from the specified leaf up to the root
//    public boolean validate(String id) {
//        Node leaf = leaves.get(id);
//        if(leaf == null) {
//            return false;
//        }
//
//        //special case where there is only one entry
//        if(root.equals(leaf)) {
//            return true;
//        }
//
//        return validate(leaf);

    //climb the tree and validate path to the root node
//        Node leaf = leaves.get(id);
//        if(leaf == null) {
//            return false;
//        }

//        return false;
//    }

//    private boolean validate(Node node) {
//        //special case, only one entry
//        if(root.equals(node)) {
//            return true;
//        }
//
//        //if no left and right, then its a leaf, go up a level
//        if(node.left == null && node.right == null) {
//            return validate(node.parent);
//        }
//
//        //concatenate and hash left and right, compare to hash
//        if( hasher.hash(node.left.hash + node.right.hash).equals(node.hash)) {
//            //node hash is good, are we at the root?
//            if(root.equals(node)) {
//                return true;
//            }
//            else {
//                return validate(node.parent);
//            }
//        }
//        //node hash was bad
//        return false;
//    }
//
//    public boolean validate() {
//        for(Node leaf : leaves.values()) {
//            if( ! validate(leaf) ) {
//                return false;
//            }
//        }
//        return true;
//    }
}
