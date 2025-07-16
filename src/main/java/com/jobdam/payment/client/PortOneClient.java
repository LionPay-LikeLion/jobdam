// src/main/java/com/jobdam/payment/client/PortOneClient.java
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

    /** 1) 최초 또는 갱신된 토큰 발급 */
    private String fetchToken() {
        JsonNode resp = webClient.post()
                .uri("/users/getToken")
                .bodyValue(Map.of("imp_key", apiKey, "imp_secret", apiSecret))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return resp.path("response").path("access_token").asText();
    }

    /** 2) 요청 스펙 빌드 (Bearer 헤더 포함) */
    private WebClient.RequestHeadersSpec<?> buildSpec(HttpMethod method, String uri, Object body, Object... uriVars) {
        var req = webClient.method(method)
                .uri(uri, uriVars)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return (body != null) ? req.bodyValue(body) : req;
    }

    /** 3) 단일 재시도 포함한 호출 처리 */
    private Mono<JsonNode> callApiReactive(HttpMethod method, String uri, Object body, Object... uriVars) {
        return buildSpec(method, uri, body, uriVars)
                .exchangeToMono(resp -> {
                    if (resp.statusCode() == HttpStatus.UNAUTHORIZED) {
                        // HTTP 401 → 토큰 재발급 및 재시도
                        accessToken = fetchToken();
                        return buildSpec(method, uri, body, uriVars)
                                .exchangeToMono(r2 -> r2.bodyToMono(JsonNode.class));
                    }
                    return resp.bodyToMono(JsonNode.class);
                })
                .flatMap(json -> {
                    // JSON 내부 { code:401 } 응답 시에도 단일 재시도
                    if (json.has("code") && json.get("code").asInt() == 401) {
                        accessToken = fetchToken();
                        return buildSpec(method, uri, body, uriVars)
                                .exchangeToMono(r3 -> r3.bodyToMono(JsonNode.class));
                    }
                    return Mono.just(json);
                });
    }

    /** 4) 동기 방식 호출 래퍼 */
    private JsonNode callApi(HttpMethod method, String uri, Object body, Object... uriVars) {
        return callApiReactive(method, uri, body, uriVars).block();
    }

    /** [1] 결제 준비 (사전등록) */
    public JsonNode readyPayment(ObjectNode payload) {
        return callApi(HttpMethod.POST, "/payments/prepare", payload);
    }

    /** [2] 결제 정보 조회 (승인 후 검증용) */
    public JsonNode getPaymentByImpUid(String impUid) {
        return callApi(HttpMethod.GET, "/payments/{imp_uid}", null, impUid);
    }

    /** [3] 결제 취소 */
    public JsonNode cancelPayment(ObjectNode payload) {
        return callApi(HttpMethod.POST, "/payments/cancel", payload);
    }

    /** [4] 결제 실패 */
    public JsonNode failPayment(ObjectNode payload) {
        return callApi(HttpMethod.POST, "/payments/fail", payload);
    }
}
