package org.example.CMS.security;

import lombok.extern.slf4j.Slf4j;
import org.example.CMS.configuration.IpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
@Order(2)
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final JwtTokenFilter authenticationJwtTokenFilter;

    private final IpConfiguration ipConfiguration;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationEntryPoint unauthorizedHandler,
                                 JwtTokenFilter authenticationJwtTokenFilter,
                                 IpConfiguration ipConfiguration) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;
        this.ipConfiguration = ipConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String ipFilter = createIpFilter(ipConfiguration.getWhitelistedIps());
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
//                .access(ipFilter)
                .anyRequest().authenticated();
        http.cors().and().csrf().disable();

        http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private String createIpFilter(String IPs){
        IPs = IPs.replaceAll(" ", "");
        List<String> ipList = Arrays.asList(IPs.split(","));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ipList.size(); i++) {
            if(i==0) sb.append("hasIpAddress('").append(ipList.get(i)).append("/32')");
            else sb.append(" or hasIpAddress('").append(ipList.get(i)).append("/32')");
        }
        return sb.toString();
    }
}

