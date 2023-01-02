package api.automation.basic;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RequestSpecificationExample {
    RequestSpecification requestSpecification;

    @BeforeClass
    public void requestSpecificationInit() {
//        requestSpecification = with()
//                //with() and given() are equal
//                .baseUri("https://api.postman.com")
//                .header("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
//                .log().all();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecification = requestSpecBuilder
                .setBaseUri("https://api.postman.com")
                .addHeader("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void validateStatusCode() {
        Response response = given(requestSpecification)
                .header("dummyHeader", "dummuValue")
                .get("/workspaces")
                .then()
                .log().all()
                .extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));

//        given(requestSpecification)
//                //given().spec(requestSpecification)
//                .when()
//                .get("/workspaces")
//                .then()
//                .log().all()
//                .assertThat()
//                .statusCode(200);
    }

    @Test
    public void validateResponseBody() {
        Response response = given().spec(requestSpecification).get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces[0].name"), equalTo("My Workspace"));
    }
}
