package demo.domain;

import demo.Application;
import demo.MerkleException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ChainTest {

    @Autowired
    private Chain chain;

    @Autowired
    private MerkleTree merkleTree;

    @Test
    public void testChain() throws MerkleException {
        chain.clear();
        assertTrue(chain.size() == 0);

        merkleTree.loadRandomEntries(10);
        chain.addBlock(merkleTree);
        assertTrue(chain.verify());
        assertTrue(chain.size() == 10);

        merkleTree.loadRandomEntries(15);
        chain.addBlock(merkleTree);
        assertTrue(chain.verify());
        assertTrue(chain.size() == 25);
    }
}
