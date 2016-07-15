package io.pivotal.cf.chain.domain;

import org.springframework.http.HttpStatus;

public class VerificationException extends Exception {

    private HttpStatus status;

    private Child child;

    public HttpStatus getStatus() {
        return status;
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Child getChild() {
        return child;
    }

    private void setChild(Child child) {
        this.child = child;
    }

    VerificationException(String message, Child child, HttpStatus status) {
        this(message, status);
        setChild(child);
    }

    VerificationException(String message, HttpStatus status) {
        super(message);
        setStatus(status);
    }
}