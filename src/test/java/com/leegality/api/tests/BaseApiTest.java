package com.leegality.api.tests;

import io.restassured.specification.RequestSpecification;

import static com.leegality.api.config.RequestSpecificationFactory.defaultSpec;
import static io.restassured.RestAssured.given;

abstract class BaseApiTest {
    protected RequestSpecification request() {
        return given().spec(defaultSpec());
    }
}
