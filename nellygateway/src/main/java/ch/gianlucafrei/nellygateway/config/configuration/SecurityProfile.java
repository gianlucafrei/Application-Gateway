package ch.gianlucafrei.nellygateway.config.configuration;

import ch.gianlucafrei.nellygateway.config.ErrorValidation;
import ch.gianlucafrei.nellygateway.config.FileConfigLoader;
import ch.gianlucafrei.nellygateway.filters.zuul.route.CsrfValidationFilter;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.util.*;

@Getter @Setter
@NoArgsConstructor
public class SecurityProfile implements ErrorValidation {

    private List<String> allowedMethods;
    private String csrfProtection;
    private List<String> csrfSafeMethods = Lists.asList("GET", new String[]{"HEAD", "OPTIONS"});
    private Map<String, String> responseHeaders = new HashMap<>();

    // TODO maybe refactor reflection access
    /** This setter is used over reflection by the object mapper in {@link FileConfigLoader#load(InputStream, InputStream)} */
    private void setResponseHeaders(Map<String, String> headers) {
        this.responseHeaders = Objects.requireNonNullElseGet(headers, HashMap::new);
    }

    @Override
    public List<String> getErrors(ApplicationContext context) {
        var errors = new ArrayList<String>();

        if (allowedMethods == null)
            errors.add("'allowedMethods' not specified");

        if (csrfProtection == null)
            errors.add("'csrfProtection' not specified");

        if (csrfSafeMethods == null)
            errors.add("'csrfSafeMethods' not specified");

        if (responseHeaders == null)
            errors.add("'responseHeaders' not specified");

        try {
            CsrfValidationFilter.loadValidationImplementation(csrfProtection, context);
        } catch (NoSuchBeanDefinitionException ex) {
            errors.add("No csrf implementation found for '" + csrfProtection + "'");
        }

        return errors;
    }
}
