package io;

import io.pivotal.cf.chain.Application;
import io.pivotal.cf.chain.Hasher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class HasherTest {

    @Test
    public void testHash() {
        String hash = Hasher.hashAndEncode(null);
        assertNull(hash);

        hash = Hasher.hashAndEncode("");
        assertNotNull(hash);
        assertEquals("47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU", hash);

        hash = Hasher.hashAndEncode("This is a test.");
        assertNotNull(hash);
        assertEquals("qKL26-KGaXxSfrNaWLVTlTLps647ZNTrCkb7ZXtBViw", hash);
    }
}
