package api.automation.basic;

import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SimpleStart {

    @Test
    public void testValidateGetStatusCode() {
        given()
                .baseUri("https://api.postman.com")
                .header("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .when()
                .get("/workspaces")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testValidateResponseBody() {
        given()
                .baseUri("https://api.postman.com")
                .header("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .when()
                .get("/workspaces")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("workspaces.name", hasItems("My Workspace", "New Workspace"),
                        "workspaces.type", hasItems("personal", "personal"),
                        "workspaces[0].name", equalTo("My Workspace"),
                        "workspaces[0].name", is(equalTo("My Workspace")),
                        "workspaces.size()", equalTo(2),
                        "workspaces.name", hasItem("My Workspace"));
    }

    @Test
    public void testExtractResponse() {
        Response response = given()
                .baseUri("https://api.postman.com")
                .header("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .when()
                .get("/workspaces")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        //System.out.println("workspace name = " + JsonPath.from(response.asString()).getString("workspaces[0].name"));
        JsonPath jsonPath = new JsonPath(response.asString());
        //System.out.println("workspace name = " + jsonPath.getString("workspaces[0].name"));
        //Hamcrest
        assertThat((String) response.path("workspaces[0].name"), equalTo("My Workspace"));
        //TestNG
        Assert.assertEquals(response.path("workspaces[0].name"), "My Workspace");
    }

    @Test
    public void testValidateCollection() {
        given()
                .baseUri("https://api.postman.com")
                .header("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                .config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .log().all()
                //.log().headers()
                //.log().body()
                //f.e. if statusCode does not match
                //.log().ifValidationFails()
                .when()
                .get("/workspaces")
                .then()
                //.log().all()
                //.log().ifError()
                //.log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .body("workspaces.name", contains("My Workspace", "New Workspace"))
                .body("workspaces.name", containsInAnyOrder("New Workspace", "My Workspace"),
                        "workspaces.name", is(not(empty())),
                        "workspaces.name", hasSize(2),
                        "workspaces[0]", hasKey("id"),
                        "workspaces[0]", hasValue("personal"),
                        "workspaces[0]", hasEntry("id", "6d147bf0-ce86-4577-8dfc-1f553fb578cc"),
                        "workspaces[0]", not(equalTo(Collections.EMPTY_MAP)),
                        "workspaces[0].name", allOf(startsWith("My"), containsString("space")));
    }

    @Test
    public void testValidateCollectionBlackListHeaders() {
        Set<String> headers = new HashSet<>();
        headers.add("X-Api-Key");
        headers.add("Accept");
        given()
                .baseUri("https://api.postman.com")
                .header("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                //.config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-Key")))
                .config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers)))
                .log().all()
                .when()
                .get("/workspaces")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
