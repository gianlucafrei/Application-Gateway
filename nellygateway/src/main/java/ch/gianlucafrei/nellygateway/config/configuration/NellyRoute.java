package ch.gianlucafrei.nellygateway.config.configuration;

import ch.gianlucafrei.nellygateway.config.ErrorValidation;
import lombok.*;
import org.springframework.context.ApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NellyRoute implements ErrorValidation {

    private String path;
    private String url;
    private String type;
    private boolean allowAnonymous;

    @Override
    public List<String> getErrors(ApplicationContext context) {
        var errors = new ArrayList<String>();

        if (path == null)
            errors.add("path not defined");

        if (url == null)
            errors.add("url not defined");

        if (type == null)
            errors.add("type not defined");

        // Dont continue with validation if fields are missing
        if (!errors.isEmpty())
            return errors;

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            errors.add("invalid url");
        }

        return errors;
    }
}
