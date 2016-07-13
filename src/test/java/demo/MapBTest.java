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
    private static final String HASH_1 = "9nITsSKl1ELSuTvajMRcVkpw7F0qTg6Vu1hc8ZmGnJg";
    private static final String HASH_2 = "3sLkvEmSMUqcmlG72FnhsIG3QXiBjFPBnRjW92H12AQ";

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
