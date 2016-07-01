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
public class MapATest {

    private static final String ENTRY_1 = "test 1";
    private static final String ENTRY_2 = "test 2";

    private static final String HASH_1 = "9nITsSKl1ELSuTvajMRcVkpw7F0qTg6Vu1hc8ZmGnJg=";
    private static final String HASH_2 = "3sLkvEmSMUqcmlG72FnhsIG3QXiBjFPBnRjW92H12AQ=";

    @Autowired
    private MapA mapA;

    @Test
    public void testMapA() {

        Object hash1 = mapA.addEntry(ENTRY_1);
        assertNotNull(hash1);
        assertEquals(HASH_1, hash1);
        assertEquals(ENTRY_1, mapA.getEntry(HASH_1));

        Object hash2 = mapA.addEntry("test 2");
        assertNotNull(hash2);
        assertEquals(HASH_2, hash2);
        assertEquals(ENTRY_2, mapA.getEntry(HASH_2));

        assertTrue(mapA.verify(ENTRY_1, HASH_1));
        assertTrue(mapA.verify(ENTRY_2, HASH_2));

        assertFalse(mapA.verify("foo", HASH_1));
        assertFalse(mapA.verify("foo", HASH_2));
    }
}
