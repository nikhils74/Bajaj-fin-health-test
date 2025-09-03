package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebhookTask {

    private static final String GENERATE_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private static final String TEST_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApplicationRunner runTasks(RestTemplate restTemplate) {
        return args -> {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "Nikhil S");
            requestBody.put("regNo", "1RF220089");
            requestBody.put("email", "nikhilsdude74@gmail.com");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> generateResponse =
                    restTemplate.postForEntity(GENERATE_URL, request, Map.class);

            System.out.println("response: " + generateResponse.getBody());

            String accessToken = generateResponse.getBody().get("accessToken").toString();

            String sqlQuery = "SELECT * FROM users WHERE id = 1"; // change as needed
            String testRequestBody = "{ \"finalQuery\": \"" + sqlQuery + "\" }";

            HttpHeaders testHeaders = new HttpHeaders();
            testHeaders.setContentType(MediaType.APPLICATION_JSON);
            testHeaders.set("Authorization", accessToken);

            HttpEntity<String> testRequest =
                    new HttpEntity<>(testRequestBody, testHeaders);

            ResponseEntity<String> testResponse =
                    restTemplate.postForEntity(TEST_URL, testRequest, String.class);

            System.out.println("final response: " + testResponse.getBody());
        };
    }
}
