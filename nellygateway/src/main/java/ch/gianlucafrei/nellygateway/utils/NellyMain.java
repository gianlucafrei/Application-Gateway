package ch.gianlucafrei.nellygateway.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NellyMain implements CommandLineRunner {

    private final ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {

        var zuulProperties = (ZuulProperties) context.getBean("zuul.CONFIGURATION_PROPERTIES");
        var routes = zuulProperties.getRoutes();

        log.info("Nelly started with {} routes", routes.size());
    }
}