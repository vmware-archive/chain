package io.pivotal.cf.chain.domain;

import io.pivotal.cf.chain.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ChainTest {

    @Autowired
    private Chain chain;

    @Autowired
    private MerkleTree merkleTree;

    @Test
    public void testChain() {
        chain.clear();
        assertTrue(chain.size() == 0);

        merkleTree.loadRandomEntries(10);
        chain.addBlock(merkleTree);

        try {
            chain.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertTrue(chain.size() == 10);

        merkleTree.loadRandomEntries(15);
        chain.addBlock(merkleTree);

        try {
            chain.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertTrue(chain.size() == 25);
    }
}
