package io.pivotal.cf.chain;

public class MerkleException extends Exception {

    public MerkleException(String message) {
        super(message);
    }

    public MerkleException(Throwable t) {
        super(t);
    }
}