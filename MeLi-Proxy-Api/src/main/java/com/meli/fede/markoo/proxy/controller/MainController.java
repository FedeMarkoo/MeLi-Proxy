package com.meli.fede.markoo.proxy.controller;

import com.meli.fede.markoo.proxy.access.manager.AccessManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Order(Ordered.LOWEST_PRECEDENCE)
@RestController
@RequiredArgsConstructor
public class MainController {

    private final AccessManager accessManager;

    private WebClient localApiClient;

    @Autowired
    public void setLocalApiClient(@Value("${com.meli.fede.markoo.proxy.baseurl}") final String url) throws SSLException {
        final SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        final HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        this.localApiClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(url)
                .build();

    }

    @RequestMapping(path = "/**")
    public Object redirect(final HttpServletRequest request
            , final HttpServletResponse response
            , final HttpMethod httpMethod
            , @RequestBody(required = false) final Object body) {
        if (this.accessManager.validateAccess(request)) {
            final String path = request.getServletPath();
            final WebClient.RequestBodyUriSpec spec = this.localApiClient.method(httpMethod);
            if (body != null) {
                spec.bodyValue(body);
            }
            return spec.uri(path)
                    .retrieve()
                    .onStatus(HttpStatus::isError, cr -> {
                        response.setStatus(cr.rawStatusCode());
                        return Mono.empty();
                    })
                    .bodyToMono(Object.class)
                    .block();
        } else {
            response.setStatus(429);
            return null;
        }
    }
}
