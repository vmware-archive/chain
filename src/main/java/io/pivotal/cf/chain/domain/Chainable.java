package io.pivotal.cf.chain.domain;

public interface Chainable {

    void verify() throws VerificationException;

    void verify(String key, String entry) throws VerificationException;

    void clear();

    int size();

    Leaf get(String key);

    String getHash();
}