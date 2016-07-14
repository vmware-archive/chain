package io.pivotal.cf.chain;

import io.pivotal.cf.chain.domain.Chain;
import io.pivotal.cf.chain.domain.MerkleTree;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MerkleTree tree() {
        return new MerkleTree();
    }

    @Bean
    public Chain chain() {
        return new Chain();
    }
}