package api.automation.pojo.simple;

import api.automation.pojo.simple.SimplePojo;
import api.automation.pojo.simple.Workspace;
import api.automation.pojo.simple.WorkspaceRoot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.matchesPattern;
import static org.testng.Assert.assertTrue;

public class SimplePojoTest {
    @BeforeClass
    public void responseSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RestAssured.requestSpecification = requestSpecBuilder
                .setBaseUri("https://api.postman.com")
                .addHeader("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                //.setBaseUri("https://e29c8449-0fa6-4159-8993-51569f3df187.mock.pstmn.io")
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
    public void simplePojoSerialization() {
        //SimplePojo simplePojo = new SimplePojo("value1", "value2");
//        String payload = "{\n" +
//                "    \"key1\":\"value1\",\n" +
//                "    \"key2\":\"value2\"\n" +
//                "}";
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.setKey1("value1");
        simplePojo.setKey2("value2");
        given()
                //.body(payload)
                .body(simplePojo)
                .when()
                .post("/post")
                .then()
                .assertThat()
                .body("key1", equalTo(simplePojo.getKey1()),
                        "key2", equalTo(simplePojo.getKey2()));

    }

    @Test
    public void simplePojoDeSerialization() throws JsonProcessingException {
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.setKey1("value1");
        simplePojo.setKey2("value2");
        SimplePojo deserializesPojo = given()
                .body(simplePojo)
                .when()
                .post("/post")
                .then()
                .log().all()
                .extract()
                .response()
                .as(SimplePojo.class);
        assertThat(simplePojo,equalTo(deserializesPojo));

        ObjectMapper objectMapper = new ObjectMapper();
        String deserializedPojoStr = objectMapper.writeValueAsString(deserializesPojo);
        String simplePojoStr = objectMapper.writeValueAsString(simplePojo);
        assertThat(objectMapper.readTree(deserializedPojoStr),equalTo(objectMapper.readTree(simplePojoStr)));
    }

    @Test (dataProvider = "workspace")
    public void validatePostRequestPayloadAsMap(String name, String type, String description) {
        Workspace workspace = new Workspace(name, type, description);
        Map<String, String> newMap = new HashMap<>();
        workspace.setSomeMap(newMap);
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        WorkspaceRoot deserializesWorkspaceRoot = given()
                .body(workspaceRoot)
                .when()
                .post("/workspaces")
                .then()
                .log().all()
                .extract()
                .response()
                .as(WorkspaceRoot.class);

        assertThat(deserializesWorkspaceRoot.getWorkspace().getName(), equalTo(workspace.getName()));
        assertThat(deserializesWorkspaceRoot.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @DataProvider(name = "workspace")
    public Object[][] getWorkspace() {
        return new Object[][] {
                {"workspace4", "personal", "description4"},
                {"workspace5", "team", "description5"}
        };
    }
}
