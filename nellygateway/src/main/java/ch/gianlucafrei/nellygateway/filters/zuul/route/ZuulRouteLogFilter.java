package ch.gianlucafrei.nellygateway.filters.zuul.route;

import ch.gianlucafrei.nellygateway.config.configuration.NellyConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

@Slf4j
@Component
public class ZuulRouteLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        //
        String routeName = (String) ctx.get("proxy");
        URL routeHost = ctx.getRouteHost();
        log.info("Request of route '{}' gets forwarded to {}", routeName, routeHost.toExternalForm());

        return null;
    }
}
