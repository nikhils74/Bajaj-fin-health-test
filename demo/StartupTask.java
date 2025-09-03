package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StartupTask {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApplicationRunner sendPostOnStartup(RestTemplate restTemplate) {
        return args -> {
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            // request body
            Map<String, String> body = new HashMap<>();
            body.put("name", "Nikhil S");
            body.put("regNo", "1RF220089");
            body.put("email", "nikhilsdude74@gmail.com");

            // headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            try {
                ResponseEntity<String> response =
                        restTemplate.postForEntity(url, request, String.class);
                System.out.println("Response: " + response.getBody());
            } catch (Exception e) {
                System.err.println("Error sending POST: " + e.getMessage());
            }
        };
    }
}
