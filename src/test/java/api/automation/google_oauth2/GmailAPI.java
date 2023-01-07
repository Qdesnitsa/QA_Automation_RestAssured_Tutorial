package api.automation.google_oauth2;

import api.automation.authentication.google_oauth2.History;
import api.automation.authentication.google_oauth2.HistoryResponse;
import api.automation.authentication.google_oauth2.Profile;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GmailAPI {
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private String accessToken
            = "ya29.a0AX9GBdVpfWueLcNnb353Xww5b5QTu7AKPCe2UKGlbSVuANSJMwGWv19H2FO-mdXgLJfbjJUg2m_6EH6WuO2GIhBdTjfZQsMyFtLJR0_jKd-EFzeO9POD5JpQ-ACyKEczOJSPnaY_aIHLpimth_9Am0Sr-ouoc1nRaCgYKAXUSAQASFQHUCsbCjeSeCAfrjkzuY5P3uUNVrA0167";
    private String userId = "lenkasid90@gmail.com";
    private int startHistoryId = 45174;

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
    public void getExactNumberOfIncomeMessages() {
        History messages = given(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", userId)
                .queryParams("maxResults", 10)
                .when()
                .get("/users/{userid}/messages")
                .then()
                .extract()
                .response()
                .as(History.class);
        assertThat(messages.getMessages().size(), equalTo(10));
    }

    //documentation: https://developers.google.com/gmail/api/reference/rest/v1/users.history/list
    @Test
    public void getIncomeMessagesAfterStartHistoryId() {
        //startHistoryId = Integer.parseInt(getMyProfile().getHistoryId());
        sendMessageToMyself();
        HistoryResponse historyResponse = given(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", userId)
                .queryParams("startHistoryId", startHistoryId)
                .when()
                .get("/users/{userid}/history")
                .then()
                .extract()
                .response()
                .as(HistoryResponse.class);
        assertThat(historyResponse.getHistory().size(), greaterThanOrEqualTo(1));
    }

    // documentation: https://developers.google.com/gmail/api/reference/rest/v1/users/getProfile
    private Profile getMyProfile() {
        Profile profile = given(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", userId)
                .when()
                .get("/users/{userid}/profile")
                .then()
                .extract()
                .response()
                .as(Profile.class);
        return profile;
    }

    // documentation: https://developers.google.com/gmail/api/reference/rest/v1/users.messages/send
    // message structure: https://developers.google.com/gmail/api/reference/rest/v1/users.messages#Message
    // base64 encoder/decoder: https://ostermiller.org/calc/encode.html
    private void sendMessageToMyself() {
        String message = "From: lenkasid90@gmail.com\n" +
                "To: lenkasid90@gmail.com\n" +
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
