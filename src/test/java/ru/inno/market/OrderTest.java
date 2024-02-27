package ru.inno.market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.*;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private Order order;
    private Catalog catalog = new Catalog();

    @BeforeEach
    public void setUp() {
        Client client = new Client(1, "Ио");
        order = new Order(1, client);
    }

    @Test
    @Tag("Order")
    @DisplayName("Добавление первого товара и проверка суммы")
    public void checkAddItem() {
        Item item = catalog.getItemById(1);
        order.addItem(item);
        assertEquals(1, order.getCart().size());
        assertTrue(order.getItems().containsKey(item));
        assertEquals(item.getPrice(), order.getTotalPrice());

    }

    @Test
    @Tag("Order")
    @DisplayName("Добавление непервого товара и проверка что сумма равна сумме цен двух товаров")
    public void checkAddItemAnother() {
        Item item = catalog.getItemById(1);
        order.addItem(item);
        Item item1 = catalog.getItemById(2);
        order.addItem(item1);
        assertEquals(2, order.getCart().size());
        assertTrue(order.getItems().containsKey(item));
        assertTrue(order.getItems().containsKey(item1));
        assertEquals(item.getPrice() + item1.getPrice(), order.getTotalPrice());
    }

    @Test
    @Tag("Order")
    @DisplayName("Добавление в корзину двух одинаковых товаров")
    public void checkAddItemSame() {
        Item item = catalog.getItemById(1);
        order.addItem(item);
        Item item1 = catalog.getItemById(1);
        order.addItem(item1);
        assertEquals(1, order.getCart().size());
        assertTrue(order.getItems().containsKey(item));
        assertTrue(order.getItems().containsKey(item1));
        assertEquals(item.getPrice() + item1.getPrice(), order.getTotalPrice());
    }

    @Test
    @Tag("applyDiscount")
    @DisplayName("Применение скидки")
    public void applyDiscountOnce() {
        Item item = catalog.getItemById(1);
        order.addItem(item);
        order.applyDiscount(0.8);
        assertEquals(item.getPrice() * (1 - 0.8), order.getTotalPrice());
    }

    @Test
    @Tag("applyDiscount")
    @DisplayName("Применение скидки второй раз")
    public void applyDiscountTwice() {
        Item item = catalog.getItemById(1);
        order.addItem(item);
        order.applyDiscount(0.8);
        order.applyDiscount(0.7);
        assertEquals(item.getPrice() * (1 - 0.8), order.getTotalPrice());
    }

}


