package io;

import io.pivotal.cf.chain.Application;
import io.pivotal.cf.chain.Hasher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class HasherTest {

    @Autowired
    Hasher hasher;

    @Test
    public void testHash() {
        String hash = hasher.hashAndEncode(null);
        assertNull(hash);

        hash = hasher.hashAndEncode("");
        assertNotNull(hash);
        assertEquals("47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU", hash);

        hash = hasher.hashAndEncode("This is a test.");
        assertNotNull(hash);
        assertEquals("qKL26-KGaXxSfrNaWLVTlTLps647ZNTrCkb7ZXtBViw", hash);
    }
}
