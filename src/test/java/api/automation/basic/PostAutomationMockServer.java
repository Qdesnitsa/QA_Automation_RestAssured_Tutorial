package api.automation.basic;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostAutomationMockServer {
    @BeforeClass
    public void responseSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RestAssured.requestSpecification = requestSpecBuilder
                .setBaseUri("https://e29c8449-0fa6-4159-8993-51569f3df187.mock.pstmn.io")
                .addHeader("x-mock-match-request-body", "true")
//                .setConfig(config.encoderConfig(EncoderConfig.encoderConfig()
//                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .setContentType("application/json;charset=utf-8")
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
    public void validatePostRequestPayloadJsonArrayAsList() {
        Map<String, String> obj5001 = new HashMap<>();
        obj5001.put("id", "5001");
        obj5001.put("type", "None");
        Map<String, String> obj5002 = new HashMap<>();
        obj5002.put("id", "5002");
        obj5002.put("type", "Glazed");
        List<Map> jsonList = new ArrayList<>();
        jsonList.add(obj5001);
        jsonList.add(obj5002);

        given()
                .body(jsonList)
                .when()
                .post("/post")
                .then()
                .log().all()
                .assertThat()
                .body("msg", equalTo("Success"));

    }

    @Test
    public void validatePostRequestPayloadComplexJson() {
        List<Integer> idList = new ArrayList<>();
        idList.add(5);
        idList.add(9);

        Map<String, String> batterMap1 = new HashMap<>();
        batterMap1.put("id", "1001");
        batterMap1.put("type", "Regular");

        Map<String, Object> batterMap2 = new HashMap<>();
        batterMap2.put("id", idList);
        batterMap2.put("type", "Chocolate");

        List<Map> batterList = new ArrayList<>();
        batterList.add(batterMap1);
        batterList.add(batterMap2);

        Map<String, List> battersMap = new HashMap<>();
        battersMap.put("batter", batterList);

        List<String> typeList = new ArrayList<>();
        typeList.add("test1");
        typeList.add("test2");

        Map<String, String> toppingMap1 = new HashMap<>();
        toppingMap1.put("id", "5001");
        toppingMap1.put("type", "None");

        Map<String, Object> toppingMap2 = new HashMap<>();
        toppingMap2.put("id", "5002");
        toppingMap2.put("type", typeList);

        List<Map> toppingList = new ArrayList<>();
        toppingList.add(toppingMap1);
        toppingList.add(toppingMap2);

        Map<String, Object> mainMap = new LinkedHashMap<>();
        mainMap.put("id", "0001");
        mainMap.put("type", "donut");
        mainMap.put("name", "Cake");
        mainMap.put("ppu", 0.55);
        mainMap.put("batters", battersMap);
        mainMap.put("topping", toppingList);

        given()
                .body(mainMap)
                .when()
                .post("/postComplexJson")
                .then()
                .log().all()
                .assertThat()
                .body("msg", equalTo("Success"));
    }
}
