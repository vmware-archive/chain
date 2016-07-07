package demo.merkle;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

class MerkleUtil {

    static MerkleTree load(String json) throws MerkleException {

        MerkleTree mt = new MerkleTree();
        Child n;

        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Leaf.class, new LeafDeserializer(mt));
            module.addDeserializer(Node.class, new NodeDeserializer(mt));
            module.addDeserializer(Child.class, new ChildDeserializer(mt));

            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jp = jsonFactory.createParser(json);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            jp.setCodec(mapper);
            TreeNode jsonNode = jp.readValueAsTree().get("root");

            String s = jsonNode.toString();

            n = mapper.readValue(s, Child.class);
        } catch (IOException e) {
            throw new MerkleException(e);
        }

        mt.setRoot(n);
        return mt;
    }
}
