package demo;

import demo.domain.Chain;
import demo.domain.MerkleTree;
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