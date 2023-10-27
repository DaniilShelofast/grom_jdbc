import java.sql.*;
import java.util.ArrayList;

public class Solution {

    static String userName = "root";
    static String password = "Password";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test";


    public static ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
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

    public static ArrayList<Product> getProductsByPrice() throws SQLException {
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

    public static ArrayList<Product> getProductsByDescription() throws SQLException {
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
}
