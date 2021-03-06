package org.owasp.oag.config;

import org.springframework.context.ApplicationContext;

import java.util.List;

public interface ErrorValidation {

    List<String> getErrors(ApplicationContext context);
}
