package io.pivotal.cf.chain.domain;

import io.pivotal.cf.chain.Hasher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MerkleTreeTest {

    @Autowired
    private MerkleTree tree;

    @Autowired
    Hasher hasher;

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";
    private static final String ENTRY_3 = "test 3";
    private static final String ENTRY_4 = "test 4";

    @Test
    public void testTree() throws VerificationException {
        tree.clear();

        try {
            tree.verify("1234", "foo");
            fail("expected exception");
        } catch (VerificationException e) {
            //expected
        }

        String e1 = tree.addEntry(ENTRY_1);
        assertNotNull(e1);
        assertNotNull(tree.get(e1));

        try {
            tree.verify(e1, ENTRY_1);
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            tree.verify(e1, "foo");
            fail("expected exception");
        } catch (VerificationException e) {
            assertNotNull(e.getMessage());
            assertEquals(HttpStatus.CONFLICT, e.getStatus());
        }

        try {
            tree.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        String e2 = tree.addEntry(ENTRY_2);
        assertNotNull(e2);

        try {
            tree.verify(e2, ENTRY_2);
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            tree.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertNotNull(tree.getRoot());

        String e3 = tree.addEntry(ENTRY_3);
        assertNotNull(e3);

        try {
            tree.verify(e3, ENTRY_3);
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            tree.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertNotNull(tree.getRoot());

        String e4 = tree.addEntry(ENTRY_4);
        assertNotNull(e4);

        try {
            tree.verify(e4, ENTRY_4);
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            tree.verify(e4, "foo");
            fail("expected exception");
        } catch (VerificationException e) {
            //expected
        }

        try {
            tree.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertNotNull(tree.getRoot());
    }

    @Test
    public void testLoadToManyEntries() {
        tree.clear();
        try {
            tree.loadRandomEntries(MerkleTree.MAX_MERKLE_ENTRIES);
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            tree.loadRandomEntries(1);
        } catch (VerificationException e) {
            //expected
        }

        tree.clear();
    }

    @Test
    public void testLevels() throws VerificationException {
        tree.clear();

        assertEquals(0, tree.height());

        tree.addEntry("1");
        assertEquals(0, tree.height());

        tree.addEntry("2");
        assertEquals(1, tree.height());

        tree.addEntry("3");
        assertEquals(2, tree.height());

        tree.addEntry("4");
        assertEquals(2, tree.height());

        tree.addEntry("5");
        assertEquals(3, tree.height());
    }

    @Test
    public void testValidation() throws VerificationException {
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

        try {
            tree.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        for (String key : m.keySet()) {
            try {
                tree.verify(key, m.get(key));
            } catch (VerificationException e) {
                fail("should not have thrown an exception.");
            }
        }
    }

    @Test
    public void testCalcPath() {
        char[] c1 = tree.calcPath(9);
        assertNotNull(c1);
    }

    @Test
    public void testImport() throws IOException {
        try {
            Chainable c = tree.load(getContents("valid.json"));
            assertNotNull(c.getHash());
            c.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            Chainable c = tree.load(getContents("invalidLeaf.json"));
            assertNotNull(c.getHash());
            c.verify();
            fail("should have thrown an exception.");
        } catch (VerificationException e) {
            assertNotNull(e.getMessage());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, e.getStatus());
        }

        try {
            Chainable c = tree.load(getContents("invalidNode.json"));
            assertNotNull(c.getHash());
            c.verify();
            fail("should have thrown an exception.");
        } catch (VerificationException e) {
            assertNotNull(e.getMessage());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, e.getStatus());
        }
    }

    private String getContents(String fileName) throws IOException {
        URI u = new ClassPathResource(fileName).getURI();
        return new String(Files.readAllBytes(Paths.get(u)));
    }
}
