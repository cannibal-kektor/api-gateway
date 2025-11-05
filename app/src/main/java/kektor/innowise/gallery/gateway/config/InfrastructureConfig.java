package kektor.innowise.gallery.gateway.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

import java.io.File;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Configuration
public class InfrastructureConfig {

    @Bean
    public ApplicationRunner startupHealthCheck() {
        return _ -> {
            File healthCheckFile = new File("/tmp/healthy");
            healthCheckFile.createNewFile();
            Runtime.getRuntime().addShutdownHook(new Thread(healthCheckFile::delete));
        };
    }

    @Bean
    @Profile("cds")
    @Scope(SCOPE_SINGLETON)
    public ReactiveJwtDecoder emptyReactiveJwtDecoder() {
        return _ -> Mono.empty();
    }

}
