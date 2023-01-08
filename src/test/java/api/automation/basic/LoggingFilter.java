package api.automation.basic;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class LoggingFilter {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() throws FileNotFoundException {
        PrintStream fileOutputStream = new PrintStream(new File("restAssured.log"));
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(fileOutputStream))
                .addFilter(new ResponseLoggingFilter(fileOutputStream));
        requestSpecification = requestSpecBuilder.build();
        responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void loggingFilter() throws FileNotFoundException {
        given(requestSpecification)
                .baseUri("https://postman-echo.com")
                //.filter(new RequestLoggingFilter(LogDetail.BODY, fileOutputStream))
                //.filter(new ResponseLoggingFilter(LogDetail.STATUS, fileOutputStream))
                //.filter(new RequestLoggingFilter(LogDetail.BODY))
                //.filter(new ResponseLoggingFilter(LogDetail.STATUS))
                //.log().all()
                .when()
                .get("/get")
                .then().spec(responseSpecification)
                //.log().all()
                .statusCode(200);
    }
}
