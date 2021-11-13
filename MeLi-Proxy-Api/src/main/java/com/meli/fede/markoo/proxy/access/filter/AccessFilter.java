package com.meli.fede.markoo.proxy.access.filter;

import com.meli.fede.markoo.proxy.access.manager.AccessManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessFilter implements Filter {

    private final AccessManager accessManager;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }
}
