package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TreeTest {

    @Autowired
    Tree tree;

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";
    private static final String ENTRY_3 = "test 3";
    private static final String ENTRY_4 = "test 4";
//
//    private static final String HASH_1 = "f67213b122a5d442d2b93bda8cc45c564a70ec5d2a4e0e95bb585cf199869c98";
//    private static final String HASH_2 = "dec2e4bc4992314a9c9a51bbd859e1b081b74178818c53c19d18d6f761f5d804";
//
//    @Autowired
//    private ChainA chain;

    @Test
    public void testTree() {
        assertFalse(tree.validate("1234"));
        assertFalse(tree.validate("1234", "foo"));

        String e1 = tree.addLeaf(ENTRY_1);
        assertNotNull(e1);
        assertTrue(tree.validate(e1));
        assertTrue(tree.validate());
        assertTrue(tree.validate(e1, ENTRY_1));
        assertFalse(tree.validate(e1, "foo"));

        String e2 = tree.addLeaf(ENTRY_2);
        assertNotNull(e2);
        assertTrue(tree.validate(e2));
        assertTrue(tree.validate());

        String e3 = tree.addLeaf(ENTRY_3);
        assertNotNull(e3);
        assertTrue(tree.validate(e3));
        assertTrue(tree.validate());

        String e4 = tree.addLeaf(ENTRY_4);
        assertNotNull(e4);
        assertTrue(tree.validate(e4));
        assertTrue(tree.validate());
    }
}
