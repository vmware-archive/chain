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
public class MapBTest {

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";
    private static final String HASH_1 = "f67213b122a5d442d2b93bda8cc45c564a70ec5d2a4e0e95bb585cf199869c98";
    private static final String HASH_2 = "dec2e4bc4992314a9c9a51bbd859e1b081b74178818c53c19d18d6f761f5d804";

    @Autowired
    private MapB chain;

    @Test
    public void testChain() {
        Object key1 = chain.addEntry(ENTRY_1);
        assertNotNull(key1);
        assertTrue(chain.verify(ENTRY_1, key1.toString()));
        assertFalse(chain.verify("foo", key1.toString()));
        assertEquals(HASH_1, chain.getEntry(key1.toString()));

        Object key2 = chain.addEntry(ENTRY_2);
        assertNotNull(key2);
        assertTrue(chain.verify(ENTRY_2, key2.toString()));
        assertFalse(chain.verify("foo", key2.toString()));
        assertEquals(HASH_2, chain.getEntry(key2.toString()));
    }
}
