package rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class RestTest {
    public static String token;
    public long bookingid;

    @BeforeAll
    public static void setUp() throws ParseException {
        RestAssured.baseURI ="https://restful-booker.herokuapp.com";

        String apiBody = "{\"username\":\"admin\",\"password\":\"password123\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(apiBody)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response.body().asString());
        token = (String) body.get("token");

//        System.out.println(token);
    }

    @BeforeEach
    public void createBooking() throws ParseException {
        System.out.println("BeforeEach");

        String apiBody = "{\"firstname\" : \"Jim\",\"lastname\" : \"Brown\",\"totalprice\" : 111,\"depositpaid\" : true,\"bookingdates\" : {\"checkin\" : \"2018-01-01\",\"checkout\" : \"2019-01-01\"}, \"additionalneeds\" : \"Breakfast\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .body(apiBody)
                .when()
                .post("booking")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response.body().asString());

        bookingid = (long) body.get("bookingid");
        System.out.println(bookingid);
    }

    @Test
    public void getDetails() throws ParseException {
        System.out.println(token);
        System.out.println(bookingid);

        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("booking/" + bookingid)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response.body().asString());
        String bodyText = body.toString();

//        System.out.println(bodyText);
    }

    @Test
    public void updateDetails() throws ParseException {
//        System.out.println(bookingid);
        String apiBody = "{\"firstname\" : \"Lolo\",\"totalprice\" : 222}";

        Response response = given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(apiBody)
                .when()
                .patch("booking/" + bookingid)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response.body().asString());
        String bodyText = body.toString();

//        System.out.println(bodyText);
    }

    @Test
    public void getAllBookings() throws ParseException {
        Response response = given()
                .when()
                .get("booking/" + "")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JSONParser parser = new JSONParser();
        JSONArray body = (JSONArray) parser.parse(response.body().asString());
        String bodyText = body.toString();

        System.out.println(bodyText);
    }

    @AfterEach
    public void deleteBooking() {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Cookie", "token=" + token)
                .when()
                .delete("booking/" + bookingid)
                .then()
                .statusCode(201)
                .extract()
                .response();

        System.out.println("deleteBooking");
    }
}
