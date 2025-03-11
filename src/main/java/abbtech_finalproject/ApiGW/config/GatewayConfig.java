package abbtech_finalproject.ApiGW.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user_service", r -> r.path("/api/users/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-service"))
                .route("hotel_service", r -> r.path("/api/hotels/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://hotel-service"))
                .route("booking_service", r -> r.path("/api/bookings/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://booking-service"))
                .route("notification_service", r -> r.path("/api/notifications/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://notification-service"))
                .build();
    }
}
