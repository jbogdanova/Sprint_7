import constants.ResponseFields;
import constants.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Order;
import steps.OrderSteps;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class OrderListTests {
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем получение списка заказов")
    public void getListOrders() {
        ValidatableResponse response = OrderSteps.getOrderList(200);
        OrderSteps.checkFieldNotNull(response, ResponseFields.ORDERS);
        List<Order> orderList = response.extract().jsonPath().getList(ResponseFields.ORDERS, Order.class);
        assertFalse(orderList.isEmpty());
    }
}
