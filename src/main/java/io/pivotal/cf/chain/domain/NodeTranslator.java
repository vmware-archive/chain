package io.pivotal.cf.chain.domain;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

class NodeTranslator extends JsonDeserializer<Node> {

    @Override
    public Node deserialize(JsonParser jp, DeserializationContext ctxt) throws
            IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        Node n = new Node(root.get("hash").asText());
        if (root.get("left") != null) {
            Child left = mapper.treeToValue(root.get("left"), Child.class);
            n.addChild(left);
        }

        if (root.get("right") != null) {
            Child right = mapper.treeToValue(root.get("right"), Child.class);
            n.addChild(right);
        }

        return n;
    }
}