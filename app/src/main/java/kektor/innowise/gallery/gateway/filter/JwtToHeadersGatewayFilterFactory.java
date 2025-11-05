package kektor.innowise.gallery.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtToHeadersGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    public static final String HEADER_USERNAME = "X-User-Username";
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_EMAIL = "X-User-Email";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) ->
                exchange.getPrincipal()
                        .filter(principal -> principal instanceof JwtAuthenticationToken)
                        .cast(JwtAuthenticationToken.class)
                        .map(authentication -> {
                            Jwt jwt = authentication.getToken();
                            return exchange.getRequest().mutate()
                                    .header(HEADER_USERNAME, jwt.getSubject())
                                    .header(HEADER_USER_ID, jwt.getClaimAsString("user_id"))
                                    .header(HEADER_EMAIL, jwt.getClaimAsString("email"))
                                    .build();
                        })
                        .defaultIfEmpty(exchange.getRequest())
                        .flatMap(request -> chain.filter(exchange.mutate().request(request).build()));
    }
}
