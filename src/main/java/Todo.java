import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Todo {

    private int id;
    private Date dueDate;
    private String title;
    private String description;

    Todo(Date dueDate, String title, String description) {
        if (dueDate == null) {
            throw new IllegalArgumentException("DueDate cannot be null");
        }

        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }

        if (title.equals("")) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }

        if (description.equals("")) {
            throw new IllegalArgumentException("Description cannot be empty");
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
            preparedStatement.setString(1, DateHandler.dateToString(todo.getDueDate()));
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

    static List<Todo> getAllTodos() {
        String sql = "SELECT * FROM Todo;";

        return getTodosFromSQL(sql);
    }

    static List<Todo> getTodosDueSoon() {
        // Returns array of tasks due today or tomorrow
        String sql = "SELECT * FROM Todo\n" +
                     "WHERE date(Todo.DueDate) = date('now')\n" +
                     "   OR date(Todo.DueDate) = date('now', '+1 days');";

        return getTodosFromSQL(sql);
    }

    static List<Todo> getTodosInCategory(int categoryId) {
        // Gets a list of all todos in a given category, by category ID

        Database database = new Database();
        String sql = "SELECT * FROM Todo\n" +
                     "INNER JOIN TodoCategoryAssign\n" +
                     "ON Todo.Id=TodoCategoryAssign.TodoId\n" +
                     "WHERE TodoCategoryAssign.CategoryId=?;";

        List<Todo> todos = new ArrayList<>();

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

    private static List<Todo> getTodosFromSQL(String sql) {
        Database database = new Database();
        List<Todo> todos = new ArrayList<>();

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            todos = getTodosFromResultSet(resultSet);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return todos;
    }

    private static List<Todo> getTodosFromResultSet(ResultSet resultSet) throws SQLException {
        List<Todo> todos = new ArrayList<>();

        while (resultSet.next()) {
            Date date = null;
            try {
                date = DateHandler.stringToDate(resultSet.getString("DueDate"));
            } catch(ParseException pe) {
                pe.printStackTrace();
            }

            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            Todo todo = new Todo(date, title, description);
            todos.add(todo);
        }

        return todos;
    }
}
