package com.jobdam.payment.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class PortOneClient {
    private final WebClient webClient;
    private final String apiKey;
    private final String apiSecret;
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
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.accessToken = fetchToken();
    }

    private String fetchToken() {
        JsonNode resp = webClient.post()
                .uri("/users/getToken")
                .bodyValue(Map.of("imp_key", apiKey, "imp_secret", apiSecret))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        return resp.path("response").path("access_token").asText();
    }

    private WebClient.RequestHeadersSpec<?> buildSpec(HttpMethod method, String uri, Object body, Object... uriVars) {
        var req = webClient.method(method)
                .uri(uri, uriVars)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return (body != null) ? req.bodyValue(body) : req;
    }

    private Mono<JsonNode> callApiReactive(HttpMethod method, String uri, Object body, Object... uriVars) {
        return buildSpec(method, uri, body, uriVars)
                .exchangeToMono(resp -> {
                    if (resp.statusCode() == HttpStatus.UNAUTHORIZED) {
                        accessToken = fetchToken();
                        return buildSpec(method, uri, body, uriVars)
                                .exchangeToMono(r2 -> r2.bodyToMono(JsonNode.class));
                    }
                    return resp.bodyToMono(JsonNode.class);
                })
                .flatMap(json -> {
                    if (json.has("code") && json.get("code").asInt() == 401) {
                        accessToken = fetchToken();
                        return buildSpec(method, uri, body, uriVars)
                                .exchangeToMono(r3 -> r3.bodyToMono(JsonNode.class));
                    }
                    return Mono.just(json);
                });
    }

    private JsonNode callApi(HttpMethod method, String uri, Object body, Object... uriVars) {
        return callApiReactive(method, uri, body, uriVars).block();
    }

    public JsonNode readyPayment(ObjectNode payload) {
        return callApi(HttpMethod.POST, "/payments/prepare", payload);
    }

    public JsonNode getPaymentByImpUid(String impUid) {
        return callApi(HttpMethod.GET, "/payments/{imp_uid}", null, impUid);
    }

    public JsonNode cancelPayment(ObjectNode payload) {
        return callApi(HttpMethod.POST, "/payments/cancel", payload);
    }

    public JsonNode failPayment(ObjectNode payload) {
        return callApi(HttpMethod.POST, "/payments/fail", payload);
    }
}
