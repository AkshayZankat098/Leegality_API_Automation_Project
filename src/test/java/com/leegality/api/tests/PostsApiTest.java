package com.leegality.api.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

class PostsApiTest extends BaseApiTest {
    @Test
    void getPostByIdReturnsPost() {
        request()
                .pathParam("postId", 1)
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyOrNullString()));
    }

    @Test
    void getPostsCanBeFilteredByUserId() {
        request()
                .queryParam("userId", 1)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    void createPostReturnsCreatedResource() {
        Map<String, Object> newPost = Map.of(
                "title", "API automation test",
                "body", "Created by the Rest Assured framework",
                "userId", 1);

        Response response = request()
                .body(newPost)
                .when()
                .post("/posts");

        response.then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("title", equalTo(newPost.get("title")))
                .body("body", equalTo(newPost.get("body")))
                .body("userId", equalTo(newPost.get("userId")));
    }
}
