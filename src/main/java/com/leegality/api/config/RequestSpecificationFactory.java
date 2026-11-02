package com.leegality.api.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public final class RequestSpecificationFactory {
    private RequestSpecificationFactory() {
    }

    public static RequestSpecification defaultSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.baseUrl())
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }
}
