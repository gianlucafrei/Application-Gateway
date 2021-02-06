package ch.gianlucafrei.nellygateway.services.login.drivers;

import ch.gianlucafrei.nellygateway.config.configuration.LoginProviderSettings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

@Getter
public abstract class LoginDriverBase implements LoginDriver {


    private final LoginProviderSettings settings;
    private final URI callbackURI;

    public LoginDriverBase(LoginProviderSettings settings, URI callbackURI) {
        this.callbackURI = callbackURI;

        List<String> errors = getSettingsErrors(settings);
        if (errors.isEmpty()) {
            this.settings = settings;
        } else {
            throw new InvalidProviderSettingsException(errors);
        }
    }
}
