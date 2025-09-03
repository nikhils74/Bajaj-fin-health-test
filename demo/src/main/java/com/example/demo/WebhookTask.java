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

            String sqlQuery = "SELECT P.AMOUNT AS SALARY, E.FIRST_NAME || ' ' || E.LAST_NAME AS NAME, EXTRACT(YEAR FROM AGE(CURRENT_DATE, E.DOB)) AS AGE, D.DEPARTMENT_NAME FROM PAYMENTS P JOIN EMPLOYEE E ON P.EMP_ID = E.EMP_ID JOIN DEPARTMENT D ON E.DEPARTMENT = D.DEPARTMENT_ID WHERE EXTRACT(DAY FROM P.PAYMENT_TIME) <> 1 ORDER BY P.AMOUNT DESC LIMIT 1;";
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
