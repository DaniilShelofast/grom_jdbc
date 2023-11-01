import java.sql.*;
import java.util.LinkedList;

public class ProductDAO {

    static String userName = "root";
    static String password = "Password";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test";

    public static LinkedList<Product> getProduct() {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Product");
            while (resultSet.next()) {
                long productID = resultSet.getLong("ID");
                String productName = resultSet.getString("NAME");
                String productDescription = resultSet.getString("DESCRIPTION");
                int productPrice = resultSet.getInt("PRICE");
                Product product = new Product(productID, productName, productDescription, productPrice);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return products;
    }


    public static Product save(Product product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement statement = connection.prepareStatement("INSERT INTO product (name, description, price) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());

            int line = statement.executeUpdate();
            if (line == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long generatedID = resultSet.getLong(1);
                    product.setId(generatedID);
                    return product;
                }
            }
        }
        throw new SQLException("Error");
    }

    public static Product update(Product product) throws Exception {

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement statement = connection.prepareStatement("UPDATE product SET name = ?, description = ?, price = ? WHERE id = ?")) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setInt(3, product.getPrice());
            statement.setLong(4, product.getId());

            int update = statement.executeUpdate();
            if (update > 0) {
                return product;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new Exception("Error");
    }


    public static void delete(long id) throws Exception {
        if (!checkId(id)) {
            System.out.println("Error....");
        }

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeLargeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static boolean checkId(long productId) throws Exception {
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement statement = connection.prepareStatement("SELECT * FROM product WHERE id = ?")) {
            statement.setLong(1, productId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new Exception("Error");
    }

}
