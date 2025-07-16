package com.jobdam.payment.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
public class PortOneClient {
    private final WebClient webClient;
    private String accessToken;

    public PortOneClient(
            @Value("${port-one.base-url}") String baseUrl,
            @Value("${port-one.api-key}") String apiKey,
            @Value("${port-one.api-secret}") String apiSecret
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        this.accessToken = fetchToken(apiKey, apiSecret);
    }

    private String fetchToken(String apiKey, String apiSecret) {
        JsonNode resp = webClient.post()
                .uri("/users/getToken")
                .bodyValue(Map.of("imp_key", apiKey, "imp_secret", apiSecret))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        return resp.get("response").get("access_token").asText();
    }

    public JsonNode readyPayment(ObjectNode payload) {
        return webClient.post()
                .uri("/payments/ready")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode approvePayment(JsonNode payload) {
        return webClient.post()
                .uri("/payments/{imp_uid}/approve", payload.get("imp_uid").asText())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode cancelPayment(ObjectNode payload) {
        return webClient.post()
                .uri("/payments/{imp_uid}/cancel", payload.get("imp_uid").asText())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode failPayment(ObjectNode payload) {
        return webClient.post()
                .uri("/payments/{imp_uid}/fail", payload.get("imp_uid").asText())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
