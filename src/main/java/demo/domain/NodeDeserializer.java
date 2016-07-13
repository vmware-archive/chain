package demo.domain;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import demo.MerkleException;

import java.io.IOException;

public class NodeDeserializer extends JsonDeserializer<Node> {

    @Override
    public Node deserialize(JsonParser jp, DeserializationContext ctxt) throws
            IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        Node n = new Node(root.get("hash").asText());
        if (root.get("left") != null) {
            Child left = mapper.treeToValue(root.get("left"), Child.class);
            try {
                n.addChild(left);
            } catch (MerkleException e) {
                throw new IOException(e);
            }
        }

        if (root.get("right") != null) {
            Child right = mapper.treeToValue(root.get("right"), Child.class);
            try {
                n.addChild(right);
            } catch (MerkleException e) {
                throw new IOException(e);
            }
        }

        return n;
    }
}