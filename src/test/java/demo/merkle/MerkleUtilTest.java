package demo.merkle;

import demo.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MerkleUtilTest {

    @Autowired
    private MerkleUtil merkleUtil;

    @Test
    public void testCalcPath() {
        char[] c1 = merkleUtil.calcPath(9);
        assertNotNull(c1);
    }

    @Test
    public void testImport() throws IOException {

        try {
            MerkleTree mt = merkleUtil.load(getContents("valid.json"));
            assertNotNull(mt.getRoot());
            assertTrue(mt.verify());
        } catch (MerkleException e) {
            fail("should not have thrown an exception.");
        }

        try {
            MerkleTree mt2 = merkleUtil.load(getContents("invalidLeaf.json"));
            assertNotNull(mt2.getRoot());
            assertFalse(mt2.verify());
            fail("should have thrown an exception.");
        } catch (MerkleException e) {
            //wrong
        }

        try {
            MerkleTree mt3 = merkleUtil.load(getContents("invalidNode.json"));
            assertNotNull(mt3.getRoot());
            assertFalse(mt3.verify());
            fail("should have thrown an exception.");
        } catch (MerkleException e) {
            //wrong
        }
    }

    private String getContents(String fileName) throws IOException {
        URI u = new ClassPathResource(fileName).getURI();
        return new String(Files.readAllBytes(Paths.get(u)));
    }
}
