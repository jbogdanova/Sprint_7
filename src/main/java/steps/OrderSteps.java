package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps extends CommonSteps {
    @Step("Создать заказ")
    public static ValidatableResponse createOrder(Order order, int statusCode) {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/orders")
                .then();
        return checkStatusCode(response, statusCode);
    }

    @Step("Принять заказ с id {orderId} курьером с id {courierId}")
    public static ValidatableResponse acceptOrder(int orderId, int courierId, int statusCode) {
        ValidatableResponse response = given()
                .queryParam("courierId", courierId)
                .when()
                .put("/orders/accept/{orderId}", orderId)
                .then();
        return checkStatusCode(response, statusCode);
    }

    @Step("Получить список заказов")
    public static ValidatableResponse getOrderList(int statusCode) {
        ValidatableResponse response = given()
                .when()
                .get("/orders")
                .then();
        return checkStatusCode(response, statusCode);
    }
}
