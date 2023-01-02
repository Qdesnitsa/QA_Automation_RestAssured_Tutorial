package api.automation.basic;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class PutAutomation {
    @BeforeClass
    public void responseSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RestAssured.requestSpecification = requestSpecBuilder
                .setBaseUri("https://api.postman.com")
                .addHeader("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void validatePutRequest() {
        String workspaceId = "f9258780-ad97-4b03-a731-068b7d818d6d";
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"My Multi Workspace123\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"visibility\": \"personal\",\n" +
                "        \"description\": \"RestAssured created this\"\n" +
                "    }\n" +
                "}";
        given()
                .body(payload)
                .pathParam("workspaceId", workspaceId)
                .when()
                .put("/workspaces/{workspaceId}")
                .then()
                .log().all()
                .assertThat()
                .body("workspace.name", equalTo("My Multi Workspace123"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", equalTo(workspaceId));
    }
}
