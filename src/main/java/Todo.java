import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

class Todo {

    private int id;
    private Date dueDate;
    private String title;
    private String description;

    Todo(Date dueDate, String title, String description) {
        if (dueDate == null) {
            throw new IllegalArgumentException("DueDate cannot be null");
        }

        if (title.equals("")) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        if (description.equals("")) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.dueDate = dueDate;
        this.title = title;
        this.description = description;
    }

    private int getId() {
        return this.id;
    }

    Date getDueDate() {
        return this.dueDate;
    }

    String getTitle() {
        return this.title;
    }

    String getDescription() {
        return this.description;
    }

    private void setId(int id) {
        this.id = id;
    }

    static int create(Todo todo) {
        Database database = new Database();
        String sql = "INSERT INTO Todo(DueDate, Title, Description) VALUES(?,?,?)";

        try (Connection connection = database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, todo.getDueDate().getTime());
            preparedStatement.setString(2, todo.getTitle());
            preparedStatement.setString(3, todo.getDescription());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    todo.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return todo.getId();
    }

    static ArrayList<Todo> getAllTodos() {
        String sql = "SELECT * FROM Todo;";

        return getTodosFromSQL(sql);
    }

    static ArrayList<Todo> getTodosDueSoon() {
        // Returns array of tasks due today or tomorrow
        String sql = "SELECT * FROM Todo\n" +
                     "WHERE date(datetime(DueDate / 1000, 'unixepoch')) = date('now')\n" +
                     "   OR date(datetime(DueDate / 1000, 'unixepoch')) = date('now', '+1 days');";

        return getTodosFromSQL(sql);
    }

    static ArrayList<Todo> getTodosInCategory(int categoryId) {
        // Gets a list of all todos in a given category, by category ID

        Database database = new Database();
        String sql = "SELECT * FROM Todo\n" +
                     "INNER JOIN TodoCategoryAssign\n" +
                     "ON Todo.Id=TodoCategoryAssign.TodoId\n" +
                     "WHERE TodoCategoryAssign.CategoryId=?;";

        ArrayList<Todo> todos = new ArrayList<>();

        try (Connection connection = database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            todos = getTodosFromResultSet(resultSet);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return todos;
    }

    private static ArrayList<Todo> getTodosFromSQL(String sql) {
        Database database = new Database();
        ArrayList<Todo> todos = new ArrayList<>();

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            todos = getTodosFromResultSet(resultSet);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return todos;
    }

    private static ArrayList<Todo> getTodosFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Todo> todos = new ArrayList<>();

        while (resultSet.next()) {
            Date date = new Date(resultSet.getLong("DueDate"));
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            Todo todo = new Todo(date, title, description);
            todos.add(todo);
        }

        return todos;
    }
}
