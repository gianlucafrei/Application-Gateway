package ch.gianlucafrei.nellygateway.filters.zuul.route;

import ch.gianlucafrei.nellygateway.config.configuration.NellyConfig;
import ch.gianlucafrei.nellygateway.config.configuration.NellyRoute;
import ch.gianlucafrei.nellygateway.filters.spring.ExtractAuthenticationFilter;
import ch.gianlucafrei.nellygateway.session.Session;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessControlFilter extends ZuulFilter {

    private final NellyConfig config;

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        // Load nelly route
        String routeName = (String) ctx.get("proxy");
        NellyRoute nellyRoute = config.getRoutes().get(routeName);

        // Load session
        Optional<Session> sessionOptional = (Optional<Session>) request.getAttribute(ExtractAuthenticationFilter.NELLY_SESSION);

        if (nellyRoute.isAllowAnonymous())
            return null;

        if (sessionOptional.isEmpty()) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("not authorized");
            ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            log.info("Blocked unauthenticated request {} {}", request.getMethod(), request.getRequestURI());
        }

        return null;
    }
}
