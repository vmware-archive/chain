package io.pivotal.cf.chain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HasherTest {

    @Autowired
    private Hasher hasher;

    @Test
    public void testHash() {
        String hash = hasher.hash(null);
        assertNull(hash);

        hash = hasher.hash("");
        assertNotNull(hash);
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);

        hash = hasher.hash("This is a test.");
        assertNotNull(hash);
        assertEquals("a8a2f6ebe286697c527eb35a58b5539532e9b3ae3b64d4eb0a46fb657b41562c", hash);
    }
}
