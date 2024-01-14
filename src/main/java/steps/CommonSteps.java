package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CommonSteps {

    @Step("Проверить, что код ответа равен {statusCode}")
    public static ValidatableResponse checkStatusCode(ValidatableResponse response, int statusCode) {
        return response.statusCode(statusCode);
    }

    @Step("Проверить, что в теле ответа содержится поле {field} равное {operand}")
    public static <T> ValidatableResponse checkFieldEqualTo(ValidatableResponse response, String field, T operand) {
        return response.body(field, equalTo(operand));
    }

    @Step("Проверить, что в теле ответа содержится поле {field}")
    public static ValidatableResponse checkFieldNotNull(ValidatableResponse response, String field) {
        return response.body(field, notNullValue());
    }
}