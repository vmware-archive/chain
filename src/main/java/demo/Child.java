package demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

interface Child {

    @JsonIgnore
    Node getParent();

    void setParent(Node parent);

    String getHash();
}
