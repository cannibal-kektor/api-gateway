package kektor.innowise.gallery.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Configuration
public class InfrastructureConfig {

    @Bean
    @Profile("smoke")
    @Scope(SCOPE_SINGLETON)
    public ReactiveJwtDecoder emptyReactiveJwtDecoder() {
        return _ -> Mono.empty();
    }

}
