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

/**
 * Created by jared on 5/18/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Chain1Test {

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";

    private static final String HASH_1 = "f67213b122a5d442d2b93bda8cc45c564a70ec5d2a4e0e95bb585cf199869c98";
    private static final String HASH_2 = "dec2e4bc4992314a9c9a51bbd859e1b081b74178818c53c19d18d6f761f5d804";

    @Autowired
    private Chain1 chain;

    @Test
    public void testChain() {
        String hash1 = chain.addEntry(ENTRY_1);
        assertNotNull(hash1);
        assertEquals(HASH_1, hash1);

        String hash2 = chain.addEntry("test 2");
        assertNotNull(hash2);
        assertEquals(HASH_2, hash2);

        assertTrue(chain.verify(ENTRY_1, HASH_1));
        assertTrue(chain.verify(ENTRY_2, HASH_2));

        assertFalse(chain.verify("foo", HASH_1));
        assertFalse(chain.verify("foo", HASH_2));
    }
}
