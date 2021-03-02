package org.owasp.oag.infrastructure;

import org.owasp.oag.config.configuration.MainConfig;
import org.owasp.oag.logging.TraceContext;
import org.owasp.oag.services.tokenMapping.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures all non-trivial beans that need the main configuration for initialization
 */
@Configuration
public class PostConfigBeanConfiguration {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MainConfig config;

    @Bean
    public TraceContext traceContext() {

        var traceProfile = config.getTraceProfile();
        var implClass = context.getBean(traceProfile.getType(), TraceContext.class);

        if (implClass == null) {
            throw new RuntimeException("Trace implementation class not found: " + traceProfile.getType());
        }
        return implClass;
    }

    @Bean
    public TokenMapper tokenMapper(){

        var implName = config.getDownstreamAuthentication().getTokenMapping().getImplementation();
        var implClass = context.getBean(implName, TokenMapper.class);

        if (implClass == null) {
            throw new RuntimeException("TokenMapper implementation class not found: " + implName);
        }

        return implClass;
    }
}