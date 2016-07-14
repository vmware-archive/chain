package io.pivotal.cf.chain.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

class ChildTranslator extends JsonDeserializer<Child> {

    @Override
    public Child deserialize(JsonParser jp, DeserializationContext ctxt) throws
            IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        if (isNode(root)) {
            return mapper.treeToValue(root, Node.class);
        } else if (isLeaf(root)) {
            return mapper.treeToValue(root, Leaf.class);
        }

        throw new IOException("don't know how to deserialize this: " + root);
    }

    private boolean isNode(ObjectNode o) {
        return o.get("key") == null;
    }

    private boolean isLeaf(ObjectNode o) {
        return o.get("key") != null;
    }
}