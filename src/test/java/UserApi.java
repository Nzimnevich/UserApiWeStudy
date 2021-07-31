import io.restassured.RestAssured;
import jdk.jfr.Label;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class UserApi {

    static {

        RestAssured.baseURI = "https://userapi.webinar.ru/v3/";
    }


    String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@mailinator.com";

    @Test
    @Label("Получить список курсов, GET")
    public void withoutToken() {
        get("organization/courses")
                .then().statusCode(401)
                .body("error.message", is("A Token was not found in the TokenStorage."));

    }

    @Test
    @Label("Получить список курсов, GET")
    public void withToken() {
        given().header("X-Auth-Token", "c7069fd6a948ed584beb2c48801a5ed6").
                get("organization/courses")
                .then().statusCode(200);

    }

    @Test
    @Label("Получить список курсов, GET")
    public void checkFirstCourse() {
        given().header("X-Auth-Token", "N") //токен нельзя публиковать в публичных репазиториях
                .get("organization/courses")
                .then()
                .body("data.id[0]", is(55905))
                .body("data.name[0]", is("Новый курс"))
                .body("data.owner[0].id", is(42548317))
                .body("data.isPublish[0]", is(false));
    }


    @Test
    @Label("Получить список курсов, GET")
    public void checkSecondCourse() {
        given().header("X-Auth-Token", "N")
                .get("organization/courses")
                .then()
                .body("data.id[1]", is(59441))
                .body("data.name[1]", is("Новый курс"))
                .body("data.owner[1].id", is(42548317))
                .body("data.isPublish[1]", is(true));
    }


    @Test
    @Label("Пригласить участника в группу, POST")
    public void getInfoAboutCourse() {
        given().contentType(JSON)
                .body("{\"data\":{ \"email\": \"" + email + "\"}}")
                .header("X-Auth-Token", "N")
                .post("groups/" + 72095 + "/invites?sendInvites=true")
                .then().statusCode(200)
                .body("contact.id[0]", notNullValue())
                .body("contact.email[0]", is(email))
                .body("contact.firstName[0]", nullValue())
                .body("contact.lastName[0]", nullValue());
    }


}
