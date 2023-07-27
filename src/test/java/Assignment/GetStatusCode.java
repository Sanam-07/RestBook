package Assignment;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.response.Response;

public class GetStatusCode {
	String token;
	String orderid;
	Faker faker = new Faker();

	@Test(priority = 0)
	void getStatus() {
		given().when().get("https://simple-books-api.glitch.me/status").then().statusCode(200).log().all();
	}

	@Test(priority = 1)
	void registerClient() {
		String email = faker.internet().emailAddress();
		String firstname = faker.name().fullName();
		System.out.println(email);
		System.out.println(firstname);
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("clientName", firstname);
		data.put("clientEmail", email);
		Response response = given()

				.contentType("application/json").body(data).when()
				.post("https://simple-books-api.glitch.me/api-clients");
		token = response.jsonPath().getString("accessToken");
		;
	}

	@Test(priority = 2)
	void listOfBooks() {
		given().pathParam("mypath", "books") // path parameters
				.queryParam("type", "non-fiction")// query parameters
				.when().get("https://simple-books-api.glitch.me/{mypath}").then().statusCode(200).log().all();
	}

	@Test(priority = 3)
	void getSingleBook() {
		given().when().get("https://simple-books-api.glitch.me/books/1").then().statusCode(200).log().all();
	}

	@Test(priority = 4)
	void orderBook() {
		String name = faker.name().firstName();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("bookId", 1);
		data.put("customerName", name);
		Response response = given().contentType("application/json").body(data)
				.header("Authorization", "Bearer " + token).when().post("https://simple-books-api.glitch.me/orders");
		orderid = response.jsonPath().getString("orderId");
		System.out.println(orderid);

	}

	@Test(priority = 5)
	void getorderbooks() {
		given().header("Authorization", "Bearer " + token).when().get("https://simple-books-api.glitch.me/orders")
				.then().statusCode(200);
	}

	@Test(priority = 6)
	void getOneOrder() {
//		int convertedorderid = Integer.parseInt(orderid);
//		System.out.println(convertedorderid);
		given().header("Authorization", "Bearer " + token).when()
				.get("https://simple-books-api.glitch.me/orders/" + orderid).then().statusCode(200).log().all();
	}

	@Test(priority = 7)
	void updateOrder() {
		String name = faker.name().firstName();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("bookId", 1);
		data.put("customerName", name);
		given().contentType("application/json").body(data).header("Authorization", "Bearer " + token).when()
				.post("https://simple-books-api.glitch.me/orders").then().statusCode(201);
	}

	@Test(priority = 8)
	void deleteOrder() {
		given().header("Authorization", "Bearer " + token).when()
				.delete("https://simple-books-api.glitch.me/orders/" + orderid).then().statusCode(204);

	}
}
