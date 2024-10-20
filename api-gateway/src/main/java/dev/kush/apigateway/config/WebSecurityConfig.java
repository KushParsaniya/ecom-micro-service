package dev.kush.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Configuration
public class WebSecurityConfig {

    private final String[] freeResourceUrls = {"webjars/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/api-docs/**", "/aggregate/**"};

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
                .authorizeExchange(
                        exchange -> exchange
                                .pathMatchers(freeResourceUrls).permitAll()
                                .pathMatchers(HttpMethod.GET, "/products/**").hasAnyAuthority("ADMIN", "USER")
                                .pathMatchers(HttpMethod.POST, "/products/**").hasAuthority("ADMIN")
                                .anyExchange().authenticated())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(configurationSource()))
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    // blocking
    //    @Bean
//    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
//        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//            var authorities = new HashSet<GrantedAuthority>();
//
//            // Extract roles from resource_access for the specific client
//            var resourceAccess = jwt.getClaimAsMap("resource_access");
//            if (resourceAccess != null && resourceAccess.containsKey("microservice01")) {
//                var clientRoles = (Map<String, Object>) resourceAccess.get("microservice01");
//                if (clientRoles.containsKey("roles")) {
//                    var roles = (List<String>) clientRoles.get("roles");
//                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
//                }
//            }
//            return Flux.fromIterable(authorities).collectList().flatMapMany(Flux::fromIterable);
//        });
//        return converter;
//    }
    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            return Mono.justOrEmpty(jwt.getClaimAsMap("resource_access"))
                    .flatMapMany(resourceAccess -> {
                        if (resourceAccess != null && resourceAccess.containsKey("microservice01")) {
                            var clientRoles = (Map<String, Object>) resourceAccess.get("microservice01");
                            if (clientRoles.containsKey("roles")) {
                                var roles = (List<String>) clientRoles.get("roles");
                                return Flux.fromIterable(roles)
                                        .map(role -> new SimpleGrantedAuthority(role))
                                        .collectList()
                                        .flatMapMany(Flux::fromIterable);
                            }
                        }
                        return Flux.empty();
                    });
        });
        return converter;
    }


    @Bean
    CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
