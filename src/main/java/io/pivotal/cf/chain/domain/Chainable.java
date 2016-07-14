package io.pivotal.cf.chain.domain;

import io.pivotal.cf.chain.MerkleException;

public interface Chainable {

    boolean verify() throws MerkleException;

    boolean verify(String key, String entry) throws MerkleException;

    void clear();

    int size();

    Leaf get(String key);

    String getHash();
}