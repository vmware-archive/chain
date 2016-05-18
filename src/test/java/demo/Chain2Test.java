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
public class Chain2Test {

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";

    @Autowired
    private Chain1 chain;

    @Test
    public void testChain() {
        String key1 = chain.addEntry(ENTRY_1);
        assertNotNull(key1);
        assertTrue(chain.verify(ENTRY_1, key1));
        assertFalse(chain.verify("foo", key1));

        String key2 = chain.addEntry(ENTRY_2);
        assertNotNull(key2);
        assertTrue(chain.verify(ENTRY_2, key2));
        assertFalse(chain.verify("foo", key2));
    }
}
