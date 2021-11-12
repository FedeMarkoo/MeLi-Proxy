package com.meli.fede.markoo.access.filter;

import com.meli.fede.markoo.access.manager.AccessManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessFilter implements Filter {

    private final AccessManager accessManager;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        if (this.accessManager.validateAccess((HttpServletRequest) request)) {
            filterChain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(429);
        }
    }
}
