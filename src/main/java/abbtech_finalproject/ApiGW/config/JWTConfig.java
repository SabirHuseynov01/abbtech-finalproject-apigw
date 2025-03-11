package abbtech_finalproject.ApiGW.config;

import abbtech_finalproject.ApiGW.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider("secret-key-please-update");
    }
}
