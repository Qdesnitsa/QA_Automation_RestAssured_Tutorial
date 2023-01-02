package api.automation.basic;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RequestPathParameter {
    @Test
    public void queryParameter() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("foo5", "bar5");
        queryParams.put("foo6", "bar6");
        given()
                .baseUri("https://postman-echo.com")
//                .param("foo2", "bar2")
//                .param("foo4", "bar4")
//                .queryParam("foo1", "bar1")
//                .queryParam("foo3", "bar3")
//                .queryParams("foo1", "bar1", "foo3", "bar3")
                .queryParams(queryParams)
                .log().all()
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void multiValueQueryParameter() {
        given()
                .baseUri("https://postman-echo.com")
                .queryParam("foo1", "bar1, bar2, bar3")
                //.queryParam("foo1", "bar1;bar2;bar3")
                .log().all()
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void pathParameter() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("userId", "2");
        pathParams.put("bookId", "1");
        given()
                .baseUri("https://reqres.in")
                .pathParam("userId", "2")
                //.pathParam("bookId", "1")
                .log().all()
                .when()
                .get("/api/users/{userId}")
                //.get("/api/users/{userId}/{bookId}")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}
