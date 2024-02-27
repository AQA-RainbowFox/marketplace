package ru.inno.market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;
import ru.inno.market.model.PromoCodes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarketServiceTest {
    Catalog catalog = new Catalog();
    MarketService service = null;

    @BeforeEach
    public void setUp() {
        service = new MarketService();
    }

    @Test
    @Tag("createOrderFor")
    @DisplayName("Создание одного заказа  для одного клиента через MarketService")
    public void createOneOrderForClient() {
        Client client = new Client(999, "Калипсо");
        int id = service.createOrderFor(client);
        assertEquals(0, id);
        assertEquals(client, service.getOrderInfo(id).getClient());
    }

    @Test
    @Tag("createOrderFor")
    @DisplayName("Создание двух заказов для одного клиента через MarketService")
    public void createTwoOrdersForClient() {
        Client client = new Client(999, "Калипсо");
        int id = service.createOrderFor(client);
        int id1 = service.createOrderFor(client);
        assertEquals(0, id);
        assertEquals(1, id1);
        assertEquals(service.getOrderInfo(id).getClient(), service.getOrderInfo(id1).getClient());

    }

    @Test
    @Tag("addItemToOrder")
    @DisplayName("Добавление товара к существующему заказу")
    public void addItemToCurrentOrder() {
        Client client = new Client(999, "Калипсо");
        int id = service.createOrderFor(client);
        Item item = catalog.getItemById(1);
        service.addItemToOrder(item, id);
        assertTrue(service.getOrderInfo(id).getItems().containsKey(item));

    }

    @Test
    @Tag("addItemToOrder")
    @DisplayName("Добавление двух одинаковых товаров")
    public void addTwoItemToCurrentOrder() {
        Client client = new Client(999, "Калипсо");
        int id = service.createOrderFor(client);
        Item item = catalog.getItemById(1);
        Item item1 = catalog.getItemById(1);
        service.addItemToOrder(item, id);
        service.addItemToOrder(item1, id);
        assertTrue(service.getOrderInfo(id).getItems().containsKey(item));
        assertTrue(service.getOrderInfo(id).getItems().containsKey(item1));

    }

    @Test
    @Tag("applyDiscountForOrder")
    @DisplayName("Применение скидки для заказа")
    public void applyDiscountForOrderOnce() {
        Client client = new Client(999, "Калипсо");
        int id = service.createOrderFor(client);
        Item item = catalog.getItemById(1);
        service.addItemToOrder(item, id);
        service.applyDiscountForOrder(id, PromoCodes.FIRST_ORDER);
        assertEquals(item.getPrice() * (1 - 0.2), service.getOrderInfo(id).getTotalPrice());

    }

    @Test
    @Tag("applyDiscountForOrder")
    @DisplayName("Повторное применение скидки для заказа")
    public void applyDiscountForOrderAgain() {
        Client client = new Client(999, "Калипсо");
        int id = service.createOrderFor(client);
        Item item = catalog.getItemById(1);
        service.addItemToOrder(item, id);
        service.applyDiscountForOrder(id, PromoCodes.FIRST_ORDER);
        service.applyDiscountForOrder(id, PromoCodes.FIRST_ORDER);
        assertEquals(item.getPrice() * (1 - 0.2), service.getOrderInfo(id).getTotalPrice());
    }

}
