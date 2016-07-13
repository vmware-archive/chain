package demo.domain;

import demo.MerkleException;

interface Chainable {

    boolean verify() throws MerkleException;

    boolean verify(String key, String entry) throws MerkleException;

    void clear();

    int size();
}