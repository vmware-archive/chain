package demo.merkle;

public class MerkleException extends Exception {

    MerkleException(String message) {
        super(message);
    }

    MerkleException(Throwable t) {
        super(t);
    }
}