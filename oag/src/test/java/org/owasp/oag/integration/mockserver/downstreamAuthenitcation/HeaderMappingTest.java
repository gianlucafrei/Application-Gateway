package org.owasp.oag.integration.mockserver.downstreamAuthenitcation;

import org.junit.jupiter.api.Test;
import org.owasp.oag.config.ConfigLoader;
import org.owasp.oag.integration.testInfrastructure.IntegrationTestConfig;
import org.owasp.oag.integration.testInfrastructure.TestFileConfigLoader;
import org.owasp.oag.integration.testInfrastructure.WiremockTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.main.allow-bean-definition-overriding=true",
                "logging.level.org.owasp.oag=TRACE"},
        classes = {IntegrationTestConfig.class, HeaderMappingTest.PathTestConfig.class})
public class HeaderMappingTest extends WiremockTest {

    @Test
    public void testDownstreamAuthentication() {

        // Arrange
        LoginResult loginResult = makeLogin();

        // Act
        authenticatedRequest(HttpMethod.GET, TEST_1_ENDPOINT, loginResult)
                .exchange()
                .expectStatus().isOk();

        // Assert
        verify(getRequestedFor(urlEqualTo(TEST_1_ENDPOINT))
                .withHeader("X-USER-ID", matching(loginResult.id))
                .withHeader("X-USER-PROVIDER", matching("local")));
    }

    @TestConfiguration
    public static class PathTestConfig {

        @Primary
        @Bean
        ConfigLoader configLoader() {
            return new TestFileConfigLoader("/headerUserMappingConfig.yaml");
        }
    }
}
