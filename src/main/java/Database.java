import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static void initializeDatabase() {
        String url = "jdbc:sqlite:todo.db";

        String todo_sql = "CREATE TABLE IF NOT EXISTS Todo (\n"
                        + " Id integer PRIMARY KEY, \n"
                        + " DueDate date NOT NULL, \n"
                        + " Title text NOT NULL, \n"
                        + " Description text NOT NULL, \n"
                        + ");";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(todo_sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
