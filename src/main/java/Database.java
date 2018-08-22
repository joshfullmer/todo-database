import java.sql.*;

class Database {

    Connection connect() {
        String url = "jdbc:sqlite:todo.db";
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    void initializeDatabase() {

        String todoDrop = "DROP TABLE IF EXISTS Todo;";

        String todoCreate = "CREATE TABLE IF NOT EXISTS Todo (\n"
                          + " Id integer PRIMARY KEY, \n"
                          + " DueDate text NOT NULL, \n"
                          + " Title text NOT NULL, \n"
                          + " Description text NOT NULL\n"
                          + ");";

        String categoryDrop = "DROP TABLE IF EXISTS Category;";

        String categoryCreate = "CREATE TABLE IF NOT EXISTS Category (\n" +
                                " Id integer PRIMARY KEY, \n" +
                                " Name text NOT NULL\n" +
                                ");";

        String todoCatDrop = "DROP TABLE IF EXISTS TodoCategoryAssign;";

        String todoCatCreate = "CREATE TABLE IF NOT EXISTS TodoCategoryAssign (\n" +
                               " Id integer PRIMARY KEY, \n" +
                               " TodoId integer REFERENCES Todo(Id), \n" +
                               " CategoryId integer REFERENCES Category(Id)\n" +
                               ");";

        try (Connection connection = this.connect();
                Statement statement = connection.createStatement()) {
            statement.execute(todoDrop);
            statement.execute(todoCreate);
            statement.execute(categoryDrop);
            statement.execute(categoryCreate);
            statement.execute(todoCatDrop);
            statement.execute(todoCatCreate);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
