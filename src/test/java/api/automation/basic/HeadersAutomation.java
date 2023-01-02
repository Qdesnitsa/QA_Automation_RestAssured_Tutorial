package api.automation.basic;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HeadersAutomation {
    @Test
    public void testMultipleHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("headerName", "value1");
        headersMap.put("x-mock-match-request-headers", "headerName");

        Header customHeader = new Header("headerName", "value1");
        Header matchHeader = new Header("x-mock-match-request-headers", "headerName");

        Headers headers = new Headers(customHeader, matchHeader);

        given()
                .baseUri("https://e29c8449-0fa6-4159-8993-51569f3df187.mock.pstmn.io")

                //.header(customHeader)
                //.header(matchHeader)

                //.headers(headers)

                .headers(headersMap)
                .when()
                .get("/get")
                .then()
                .assertThat()
                .statusCode(200)
                //.header("responseHeader", "resValue1")
                //.header("X-RateLimit-Limit", "120")
                .headers("responseHeader", "resValue1",
                        "X-RateLimit-Limit", "120");
    }

    @Test
    public void testMultipleValueHeaders() {
        Header header1 = new Header("multiValueHeader", "value1");
        Header header2 = new Header("multiValueHeader", "value2");

        Headers headers = new Headers(header1, header2);

        given()
                .baseUri("https://e29c8449-0fa6-4159-8993-51569f3df187.mock.pstmn.io")
                //.header("multiValueHeader", "value1", "value2")
                .headers(headers)
                .log().headers()
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testExtractResponseHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("headerName", "value1");
        headersMap.put("x-mock-match-request-headers", "headerName");

        Headers extractedHeaders = given()
                .baseUri("https://e29c8449-0fa6-4159-8993-51569f3df187.mock.pstmn.io")
                .headers(headersMap)
                //.log().headers()
                .when()
                .get("/get")
                .then()
                //.log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .headers();

        //extractedHeaders.asList().stream().forEach(s -> System.out.println("name=" + s.getName() + " value=" + s.getValue()));

        List<String> values = extractedHeaders.getValues("multiValueHeader");
        values.stream().forEach(s -> System.out.println(s));
    }
}
