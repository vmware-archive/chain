package demo.merkle;

import demo.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MerkleTreeTest {

    @Autowired
    private MerkleTree tree;

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";
    private static final String ENTRY_3 = "test 3";
    private static final String ENTRY_4 = "test 4";

    @Test
    public void testTree() throws MerkleException {
        tree.clear();

        try {
            tree.verify("1234", "foo");
            fail("expected exception");
        } catch (MerkleException e) {
            //expected
        }

        String e1 = tree.addEntry(ENTRY_1);
        assertNotNull(e1);
        assertNotNull(tree.getEntry(e1));
        assertTrue(tree.verify(e1, ENTRY_1));

        try {
            tree.verify(e1, "foo");
            fail("expected exception");
        } catch (MerkleException e) {
            //expected
        }

        assertTrue(tree.verify());

        String e2 = tree.addEntry(ENTRY_2);
        assertNotNull(e2);
        assertTrue(tree.verify(e2, ENTRY_2));
        assertTrue(tree.verify());
        assertNotNull(tree.getRoot());

        String e3 = tree.addEntry(ENTRY_3);
        assertNotNull(e3);
        assertTrue(tree.verify(e3, ENTRY_3));
        assertTrue(tree.verify());
        assertNotNull(tree.getRoot());

        String e4 = tree.addEntry(ENTRY_4);
        assertNotNull(e4);
        assertTrue(tree.verify(e4, ENTRY_4));

        try {
            tree.verify(e4, "foo");
            fail("expected exception");
        } catch (MerkleException e) {
            //expected
        }

        assertTrue(tree.verify());
        assertNotNull(tree.getRoot());
    }

    @Test
    public void testLevels() throws MerkleException {
        tree.clear();

        assertEquals(0, tree.levels());

        tree.addEntry("1");
        assertEquals(0, tree.levels());

        tree.addEntry("2");
        assertEquals(1, tree.levels());

        tree.addEntry("3");
        assertEquals(2, tree.levels());

        tree.addEntry("4");
        assertEquals(2, tree.levels());

        tree.addEntry("5");
        assertEquals(3, tree.levels());
    }

    @Test
    public void testValidation() throws MerkleException {
        tree.clear();
        List<String> entries = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            entries.add(UUID.randomUUID().toString());
        }

        Map<String, String> m = new HashMap<>();
        for (String entry : entries) {
            String key = tree.addEntry(entry);
            m.put(key, entry);
        }

        tree.verify();
        for (String key : m.keySet()) {
            assertTrue(tree.verify(key, m.get(key)));
        }
    }
}
