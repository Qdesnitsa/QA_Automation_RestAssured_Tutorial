package api.automation.basic;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RequestSpecificationDefaultExample {

    @BeforeClass
    public void requestSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RestAssured.requestSpecification = requestSpecBuilder
                .setBaseUri("https://api.postman.com")
                .addHeader("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void testQuery() {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier
                .query(RestAssured.requestSpecification);
        System.out.println(queryableRequestSpecification.getBaseUri());
        System.out.println(queryableRequestSpecification.getHeaders());
    }

    @Test
    public void validateStatusCode() {
        Response response = get("/workspaces")
                .then()
                .log().all()
                .extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    @Test
    public void validateResponseBody() {
        Response response = get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces[0].name"), equalTo("My Workspace"));
    }
}
