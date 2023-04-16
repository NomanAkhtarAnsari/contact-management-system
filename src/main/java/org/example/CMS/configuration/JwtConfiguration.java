package org.example.CMS.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("jwt-configuration")
public class JwtConfiguration {

    public String jwtSecret;
    public int jwtTokenValidity;
}
