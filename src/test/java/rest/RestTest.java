package rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RestTest {
    public static String token;
    public long bookingid;

    @BeforeAll
    public static void setUp() throws ParseException {
        RestAssured.baseURI ="https://restful-booker.herokuapp.com";

        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "admin");
        requestParams.put("password", "password123");

        String response = given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .asString();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response);
        token = (String) body.get("token");
    }

    @BeforeEach
    public void createBooking() throws ParseException {
        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2018-01-01");
        bookingdates.put("checkout", "2019-01-01");

        JSONObject requestParams = new JSONObject();
        requestParams.put("firstname", "Jim");
        requestParams.put("lastname", "Brown");
        requestParams.put("totalprice", 111);
        requestParams.put("depositpaid", true);
        requestParams.put("bookingdates", bookingdates);
        requestParams.put("additionalneeds", "Breakfast");

        Response response = given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .body(requestParams.toJSONString())
                .when()
                .post("booking")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response.body().asString());
        bookingid = (long) body.get("bookingid");
    }

    public JSONObject getDetails() throws ParseException {
        String response = given()
                .header("Accept", "application/json")
                .when()
                .get("booking/" + bookingid)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .asString();

        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject) parser.parse(response);
        return body;
    }

    @Test
    public void getDetailsTest() throws ParseException {
        JSONObject details = getDetails();

        Assert.assertEquals(details.get("firstname"), "Jim");
        Assert.assertEquals(details.get("lastname"), "Brown");
        Assert.assertEquals((long) details.get("totalprice"), 111);
        Assert.assertEquals(details.get("depositpaid"), true);
        Assert.assertEquals(((JSONObject) details.get("bookingdates")).get("checkin"), "2018-01-01");
        Assert.assertEquals(((JSONObject) details.get("bookingdates")).get("checkout"), "2019-01-01");
        Assert.assertEquals(details.get("additionalneeds"), "Breakfast");
    }

    @Test
    public void updateDetailsTest() throws ParseException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("firstname", "Lolo");
        requestParams.put("totalprice", 222);

        given().header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(requestParams.toJSONString())
                .when()
                .patch("booking/" + bookingid)
                .then()
                .statusCode(200);

        JSONObject details = getDetails();

        Assert.assertEquals(details.get("firstname"), "Lolo");
        Assert.assertEquals(details.get("lastname"), "Brown");
        Assert.assertEquals((long) details.get("totalprice"), 222);
        Assert.assertEquals(details.get("depositpaid"), true);
        Assert.assertEquals(((JSONObject) details.get("bookingdates")).get("checkin"), "2018-01-01");
        Assert.assertEquals(((JSONObject) details.get("bookingdates")).get("checkout"), "2019-01-01");
        Assert.assertEquals(details.get("additionalneeds"), "Breakfast");
    }

    @Test
    public void getBookingIDsTest() throws ParseException {
        String response = given()
                .when()
                .get("booking/")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .asString();

        JSONParser parser = new JSONParser();
        JSONArray body = (JSONArray) parser.parse(response);

        List<Long> arr = new ArrayList<>();
        body.forEach(el -> arr.add((Long) ((JSONObject) el).get("bookingid")));

        Assert.assertTrue(arr.contains(bookingid));
    }

    @AfterEach
    public void deleteBooking() {
        given().header("Content-type", "application/json")
                .header("Cookie", "token=" + token)
                .when()
                .delete("booking/" + bookingid)
                .then()
                .statusCode(201);
    }
}
