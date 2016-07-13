package demo.domain;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

class LeafDeserializer extends JsonDeserializer<Leaf> {

    private MerkleTree tree;

    LeafDeserializer(MerkleTree mt) {
        this.tree = mt;
    }

    @Override
    public Leaf deserialize(JsonParser jp, DeserializationContext ctxt) throws
            IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        Leaf l = new Leaf(root.get("key").asText(), root.get("hash").asText());
        tree.put(l);

        return l;
    }
}