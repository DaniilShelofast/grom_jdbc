import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {

    static String userName = "root";
    static String password = "Password";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test";

    public static LinkedList<Product> getAllProducts() throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("select * from product")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE")));
            }
        }
        return products;
    }

    public static LinkedList<Product> getProductsByPrice() throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE PRICE >= 100")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE")));
            }
        }
        return products;
    }

    public static LinkedList<Product> getProductsByDescription() throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE LENGTH(DESCRIPTION) > 50")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE")));
            }
        }
        return products;
    }

    public static void increasePrice() throws Exception {
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE product SET price = price + 100 WHERE price < 970;");
        }
    }

    public static void changeDescription() throws Exception {
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password)) {
            PreparedStatement select = connection.prepareStatement("SELECT ID, DESCRIPTION FROM product WHERE LENGTH(DESCRIPTION) > 100;");
            ResultSet resultSet = select.executeQuery();
            PreparedStatement update = connection.prepareStatement("UPDATE product SET DESCRIPTION = ? WHERE ID = ?");
            while (resultSet.next()) {
                long productId = resultSet.getLong("ID");
                String description = resultSet.getString("DESCRIPTION");

                String[] words = description.split("\\.");
                if (words.length > 1) {
                    int index = words.length - 1;
                    words[index] = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : words) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(".");
                        }
                        stringBuilder.append(s);
                    }
                    description = stringBuilder.toString();

                    update.setString(1, description);
                    update.setLong(2, productId);
                    update.executeUpdate();
                }
            }
        }
    }


    public static LinkedList<Product> findProductsWithEmptyDescription() throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE DESCRIPTION IS NULL OR DESCRIPTION = '';")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE")));
            }
        }
        return products;
    }

    public static LinkedList<Product> findProductsByName(String word) throws Exception {
        if (word.length() < 3 || word.isBlank()) {
            throw new Exception("Error");
        }

        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE NAME LIKE '%word%';")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE")));
            }
        }
        return products;
    }

    public static LinkedList<Product> findProductsByPrice(int price, int delta) throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PRODUCT WHERE PRICE BETWEEN ? AND ?")) {
            preparedStatement.setInt(1, price - delta);
            preparedStatement.setInt(2, price + delta);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE")));
            }
        }
        return products;
    }

    public static void testSavePerformance() throws Exception {
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("insert into TEST_SPEED (ID, SOME_STRING, SOME_NUMBER) value (?, ?, ?)")) {

            for (long i = 0; i <= 1000; i++) {
                preparedStatement.setLong(1, ThreadLocalRandom.current().nextLong(1L, Long.MAX_VALUE));
                preparedStatement.setString(2, "User" + ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE));
                preparedStatement.setInt(3, ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE));
                preparedStatement.executeUpdate();
            }
        }
    }
}

