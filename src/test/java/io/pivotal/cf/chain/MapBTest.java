package io.pivotal.cf.chain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MapBTest {

    private static final String ENTRY_1 = "test a";
    private static final String ENTRY_2 = "test b";
    private static final String HASH_1 = "1136b2eb6a1a50fc4ce4463e726ee7a5b8cdf8fbaa969aa83a78dd2beaa275eb";
    private static final String HASH_2 = "6346935ed39208ef8651fc772879acb7cd46382af5a968fdb0ad82759413f5fb";

    @Autowired
    private MapB mapB;

    @Test
    public void testMapB() {
        String key1 = mapB.put(ENTRY_1);
        assertNotNull(key1);
        assertTrue(mapB.verify(ENTRY_1, key1));
        assertFalse(mapB.verify("foo", key1));
        assertEquals(HASH_1, mapB.get(key1));

        String key2 = mapB.put(ENTRY_2);
        assertNotNull(key2);
        assertTrue(mapB.verify(ENTRY_2, key2));
        assertFalse(mapB.verify("foo", key2));
        assertEquals(HASH_2, mapB.get(key2));
    }
}
