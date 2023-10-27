package teaching_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Savepoint;
import java.sql.Statement;

public class lesson5 {
    public static void main(String[] args) throws Exception {
        String userName = "root";
        String password = "Password";
        String connectionUrl = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            statement.executeUpdate("drop table Books");
            statement.executeUpdate("create table if not exists Books(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");

            statement.executeUpdate("insert into Books (name) values('Inferno')");
            statement.executeUpdate("insert into Books (name) values('Solomon key')");
            Savepoint savepoint = connection.setSavepoint();
            statement.executeUpdate("insert into Books (name) values('Code')");

            connection.rollback(savepoint);
            connection.commit();
            connection.releaseSavepoint(savepoint);



        }
    }
}
