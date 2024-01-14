import constants.ErrorMessages;
import constants.ResponseFields;
import constants.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Courier;
import steps.CourierSteps;

import java.util.Random;

public class CourierLoginTests {
    Courier courier;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Before
    public void prepareTest() {
        courier = new Courier(TestData.LOGIN + new Random().nextInt(), TestData.PASSWORD, TestData.FIRST_NAME);
        CourierSteps.createCourier(courier, 201);
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Запрос возвращает статус код - 200 и id")
    public void successfulAuthorizationCourier() {
        ValidatableResponse response = CourierSteps.login(courier.getLogin(), courier.getPassword(), 200);
        CourierSteps.checkFieldNotNull(response, ResponseFields.ID);
    }

    @Test
    @DisplayName("Ошибка авторизации с неправильным логином")
    @Description("Запрос возвращает статус код - 404 и сообщение что учетная запись не найдена")
    public void authorizationWithInvalidLogin() {
        ValidatableResponse response = CourierSteps.login(TestData.INVALID_LOGIN, courier.getPassword(), 404);
        CourierSteps.checkFieldEqualTo(response, ResponseFields.MESSAGE, ErrorMessages.NOT_FOUND_ACCOUNT);
    }

    @Test
    @DisplayName("Для авторизации нужно заполнить все обязательные поля")
    @Description("Запрос возвращает статус код - 400 и сообщение что недостаточно данных для входа")
    public void checkAuthorizationCourierWithoutFillingRequiredFields() {
        ValidatableResponse response = CourierSteps.login(courier.getLogin(), TestData.INVALID_PASSWORD, 400);
        CourierSteps.checkFieldEqualTo(response, ResponseFields.MESSAGE, ErrorMessages.NOT_FILLED_REQUIRED_FIELDS_AUTH);
    }

    @After
    public void tearDown() {
        ValidatableResponse response = CourierSteps.login(courier.getLogin(), courier.getPassword(), 200);
        int courierId = response.extract().jsonPath().getInt(ResponseFields.ID);
        CourierSteps.deleteCourier(courierId, 200);
    }
}
