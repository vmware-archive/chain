# chain is no longer actively maintained by VMware.

# chain

Application to demonstrate technologies underlying blockchains.

```bash
git clone git@github.com:cf-platform-eng/chain.git
cd chain
mvn clean install
mvn spring-boot:run
```

## MapA
Simple map of entries and hashes. Not very useful...

* add an entry: [http://chain.cfapps.io/mapA/add/anEntry] returns the hash for the added entry
* find an entry: [http://chain.cfapps.io/mapA/find/aHash] returns the entry for the given hash
* verify an entry: [http://chain.cfapps.io/mapA/verify?entry=anEntry&hash=aHash] returns true or false
* see the contents of the map: [http://chain.cfapps.io/mapA]

## MapB
Same as above, except instead of caching values and hashes, it returns a key for future use. Better than MapA because it does not store entries (more secure, less space needed) but still not that useful (can be tampered with).

* add an entry: [http://chain.cfapps.io/mapB/add/anEntry] returns the key for the added entry
* find an entry: [http://chain.cfapps.io/mapB/find/aKey] returns the hash for the given key
* verify an entry: [http://chain.cfapps.io/mapB/verify?entry=anEntry&key=aKey] returns true or false
* see the contents of the map: [http://chain.cfapps.io/mapB]

## MerkleTree
Data structure used to store hashes in a efficient, tamper-proof manner. For more information about merkle trees, see [https://en.wikipedia.org/wiki/Merkle_tree]. This tree is implemented as a "perfect binary tree" where leaves are all at the same level. This lets us use some interesting mathematical properties to manage the tree.

* add an entry: [http://chain.cfapps.io/merkleTree/add/anEntry] returns the key for the added entry
* find an entry: [http://chain.cfapps.io/merkleTree/find/aKey] returns the leaf entry from the merkle tree for the given key
* see the contents of the tree: [http://chain.cfapps.io/merkleTree]
* load some random entries into the tree: [http://chain.cfapps.io/merkleTree/load/anInt] adds the specified number of entries to the tree. How many can be added? Tree is held in memory so there are some practical limits here.
* clear all entries from the tree: [http://chain.cfapps.io/merkleTree/clear]
* verify an entry: [http://chain.cfapps.io/merkleTree/verify?key=aKey&entry=anEntry] returns true if the entry is valid, outputs an exception otherwise
* verify the tree: [http://chain.cfapps.io/merkleTree/verify] returns true if the tree hashes are correct, outputs first invalid entry it finds otherwise

## Chain
A data structure that is made up of merkle trees. Represents a simplified implementation of a public blockchain.

* add a block to the chain: [http://chain.cfapps.io/chain/addBlock] moves the current contents of the merkle tree to the chain and empties out the merkle tree for the next cadence
* get an entry from the chain: [http://chain.cfapps.io/chain/find/aKey] returns the leaf entry from the chain for the given key
* see the contents of the chain: [http://chain.cfapps.io/chain]
* clear all entries from the chain: [http://chain.cfapps.io/chain/clear]
* verify an entry: [http://chain.cfapps.io/chain/verify?key=aKey&entry=anEntry] returns true if the entry is valid, outputs an exception otherwise
* verify the chain: [http://chain.cfapps.io/chain/verify] returns true if the chain hashes are correct, outputs first invalid entry it finds otherwise

## Coming Soon?
* A spiffy UI for viewing the tree and chain
* Post functionality to load non-trivial entries onto the tree
* Post functionality to import json into the tree

## Typical use case
1. chain/clear the chain
1. merkleTree/clear the tree
1. merkleTree/add an entry to the tree
1. merkleTree/load some additional entries onto the tree
1. merkleTree/verify the entry you added
1. merkleTree/verify the tree
1. view the tree
1. chain/addBlock to move the tree to the chain
1. chain/verify the entry you loaded to the tree
1. chain/verify the chain
1. add more stuff to the tree ...
1. addBlock more stuff to the chain ...
1. view the chain ...
1. chain/clear the chain
1. merkleTree/clear the tree
