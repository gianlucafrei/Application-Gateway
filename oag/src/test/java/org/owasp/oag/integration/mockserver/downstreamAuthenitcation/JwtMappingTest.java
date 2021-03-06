package org.owasp.oag.integration.mockserver.downstreamAuthenitcation;

import org.junit.jupiter.api.Test;
import org.owasp.oag.integration.testInfrastructure.IntegrationTestConfig;
import org.owasp.oag.integration.testInfrastructure.LocalServerTestConfig;
import org.owasp.oag.integration.testInfrastructure.WiremockTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.main.allow-bean-definition-overriding=true",
                "logging.level.org.owasp.oag=TRACE"},
        classes = {IntegrationTestConfig.class, LocalServerTestConfig.class})
public class JwtMappingTest extends WiremockTest {

    @Test
    public void testDownstreamAuthentication() {

        // Arrange
        LoginResult loginResult = makeLogin();

        // Act
        authenticatedRequest(HttpMethod.GET, TEST_1_ENDPOINT, loginResult)
                .exchange()
                .expectStatus().isOk();

        // We do it twice to also cover the cached case
        authenticatedRequest(HttpMethod.GET, TEST_1_ENDPOINT, loginResult)
                .exchange()
                .expectStatus().isOk();

        // Assert
        verify(getRequestedFor(urlEqualTo(TEST_1_ENDPOINT))
                .withHeader("Authorization", matching("Bearer .*")));
    }
}
