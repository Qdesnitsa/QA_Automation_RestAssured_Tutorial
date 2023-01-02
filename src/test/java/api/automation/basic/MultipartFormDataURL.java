package api.automation.basic;

import io.restassured.config.EncoderConfig;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class MultipartFormDataURL {
    @Test
    public void multipartFormData() {
        given()
                .baseUri("https://postman-echo.com")
                .multiPart("foo1", "bar1")
                .multiPart("foo2", "bar2")
                .log().all()
                .when()
                .post("/post")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void uploadFileMultipartFormData() {
        String attributes = "{\"name\":\"example.txt\",\"parent\":{\"id\":\"12345\"}}";
        given()
                .baseUri("https://postman-echo.com")
                .multiPart("file", new File("example.txt"))
                .multiPart("attributes", attributes, "application/json")
                .log().all()
                .when()
                .post("/post")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void downloadFile() throws IOException {
        InputStream is = given()
                .baseUri("https://mc.yandex.ru")
                .log().all()
                .when()
                .get("/watch/15011071/1?page-url=https%3A%2F%2Fdisk.yandex.ru%2Fd%2F26n_GBSkDTDQLw&charset=" +
                        "utf-8&ut=noindex&hittoken=1671150500_09ae96e6d62a8fbbc3f65f03394dc0e089f4178841d57dcd76b83400d88ec41b&browser-info=" +
                        "pa%3A1%3Aar%3A1%3Avf%3A75h6wcsjl31tvi5xjf8ir%3Afu%3A0%3Aen%3Autf-8%3Ala%3Aru-RU%3Av%3A943%3Acn%3A1%3Adp%3A0%3Als" +
                        "%3A243978735441%3Ahid%3A966501147%3Az%3A180%3Ai%3A20221216032827%3Aet%3A1671150507%3Ac%3A1%3Arn%3A457946196" +
                        "%3Arqn%3A7%3Au%3A16537420061019202781%3Aw%3A1519x150%3As%3A1536x864x24%3Ask%3A1.25%3Aco%3A0%3Acpf%3A1%3Aeu%3A0" +
                        "%3Ans%3A1671150498246%3Aadb%3A2%3Arqnl%3A1%3Ast%3A1671150507&t=gdpr(3-0)mc(p-5)clc(1-640-57)rqnt(7)lt(64200)aw(1)ecs(1)ti(2)")
                .then()
                .log().all()
                .extract()
                .response().asInputStream();

        OutputStream os = new FileOutputStream(new File("check.txt"));
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        os.write(bytes);
        os.close();
    }

    @Test
    public void formUrlEncoded() {
        given()
                .baseUri("https://postman-echo.com")
                .config(config().encoderConfig(EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .formParam("key1", "value1")
                .formParam("key 2", "value 2")
                .log().all()
                .when()
                .post("/post")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}
