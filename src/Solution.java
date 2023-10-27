import java.sql.*;
import java.util.ArrayList;

public class Solution {
//SELECT NAME FROM product WHERE PRICE >= 100;
//SELECT NAME FROM product WHERE LENGTH(DESCRIPTION) > 50;

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
                 Product product = new Product(productID,productName,productDescription,productPrice);
                 products.add(product);
            }
        }
        return products;
    }








    public static void main(String[] args) throws Exception {
        String userName = "root";
        String password = "Password";
        String connectionUrl = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {

        }
    }
}
