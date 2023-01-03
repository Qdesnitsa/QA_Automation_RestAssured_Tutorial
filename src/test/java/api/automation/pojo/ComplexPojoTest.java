package api.automation.pojo;

import api.automation.pojo.collection.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ComplexPojoTest {
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
    public void complex_pojo_create_collection() throws JsonProcessingException, JSONException {

        Header header = new Header("Content-Type", "application/json");
        List<Header> headerList = new ArrayList<>();
        headerList.add(header);

        Body body = new Body("raw", "{\"data\": \"123\"}");

        RequestRequest request = new RequestRequest("https://postman-echo.com/post",
                "POST", headerList, body, "This is a sample POST Request");

        RequestRootRequest requestRoot = new RequestRootRequest("This is a folder", request);
        List<RequestRootRequest> requestRootList = new ArrayList<>();
        requestRootList.add(requestRoot);

        FolderRequest folder = new FolderRequest("This is a folder", requestRootList);
        List<FolderRequest> folderList = new ArrayList<>();
        folderList.add(folder);

        Info info = new Info("Collection1", "This is just a sample collection.",
                "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        CollectionRequest collection = new CollectionRequest(info, folderList);
        CollectionRootBase collectionRoot = new CollectionRootRequest(collection);

        String collectionUid = given()
                .body(collectionRoot)
                .when()
                .post("/collections")
                .then()
                .log().all()
                .extract()
                .response()
                .path("collection.uid");

        CollectionRootResponse deserializedCollectionRoot = given()
                .pathParam("collectionUid", collectionUid)
                .when()
                .get("/collections/{collectionUid}")
                .then()
                .extract()
                .response()
                .as(CollectionRootResponse.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot);
        String deserializedCollectionRootStr = objectMapper.writeValueAsString(deserializedCollectionRoot);

        JSONAssert.assertEquals(collectionRootStr, deserializedCollectionRootStr,
                new CustomComparator(JSONCompareMode.STRICT_ORDER,
                        new Customization("collection.item[*].item[*].request.url", new ValueMatcher<Object>() {
                            @Override
                            public boolean equal(Object o, Object t1) {
                                return true;
                            }
                        })));

        List<String> urlRequestList = new ArrayList<>();
        List<String> urlResponseList = new ArrayList<>();

        for (RequestRootRequest requestRootRequest : requestRootList) {
            urlRequestList.add(requestRootRequest.getRequest().getUrl());
        }

        List<FolderResponse> folderResponseList = deserializedCollectionRoot.getCollection().getItem();
        for (FolderResponse folderResponse : folderResponseList) {
            List<RequestRootResponse> requestRootResponseList = folderResponse.getItem();
            for (RequestRootResponse requestRootResponse : requestRootResponseList) {
                URL url = requestRootResponse.getRequest().getUrl();
                urlResponseList.add(url.getRaw());
            }
        }

        assertThat(urlResponseList, containsInAnyOrder(urlRequestList.toArray()));
    }

    @Test
    public void without_folder_pojo_create_collection() throws JsonProcessingException, JSONException {

        List<FolderRequest> folderList = new ArrayList<>();

        Info info = new Info("Collection2", "This is just a sample collection.",
                "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        CollectionRequest collection = new CollectionRequest(info, folderList);
        CollectionRootBase collectionRoot = new CollectionRootRequest(collection);

        String collectionUid = given()
                .body(collectionRoot)
                .when()
                .post("/collections")
                .then()
                .log().all()
                .extract()
                .response()
                .path("collection.uid");

        CollectionRootResponse deserializedCollectionRoot = given()
                .pathParam("collectionUid", collectionUid)
                .when()
                .get("/collections/{collectionUid}")
                .then()
                .extract()
                .response()
                .as(CollectionRootResponse.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot);
        String deserializedCollectionRootStr = objectMapper.writeValueAsString(deserializedCollectionRoot);

        assertThat(objectMapper.readTree(collectionRootStr),
                equalTo(objectMapper.readTree(deserializedCollectionRootStr)));

//        JSONAssert.assertEquals(collectionRootStr, deserializedCollectionRootStr,
//                new CustomComparator(JSONCompareMode.STRICT_ORDER,
//                        new Customization("collection.item[*].item[*].request.url", new ValueMatcher<Object>() {
//                            @Override
//                            public boolean equal(Object o, Object t1) {
//                                return true;
//                            }
//                        })));
    }
}
