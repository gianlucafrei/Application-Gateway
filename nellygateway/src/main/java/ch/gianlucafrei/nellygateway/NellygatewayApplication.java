package ch.gianlucafrei.nellygateway;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@Slf4j
@EnableZuulProxy
@SpringBootApplication
public class NellygatewayApplication {

    public static void main(String[] args) {

        // The global configuration is loaded before Spring starts
        log.debug(String.format("Nell starting... Working directory %s", System.getProperty("user.dir")));
        SpringApplication.run(NellygatewayApplication.class, args);
    }
}
