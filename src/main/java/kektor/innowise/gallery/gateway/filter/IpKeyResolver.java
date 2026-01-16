package kektor.innowise.gallery.gateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * This bean is used by {@link org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory}
 */
@Component
public class IpKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        Optional<String> host = Optional.of(exchange.getRequest())
                .map(ServerHttpRequest::getRemoteAddress)
                .map(InetSocketAddress::getHostString);
        return Mono.justOrEmpty(host);
    }

}
