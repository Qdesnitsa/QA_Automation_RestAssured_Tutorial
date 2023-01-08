package api.automation.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class JacksonAPI_JSON {
    @BeforeClass
    public void responseSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RestAssured.requestSpecification = requestSpecBuilder
                //.setBaseUri("https://api.postman.com")
                .setBaseUri("https://e29c8449-0fa6-4159-8993-51569f3df187.mock.pstmn.io")
                .addHeader("x-mock-match-request-body", "true")
                .setContentType("application/json;charset=utf-8")
                //.addHeader("X-Api-Key", "PMAK-638cebffa1b9ab067de10803-597f2f61077fe78039e73a8211dd088ccd")
                //.setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void validatePostRequestPayloadJsonAsMap() throws JsonProcessingException {
        Map<String, Object> mainObject = new HashMap<>();
        Map<String, String> nestedObject = new HashMap<>();
        nestedObject.put("name", "myWorkspace1");
        nestedObject.put("type", "personal");
        nestedObject.put("description", "Here is some description");
        mainObject.put("workspace", nestedObject);

        ObjectMapper objectMapper = new ObjectMapper();
        String mainObjectStr = objectMapper.writeValueAsString(mainObject);

        Response response = with()
                .body(mainObjectStr)
                .post("/workspaces")
                .then()
                .log().all()
                .extract()
                .response();
        assertThat(response.path("workspace.name"), equalTo("myWorkspace1"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validatePostRequestPayloadJsonAsList() throws JsonProcessingException {
        Map<String, String> obj5001 = new HashMap<>();
        obj5001.put("id", "5001");
        obj5001.put("type", "None");
        Map<String, String> obj5002 = new HashMap<>();
        obj5002.put("id", "5002");
        obj5002.put("type", "Glazed");
        List<Map<String, String>> jsonList = new ArrayList<>();
        jsonList.add(obj5001);
        jsonList.add(obj5002);

        given()
                .body(jsonList)
                .when()
                .post("/post")
                .then()
                .log().all()
                .body("msg", equalTo("Success"));
    }

    @Test
    public void serializeJacksonObjectNodeToJsonObject() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode nestedObjectNode = objectMapper.createObjectNode();
        nestedObjectNode.put("name", "myWorkspace10");
        nestedObjectNode.put("type", "personal");
        nestedObjectNode.put("description", "Here is some description");

        ObjectNode mainObjectNode = objectMapper.createObjectNode();
        mainObjectNode.set("workspace", nestedObjectNode);

        String mainObjectStr = objectMapper.writeValueAsString(mainObjectNode);

        Response response = with()
                //.body(mainObjectStr)
                .body(mainObjectNode)
                .post("/workspaces")
                .then()
                .log().all()
                .extract()
                .response();
        assertThat(response.path("workspace.name"), equalTo("myWorkspace10"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void serializeJacksonArrayNodeToJsonArray() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode obj5001Node = objectMapper.createObjectNode();
        obj5001Node.put("id", "5001");
        obj5001Node.put("type", "None");

        ObjectNode obj5002Node = objectMapper.createObjectNode();
        obj5002Node.put("id", "5002");
        obj5002Node.put("type", "Glazed");

        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add(obj5001Node);
        arrayNode.add(obj5002Node);

        String jsonListStr = objectMapper.writeValueAsString(arrayNode);

        given()
                .body(jsonListStr)
                .when()
                .post("/post")
                .then()
                .log().all()
                .assertThat()
                .body("msg", equalTo("Success"));
    }
}
