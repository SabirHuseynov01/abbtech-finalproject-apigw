package abbtech_finalproject.ApiGW;

import abbtech_finalproject.ApiGW.filter.JwtAuthenticationFilter;
import abbtech_finalproject.ApiGW.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtAuthenticationFilterTest {

	private JwtAuthenticationFilter filter;
	private JwtTokenProvider jwtTokenProvider;
	private GatewayFilterChain chain;

	@BeforeEach
	public void setUp() {
		jwtTokenProvider = mock(JwtTokenProvider.class);
		filter = new JwtAuthenticationFilter();
		filter.jwtTokenProvider = jwtTokenProvider;
		chain = mock(GatewayFilterChain.class);
		when(chain.filter(any())).thenReturn(Mono.empty());
	}

	@Test
	public void testValidToken() {
		MockServerHttpRequest request = MockServerHttpRequest
				.get("/api/users/profile")
				.header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);

		filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange,chain);

		verify(chain, times(1)).filter(exchange);
		assertEquals(HttpStatus.OK, exchange.getResponse().getStatusCode());
	}

	@Test
	public void testNoToken() {
		MockServerHttpRequest request = MockServerHttpRequest
				.get("/api/users/profile")
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange,chain);

		verify(chain, never()).filter(exchange);
		assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
	}

	@Test
	public void testInvalidToken() {
		MockServerHttpRequest request = MockServerHttpRequest
				.get("/api/users/profile")
				.header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

		filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, chain);

		verify(chain, never()).filter(exchange);
		assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
	}

}
