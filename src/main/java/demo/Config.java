package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Hasher hasher() {
        return new Hasher();
    }

    @Bean
    BlockChain tree() {
        return new BlockChain();
    }
}
