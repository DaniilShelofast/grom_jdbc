import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class lesson4 {
    public static void main(String[] args) throws Exception {
        String userName = "root";
        String password = "Password";
        String connectionUrl = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password); Statement statement = connection.createStatement()) {
            //statement.executeUpdate("drop table if exists Books");
            statement.executeUpdate("create table if not exists Books(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, img BLOB,  PRIMARY KEY (id))");
            BufferedImage image = ImageIO.read(new File("C:\\Users\\User\\Pictures//smile.png"));


            Blob blob = connection.createBlob();
            try (OutputStream outputStream = blob.setBinaryStream(1)) {
                ImageIO.write(image, "png", outputStream);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("insert into Books (name, img) values (?,?)");
            preparedStatement.setString(1, "inferno");
            preparedStatement.setBlob(2, blob);
            preparedStatement.execute();

            ResultSet resultSet = statement.executeQuery("select * from Books");
            while (resultSet.next()) {
                Blob blob2 = resultSet.getBlob("img");
                InputStream inputStream = blob2.getBinaryStream();
                BufferedImage image2 = ImageIO.read(inputStream);
                File outputFile = new File("saved.png");
                ImageIO.write(image2, "png", outputFile);
            }
        }
    }
}
