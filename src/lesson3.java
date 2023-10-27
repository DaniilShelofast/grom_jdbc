import java.sql.*;

public class lesson3 {
    public static void main(String[] args) throws Exception {
        String userName = "root";
        String password = "Password";
        String connectionUrl = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table Users");
            statement.executeUpdate("create table if not exists Users(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, password CHAR(30) NOT NULL, PRIMARY KEY (id))");
            statement.executeUpdate("insert into Users (name, password) values('max', '123')");
            statement.executeUpdate("insert into Users SET name = 'otherGuy', password = '321'");
            String userId = "2";
           /* String userId2 = "'1'";
            ResultSet resultSet = statement.executeQuery("select * from Users where id = '" + userId2 + "");
            while (resultSet.next()){
                System.out.println("userName : " + resultSet.getString("name"));
                System.out.println("userPassword : " + resultSet.getString("password"));
            }*/

            PreparedStatement preparedStatement = connection.prepareStatement("select * from Users where id = ?");
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println("userName : " + resultSet.getString("name"));
                System.out.println("userPassword : " + resultSet.getString("password"));
            }

        }
    }
}
