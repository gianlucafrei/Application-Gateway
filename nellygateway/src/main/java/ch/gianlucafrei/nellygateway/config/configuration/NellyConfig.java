package ch.gianlucafrei.nellygateway.config.configuration;

import ch.gianlucafrei.nellygateway.config.ErrorValidation;
import lombok.*;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationContext;

import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NellyConfig implements ErrorValidation {

    private Map<String, LoginProvider> loginProviders;
    private Map<String, NellyRoute> routes;
    private Map<String, SecurityProfile> securityProfiles;
    private String hostUri;
    private String nellyApiKey;
    private List<String> trustedRedirectHosts;
    private SessionBehaviour sessionBehaviour;

    public Map<String, ZuulProperties.ZuulRoute> getRoutesAsZuulRoutes() {

        Map<String, ZuulProperties.ZuulRoute> zuulRoutes = new HashMap<>();

        if (getRoutes() != null) {
            getRoutes().forEach((name, route) -> {
                ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute(route.getPath(), route.getUrl());
                zuulRoute.setId(name);
                zuulRoute.setSensitiveHeaders(new HashSet<>());
                zuulRoutes.put(name, zuulRoute);
            });
        }

        return zuulRoutes;
    }

    public boolean isHttpsHost() {

        if (hostUri == null)
            return false;

        return getHostUri().startsWith("https://");
    }

    @Override
    public List<String> getErrors(ApplicationContext context) {

        var errors = new ArrayList<String>();

        if (loginProviders == null)
            errors.add("NellyConfig: loginProviders not defined");

        if (routes == null)
            errors.add("NellyConfig: routes not defined");

        if (hostUri == null)
            errors.add("NellyConfig: hostUri not defined");

        if (securityProfiles == null)
            errors.add("NellyConfig: securityProfiles not defined");

        if (trustedRedirectHosts == null)
            errors.add("NellyConfig: trustedRedirectHosts not defined");

        if (sessionBehaviour == null)
            errors.add("NellyConfig: sessionBehaviour not defined");

        if (!errors.isEmpty())
            return errors;

        // Recursive validation
        loginProviders.values().forEach(s -> errors.addAll(s.getErrors(context)));
        securityProfiles.values().forEach(s -> errors.addAll(s.getErrors(context)));
        routes.values().forEach(s -> errors.addAll(s.getErrors(context)));
        errors.addAll(sessionBehaviour.getErrors(context));

        if (!errors.isEmpty())
            return errors;

        // Cross cutting concerns
        // Check if security profile exists for each route
        routes.values().forEach(r -> {

            if (!securityProfiles.containsKey(r.getType()))
                errors.add(String.format("Security profile '%s' does not exist", r.getType()));
        });

        return errors;
    }
}
