package api.automation.authentication.form_authentication;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FormAuthSpringSecurity {

    @BeforeClass
    public void requestAndResponseSpecificationInit() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .setBaseUri("https://localhost:8443")
                .build();
    }

    @Test
    public void form_authentication_using_csrf_token_sessionID_filter() {
        SessionFilter filter = new SessionFilter();
        given()
                .auth().form("dan", "dan123",
                        new FormAuthConfig("/signin", "txtUsername", "txtPassword")
                                .withAutoDetectionOfCsrf())
                .filter(filter)
                .log().all()
                .when()
                .get("/login")
                .then()
                .log().all()
                .statusCode(200);

        given()
                .sessionId(filter.getSessionId())
                .log().all()
                .when()
                .get("/profile/index")
                .then()
                .log().all()
                .statusCode(200)
                .body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void form_authentication_using_csrf_token_sessionID_cookieBuilder() {
        SessionFilter filter = new SessionFilter();
        given()
                .auth().form("dan", "dan123",
                        new FormAuthConfig("/signin", "txtUsername", "txtPassword")
                                .withAutoDetectionOfCsrf())
                .filter(filter)
                .log().all()
                .when()
                .get("/login")
                .then()
                .log().all()
                .statusCode(200);

        Cookie cookie1 = new Cookie.Builder("JSESSIONID", filter.getSessionId())
                .setSecured(true)
                .setHttpOnly(true)
                .setComment("my cookie")
                .build();
        Cookie cookie2 = new Cookie.Builder("dummy", "dummyValue").build();
        Cookies cookies = new Cookies(cookie1, cookie2);

        given()
                //.cookie("JSESSIONID", filter.getSessionId())
                //.cookie(cookie1)
                .cookies(cookies)
                .log().all()
                .when()
                .get("/profile/index")
                .then()
                .log().all()
                .statusCode(200)
                .body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void fetch_single_and_multiple_cookies() {
        Response response = given()
                .log().all()
                .when()
                .get("/profile/index")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        Map<String, String> multipleCookies = response.getCookies();
        multipleCookies
                .entrySet()
                .forEach(System.out::println);

        Cookies multipleCookiesDetailed = response.getDetailedCookies();
        multipleCookiesDetailed
                .asList()
                .forEach(System.out::println);

        String singleCookie = response.getCookie("JSESSIONID");
        System.out.println(singleCookie);

        Cookie detailedCookie = response.getDetailedCookie("JSESSIONID");
        System.out.println(detailedCookie);
    }
}
