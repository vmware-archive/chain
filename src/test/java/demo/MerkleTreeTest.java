package demo;

import org.junit.Ignore;
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
public class MerkleTreeTest {

    @Autowired
    private MerkleTree tree;

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";
    private static final String ENTRY_3 = "test 3";
    private static final String ENTRY_4 = "test 4";

    @Test
    @Ignore
    public void testTree() {
//        assertFalse(tree.validate("1234"));
        assertFalse(tree.verify("1234", "foo"));

        String e1 = tree.addEntry(ENTRY_1);
        assertNotNull(e1);

      //  tree.print();


//        assertTrue(tree.validate(e1));
//        assertTrue(tree.validate());
//        assertTrue(tree.validate(e1, ENTRY_1));
//        assertFalse(tree.validate(e1, "foo"));

        String e2 = tree.addEntry(ENTRY_2);
        assertNotNull(e2);

    //    tree.print();
//        assertTrue(tree.validate(e2));
//        assertTrue(tree.validate());

        String e3 = tree.addEntry(ENTRY_3);
        assertNotNull(e3);

     //   tree.print();
//        assertTrue(tree.validate(e3));
//        assertTrue(tree.validate());

        String e4 = tree.addEntry(ENTRY_4);
        assertNotNull(e4);

  //      tree.print();

//        assertTrue(tree.validate(e4));
//        assertTrue(tree.validate());
    }

    @Test
    public void testLevels() {
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
}
