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

public class CreateCourierTests {
    Courier courier;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Before
    public void prepareTest() {
        courier = new Courier(TestData.LOGIN + new Random().nextInt(), TestData.PASSWORD, TestData.FIRST_NAME);
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Запрос возвращает статус код - 201 и сообщение ok: true")
    public void createNewCourierAndCheckResponse() {
        ValidatableResponse response = CourierSteps.createCourier(courier, 201);
        CourierSteps.checkFieldEqualTo(response, ResponseFields.OK, true);
    }

    @Test
    @DisplayName("Невозможность создания двух одинаковых курьеров")
    @Description("Запрос возвращает статус код - 409 и сообщение что логин уже используется")
    public void checkNotCreateTwoIdenticalCouriers() {
        CourierSteps.createCourier(courier, 201);
        ValidatableResponse response = CourierSteps.createCourier(courier, 409);
        CourierSteps.checkFieldEqualTo(response, ResponseFields.MESSAGE, ErrorMessages.LOGIN_USED_ERROR);
    }

    @Test
    @DisplayName("Для создания курьера нужно заполнить все обязательные поля")
    @Description("Запрос возвращает статус код - 400 и сообщение что недостаточно данных для создания учетной записи")
    public void checkNotCreateCourierWithoutFillingRequiredFields() {
        courier.setPassword(null);
        ValidatableResponse response = CourierSteps.createCourier(courier, 400);
        CourierSteps.checkFieldEqualTo(response, ResponseFields.MESSAGE, ErrorMessages.NOT_FILLED_REQUIRED_FIELDS_ACCOUNT);
        courier.setPassword(TestData.PASSWORD);
        response = CourierSteps.createCourier(courier, 201);
        CourierSteps.checkFieldEqualTo(response, ResponseFields.OK, true);
    }

    @After
    public void tearDown() {
        ValidatableResponse response = CourierSteps.login(courier.getLogin(), courier.getPassword(), 200);
        int courierId = response.extract().jsonPath().getInt(ResponseFields.ID);
        CourierSteps.deleteCourier(courierId, 200);
    }
}
