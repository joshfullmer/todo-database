import java.sql.*;

public class Database {

    private Connection connect() {
        String url = "jdbc:sqlite:todo.db";
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private void initializeDatabase() {


        String todo_sql = "CREATE TABLE IF NOT EXISTS Todo (\n"
                        + " Id integer PRIMARY KEY, \n"
                        + " DueDate real NOT NULL, \n"
                        + " Title text NOT NULL, \n"
                        + " Description text NOT NULL\n"
                        + ");";

        try (Connection connection = this.connect();
                Statement statement = connection.createStatement()) {
            statement.execute(todo_sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addTask() {
        String sql = "INSERT INTO Todo(DueDate, Title, Description) VALUES(?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, 250000.0);
            preparedStatement.setString(2, "Hello");
            preparedStatement.setString(3, "Goodbye");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Todo getTask(int id) {
        String sql = "SELECT * FROM Todo WHERE Id=?";
        ResultSet resultSet = null;
        Todo todo = null;
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            todo = new Todo(resultSet.getInt("Id"),
                            resultSet.getDate("DueDate"),
                            resultSet.getString("Title"),
                            resultSet.getString("Description"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return todo;
    }

    public static void main(String[] args) {
        Database database = new Database();
        database.initializeDatabase();
        System.out.println("Initialized database.");
        database.addTask();
        System.out.println("Created Task.");
        Todo todo = database.getTask(1);
        System.out.println(todo.getTitle());
    }
}
