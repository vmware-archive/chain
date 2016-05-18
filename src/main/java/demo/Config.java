package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jared on 5/18/16.
 */

@Configuration
public class Config {

    @Bean
    public Hasher hasher() {
        return new Hasher();
    }

    @Bean
    public Chain1 chain() {
        return new Chain1();
    }

}
