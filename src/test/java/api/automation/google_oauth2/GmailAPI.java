package api.automation.google_oauth2;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GmailAPI {
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private String accessToken = "ya29.a0AX9GBdVpkP7sB6qK8leew2g2VVXTyZ3WcwRWIUqrDqb8TSyUa7RAUZ9JHwxrrE6bUpQ4Ccm_OC_4KzI15kGEn2_qzoTMjxn-XY0LwC2bBK1rFXguQ9eoGcs1CfmCyuIRLnrbuXeIDymNIsbN6hly9dWvA_q8GRQaCgYKAdESAQASFQHUCsbCYDZp9wl6SdrYCdetCSXAhw0166";
    private String userId = "lenkasid90@gmail.com";

    @BeforeClass
    public void requestAndResponseSpecificationInit() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri("https://gmail.googleapis.com")
                .addHeader("Authorization", "Bearer " + accessToken)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void getUserProfile() {
        given(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", userId)
                .when()
                .get("/users/{userid}/profile")
                .then()
                .spec(responseSpecification);
    }

    @Test
    public void sendMessage() {
        String message = "From: lenkasid90@gmail.com\n" +
                "To: lenkasid@tut.by\n" +
                "Subject: Rest Assured Test Email\n" +
                "\n" +
                "Sending from Rest Assured";

        String base64UrlEncodedMessage = Base64.getUrlEncoder().encodeToString(message.getBytes());
        Map<String, String> payload = new HashMap<>();
        payload.put("raw", base64UrlEncodedMessage);

        given(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", userId)
                .body(payload)
                .when()
                .post("/users/{userid}/messages/send")
                .then()
                .spec(responseSpecification);
    }
}
