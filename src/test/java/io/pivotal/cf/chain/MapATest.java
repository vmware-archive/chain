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
public class MapATest {

    private static final String ENTRY_1 = "test one";
    private static final String ENTRY_2 = "test two";

    private static final String HASH_1 = "d8a8cc528ed16ade99b0c20e4d7b89ef4bfe7528a52bfdec857aaaafb53b07bb";
    private static final String HASH_2 = "1fe58fbdb608735bfb96b974f71f8951a8986dc6593d3c95b2a59e068384b122";

    @Autowired
    private MapA mapA;

    @Test
    public void testMapA() {

        Object hash1 = mapA.put(ENTRY_1);
        assertNotNull(hash1);
        assertEquals(HASH_1, hash1);
        assertEquals(ENTRY_1, mapA.get(HASH_1));

        Object hash2 = mapA.put(ENTRY_2);
        assertNotNull(hash2);
        assertEquals(HASH_2, hash2);
        assertEquals(ENTRY_2, mapA.get(HASH_2));

        assertTrue(mapA.verify(ENTRY_1, HASH_1));
        assertTrue(mapA.verify(ENTRY_2, HASH_2));

        assertFalse(mapA.verify("foo", HASH_1));
        assertFalse(mapA.verify("foo", HASH_2));
    }
}
