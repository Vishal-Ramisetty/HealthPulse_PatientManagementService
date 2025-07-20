import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:4004"; // Adjust the base URI as needed
    }

    @Test
    public void shouldReturn200ForValidAuthentication() {
        String jsonBody = """
                {
                  "email": "testuser@test.com",
                  "password": "password123"
                }
                """;
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

    }

    @Test
    public void shouldReturn401ForInvalidAuthentication() {
        String jsonBody = """
                {
                  "email": "testuser@test.com",
                  "password": "InvalidPassword"
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

}
