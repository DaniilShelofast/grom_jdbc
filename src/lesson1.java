import java.sql.Connection;
import java.sql.DriverManager;

public class lesson1 {
    public static void main(String[] args) throws Exception{
        String userName = "root";
        String password = "Password";
        String connectionUrl = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl,userName,password)){
            System.out.println("We`re connected");
        }
    }
}
