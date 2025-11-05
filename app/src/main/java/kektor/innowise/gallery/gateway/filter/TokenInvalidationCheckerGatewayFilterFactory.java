package kektor.innowise.gallery.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * This filter monitors Redis for jti (jwt token ids) in keys blacklist:%s .
 * Authentication Server is responsible for filling them when invalidates tokens
 * (during logout or manual invalidation)
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInvalidationCheckerGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final String KEY_TEMPLATE = "blacklist:%s";

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) ->
                exchange.getPrincipal()
                        .filter(principal -> principal instanceof JwtAuthenticationToken)
                        .cast(JwtAuthenticationToken.class)
                        .flatMap(token ->
                                redisTemplate.hasKey(KEY_TEMPLATE.formatted(token.getToken().getId())))
                        .onErrorResume( e -> {
                            log.error("Error while checking Redis", e);
                            return Mono.just(false);
                        })
                        .defaultIfEmpty(false)
                        .flatMap(revoked -> {
                            if (revoked) {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return Mono.error(()-> new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_TOKEN));
                            }
                            return chain.filter(exchange);
                        });
    }

}
