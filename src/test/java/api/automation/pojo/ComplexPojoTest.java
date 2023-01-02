package api.automation.pojo;

import api.automation.pojo.collection.*;
import api.automation.pojo.simple.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

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
    public void complex_pojo_create_collection() {

        Header header = new Header("Content-Type", "application/json");
        List<Header> headerList = new ArrayList<>();
        headerList.add(header);

        Body body = new Body("raw", "{\"data\": \"123\"}");

        Request request = new Request("https://postman-echo.com/post",
                "POST", headerList, body, "This is a sample POST Request");

        RequestRoot requestRoot = new RequestRoot("This is a folder", request);
        List<RequestRoot> requestRootList = new ArrayList<>();
        requestRootList.add(requestRoot);

        Folder folder = new Folder("This is a folder", requestRootList);
        List<Object> folderList = new ArrayList<>();
        folderList.add(folder);

        Info info = new Info("Collection1", "This is just a sample collection.",
                "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        Collection collection = new Collection(info, folderList);
        CollectionRoot collectionRoot = new CollectionRoot(collection);

        given()
                .body(collectionRoot)
                .when()
                .post("/collections")
                .then()
                .log().all();
    }
}
