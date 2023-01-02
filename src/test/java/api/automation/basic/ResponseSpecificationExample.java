package api.automation.basic;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ResponseSpecificationExample {
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void responseSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RestAssured.requestSpecification = requestSpecBuilder
                .setBaseUri("https://api.postman.com")
                .addHeader("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .log(LogDetail.ALL)
                .build();

//        responseSpecification = RestAssured.expect()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().all();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validateStatusCode() {
        get("/workspaces")
                .then().spec(responseSpecification);
    }

    @Test
    public void validateResponseBody() {
        Response response = get("/workspaces")
                .then().spec(responseSpecification)
                .log().all()
                .extract().response();
        assertThat(response.path("workspaces[0].name"), equalTo("My Workspace"));
    }
}
