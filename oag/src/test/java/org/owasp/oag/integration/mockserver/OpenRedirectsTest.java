package org.owasp.oag.integration.mockserver;

import org.junit.jupiter.api.Test;
import org.owasp.oag.integration.testInfrastructure.WiremockTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenRedirectsTest extends WiremockTest {

    private Collection<String> testCases;

    public OpenRedirectsTest() throws IOException {

        this.testCases = loadOpenRedirectTestCases();
    }

    public static Collection<String> loadOpenRedirectTestCases() throws IOException {

        Collection<String> testCases = new ArrayList<>();

        var filestream = OpenRedirectsTest.class.getClassLoader().getResourceAsStream("openredirects.txt");
        var fileReader = new BufferedReader(new InputStreamReader(filestream));

        String line = null;
        while ((line = fileReader.readLine()) != null) {
            testCases.add(line);
        }

        return testCases;
    }

    @Test
    void testOpenRedirects() throws Exception {

        var failedCases = new ArrayList<>();

        for(var testCase : testCases){

            var loginUrl = "/auth/local/login?returnUrl=" + testCase;
            var result = webClient.get().uri(loginUrl).exchange().returnResult(String.class);
            var status = result.getStatus().value();

            if(status == 302)
                failedCases.add(testCase);
        }

        assertTrue(failedCases.isEmpty(), "Some openRedirects were not rejected: " + failedCases.toString());
    }
}
