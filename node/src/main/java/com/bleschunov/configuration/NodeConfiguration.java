package com.bleschunov.configuration;

import com.bleschunov.util.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bleschunov Dmitry
 */
@Configuration
public class NodeConfiguration {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool cryptoTool() {
        return new CryptoTool(salt);
    }
}
