package demo.domain;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafDeserializer extends JsonDeserializer<Leaf> {

    public Map<String, Leaf> leaves = new HashMap();

    @Override
    public Leaf deserialize(JsonParser jp, DeserializationContext ctxt) throws
            IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        Leaf l = new Leaf(root.get("key").asText(), root.get("hash").asText());
        leaves.put(l.getKey(), l);

        return l;
    }
}