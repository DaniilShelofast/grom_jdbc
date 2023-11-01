import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
public class Solution {

    static String userName = "root";
    static String password = "Password";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test";

    public static LinkedList<Product> getAllProducts() throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from Product");
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
            }
        }
        return products;
    }

    public static ArrayList<Product> getProductsByPrice() throws Exception {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE PRICE >= 100");
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
            }
        }
        return products;
    }

    public static ArrayList<Product> getProductsByDescription() throws Exception {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE LENGTH(DESCRIPTION) > 50");
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
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
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE DESCRIPTION IS NULL OR DESCRIPTION = '';");
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
            }
        }
        return products;
    }

    public static LinkedList<Product> findProductsByName(String word) throws Exception {

        if (word.length() < 3 || word.isBlank()) {
            throw new Exception("Error");
        }

        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE NAME LIKE '%word%';");
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
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
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
            }
        }
        return products;
    }
}

