package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        executeSqlFile("./db/update_001.sql");
    }

    @After
    public void after() throws SQLException {
        executeSqlFile("./db/dropOrdersAfter.sql");
    }

    private void executeSqlFile(String sql) throws SQLException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(sql)))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndByIdOneRowWithDescriptionAndName() {
        OrdersStore store = new OrdersStore(pool);
        Order o = store.save(Order.of("name1", "description1"));
        Order order = store.findById(o.getId());
        assertThat(order.getDescription(), is("description1"));
        assertThat(order.getName(), is("name1"));
        assertThat(order.getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndByByNameWithDescriptionAndName() {
        OrdersStore store = new OrdersStore(pool);
        Order o = store.save(Order.of("name1", "description1"));
        Order o2 = store.save(Order.of("name1", "description2"));
        List<Order> all = (List<Order>) store.findByName(o.getName());
        assertThat(all.size(), is(2));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
        assertThat(all.get(1).getDescription(), is("description2"));
        assertThat(all.get(1).getId(), is(2));
    }

    @Test
    public void whenUpdateOrderWithDescriptionAndName() {
        OrdersStore store = new OrdersStore(pool);
        Order o = store.save(Order.of("name1", "description1"));
        Order updated = Order.of("name2", "description2");
        updated.setId(o.getId());
        store.update(updated);
        Order order = store.findById(o.getId());
        assertThat(order.getDescription(), is("description2"));
        assertThat(order.getName(), is("name2"));
        assertThat(order.getId(), is(1));
    }
}