package demo.merkle;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import demo.Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MerkleUtil {

    @Autowired
    private Hasher hasher;

    public MerkleTree load(String json) throws MerkleException {

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

    public boolean isPowerOf2(int i) {
        return (i != 0) && ((i & (i - 1)) == 0);
    }

    public int levels(int size) {
        if (size <= 0) {
            return 0;
        }

        //calculate base2 log
        double d = Math.log(size) / Math.log(2);

        //round up to next whole number
        return (int) Math.ceil(d);
    }

    public void loadRandomEntries(String numberOfEntries, MerkleTree tree) throws MerkleException {
        int i = 0;
        try {
            i = Integer.parseInt(numberOfEntries);
        } catch (NumberFormatException e) {
            //ignore
        }

        for (int j = 0; j < i; j++) {
            tree.addEntry(hasher.createId());
        }
    }
}
