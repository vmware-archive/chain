package io.pivotal.cf.chain;

import io.pivotal.cf.chain.domain.Chain;
import io.pivotal.cf.chain.domain.MerkleTree;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MerkleTree tree(Hasher hasher) {
        return new MerkleTree(hasher);
    }

    @Bean
    public Chain chain(Hasher hasher) {
        return new Chain(hasher);
    }

    @Bean
    Hasher hasher() {
        return new Hasher();
    }
}