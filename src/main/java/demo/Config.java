package demo;

import demo.merkle.MerkleTree;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MerkleTree tree() {
        return new MerkleTree();
    }
}