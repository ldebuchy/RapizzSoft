package dao;

import model.Pizza;
import java.sql.*;
import java.util.*;

public class PizzaDAO {
    private Connection conn;

    public PizzaDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Pizza> getAllPizzas() {
        List<Pizza> pizzas = new ArrayList<>();
        String sql = "SELECT * FROM PIZZA";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pizza pizza = new Pizza(
                    rs.getInt("IDPIZZA"),
                    rs.getString("NOM"),
                    rs.getDouble("PRIX")
                );
                pizzas.add(pizza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzas;
    }
}

