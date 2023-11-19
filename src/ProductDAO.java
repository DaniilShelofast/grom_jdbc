import java.sql.*;
import java.util.LinkedList;

public class ProductDAO {

    static String userName = "root";
    static String password = "Password";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test";

    public static LinkedList<Product> getProduct() throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("select * from product")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                products.add(new Product(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
        }
        return products;
    }


    public static Product update(Product product) throws Exception {
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("update product set name = ?, description = ?, price = ? where id = ?")) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getPrice());
            preparedStatement.setLong(4, product.getId());

            int up = preparedStatement.executeUpdate();
            if (up > 0) {
                return product;
            }
        }
        throw new Exception("Error");
    }

    public static LinkedList<Product> save(Product product) throws Exception {
        LinkedList<Product> products = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("insert into product (name, description, price) value (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getPrice());

            int line = preparedStatement.executeUpdate();
            if (line > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet != null && resultSet.next()) {
                    long id = resultSet.getLong(1);
                    product.setId(id);
                    products.add(product);
                }
            }
        }
        return products;
    }


    public static void delete(long id) throws Exception {
        if (!checkIdProduct(id)) {
            System.out.println("Error....");
        }

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); PreparedStatement preparedStatement = connection.prepareStatement("delete from product where id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeLargeUpdate();
        }
    }


    private static boolean checkIdProduct(long productId) throws Exception {
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
