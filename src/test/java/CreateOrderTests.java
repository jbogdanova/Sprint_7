import constants.Colors;
import constants.ResponseFields;
import constants.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.Order;
import steps.OrderSteps;

@RunWith(Parameterized.class)
public class CreateOrderTests {
    private final String[] color;

    public CreateOrderTests(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColors() {
        return new Object[][]{
                {new String[]{Colors.BLACK}},
                {new String[]{Colors.GREY}},
                {new String[]{Colors.BLACK, Colors.GREY}},
                {new String[]{}},
        };
    }

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Запрос возвращает статус код - 201 и track - номер заказа")
    public void createNewOrderAndCheckResponse() {
        Order order = new Order(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.ADDRESS, TestData.METRO_STATION,
                TestData.PHONE, TestData.RENT_TIME, TestData.DELIVERY_DATE, TestData.COMMENT, color);
        ValidatableResponse response = OrderSteps.createOrder(order, 201);
        OrderSteps.checkFieldNotNull(response, ResponseFields.TRACK);
    }
}
