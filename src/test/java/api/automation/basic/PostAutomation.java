package api.automation.basic;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostAutomation {
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
    public void validatePostRequestPayloadString() {
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"My Multi Workspace1\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"visibility\": \"personal\",\n" +
                "        \"description\": \"RestAssured created this\"\n" +
                "    }\n" +
                "}";
        Response response = with()
                .body(payload)
                .post("/workspaces");
        assertThat(response.path("workspace.name"), equalTo("My Multi Workspace1"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
//        given()
//                .body(payload)
//                .when()
//                .post("/workspaces")
//                .then()
//                .assertThat()
//                .body("workspace.name", equalTo("My Multi Workspace"),
//                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validatePostRequestPayloadFile() {
        File file = new File("src/main/resources/CreateWorkspacePayload.json");
        Response response = with()
                .body(file)
                .post("/workspaces")
                .then()
                .log().all()
                .extract().response();
        assertThat(response.path("workspace.name"), equalTo("MySecondWorkspace"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validatePostRequestPayloadMap() {
        Map<String, Object> mainObject = new HashMap<>();
        Map<String, String> nestedObject = new HashMap<>();
        nestedObject.put("name", "MyThirdWorkspace");
        nestedObject.put("type", "personal");
        nestedObject.put("description", "Here is some description");
        mainObject.put("workspace", nestedObject);
        Response response = with()
                .body(mainObject)
                .post("/workspaces")
                .then()
                .log().all()
                .extract().response();
        assertThat(response.path("workspace.name"), equalTo("MyThirdWorkspace"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
    }
}
