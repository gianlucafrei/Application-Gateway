package ch.gianlucafrei.nellygateway.filters.spring;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(1)
@Component
@NoArgsConstructor
public class SimpleLogFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        log.info("Request to {} {}", req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);


        HttpServletResponse res = (HttpServletResponse) response;
        log.info("Response status code {} for {} {}", res.getStatus(), req.getMethod(), req.getRequestURI());
    }
}
