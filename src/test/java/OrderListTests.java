import constants.Colors;
import constants.ResponseFields;
import constants.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Courier;
import pojo.Order;
import steps.CourierSteps;
import steps.OrderSteps;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class OrderListTests {
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем получение списка заказов")
    public void getListOrders() {
        Courier courier = new Courier(TestData.LOGIN + new Random().nextInt(),
                TestData.PASSWORD, TestData.FIRST_NAME);
        CourierSteps.createCourier(courier, 201);
        ValidatableResponse courierResponse = CourierSteps.login(courier.getLogin(), courier.getPassword(), 200);
        Order order = new Order(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.ADDRESS, TestData.METRO_STATION,
                TestData.PHONE, TestData.RENT_TIME, TestData.DELIVERY_DATE, TestData.COMMENT, new String[]{Colors.BLACK});
        ValidatableResponse orderResponse = OrderSteps.createOrder(order, 201);
        int orderId = orderResponse.extract().jsonPath().getInt(ResponseFields.TRACK);
        order.setId(orderId);
        int courierId = courierResponse.extract().jsonPath().getInt(ResponseFields.ID);
        OrderSteps.acceptOrder(orderId, courierId, 200);
        ValidatableResponse orderListResponse = OrderSteps.getOrderList(courierId, 200);
        List<Order> orderList = orderListResponse.extract().jsonPath().getList("orders", Order.class);
        assertTrue(orderList.contains(order));
    }
}
