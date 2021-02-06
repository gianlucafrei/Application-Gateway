package ch.gianlucafrei.nellygateway.services.login.drivers;

import ch.gianlucafrei.nellygateway.config.configuration.LoginProviderSettings;
import ch.gianlucafrei.nellygateway.services.login.drivers.LoginDriver;
import ch.gianlucafrei.nellygateway.services.login.drivers.github.GitHubDriver;
import ch.gianlucafrei.nellygateway.services.login.drivers.oidc.OidcDriver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URI;

import static ch.gianlucafrei.nellygateway.services.login.drivers.LoginDriverTypes.GITHUB;
import static ch.gianlucafrei.nellygateway.services.login.drivers.LoginDriverTypes.OIDC;

@Component
@RequiredArgsConstructor
public class LoginDriverLoader {

    private final ApplicationContext context;

    public LoginDriver loadDriverByKey(String driverName, URI callbackURI, LoginProviderSettings settings) {

        if ("oidc".equals(driverName))
            return new OidcDriver(settings, callbackURI);

        if ("github".equals(driverName))
            return new GitHubDriver(settings, callbackURI);

        throw new RuntimeException("Login driver with name " + driverName + " not found");
    }
}
