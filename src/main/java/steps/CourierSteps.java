package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Courier;

import static io.restassured.RestAssured.given;

public class CourierSteps extends CommonSteps {
    @Step("Создать курьера с логином {courier.login}, паролем {courier.password} и именем {courier.firstName}")
    public static ValidatableResponse createCourier(Courier courier, int statusCode) {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/courier")
                .then();
        return checkStatusCode(response, statusCode);
    }

    @Step("Авторизация курьера с логином {login} и паролем {password}")
    public static ValidatableResponse login(String login, String password, int statusCode) {
        Courier courier = new Courier(login, password, null);
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/courier/login")
                .then();
        return checkStatusCode(response, statusCode);
    }

    @Step("Удалить курьера с id {courierId}")
    public static ValidatableResponse deleteCourier(int courierId, int statusCode) {
        ValidatableResponse response = given()
                .when()
                .delete("/courier/{courierId}", courierId)
                .then();
        return checkStatusCode(response, statusCode);
    }
}