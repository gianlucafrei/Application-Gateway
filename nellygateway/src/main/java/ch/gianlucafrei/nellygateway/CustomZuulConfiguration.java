package ch.gianlucafrei.nellygateway;

import ch.gianlucafrei.nellygateway.config.configuration.NellyConfig;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * This configuration class replaces the usual config file configuration of zuul
 */
@Configuration
public class CustomZuulConfiguration {

    @Autowired
    private NellyConfig nellyConfig;

    private static final Logger log = LoggerFactory.getLogger(CustomZuulConfiguration.class);

    @Primary
    @Bean(name = "zuul.CONFIGURATION_PROPERTIES")
    @RefreshScope
    @ConfigurationProperties("zuul")
    public ZuulProperties zuulProperties() {

        log.debug("Load zuul configuration");

        ZuulProperties zuulProperties = new ZuulProperties();

        // Ignored pattern
        zuulProperties.setIgnoredPatterns(Sets.newHashSet("/auth/**", "/error"));

        // Routes
        zuulProperties.setRoutes(nellyConfig.getRoutesAsZuulRoutes());

        return zuulProperties;
    }

}