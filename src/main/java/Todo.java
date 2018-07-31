import java.sql.Connection;
import java.util.Date;

public class Todo {

    private int id;
    private Date dueDate;
    private String title;
    private String description;

    public static int addTodo(Date dueDate, String title, String description) {
        // TODO: Build addTodo
        // returns the id of the task that's created

        return 0;
    }

    public static Todo[] getAllTodos() {
        // TODO: Build getAllTodos

        return new Todo[0];
    }

    public static Todo[] getTodosDueSoon() {
        // TODO: Build getTodosDueSoon
        // Returns a list of todos that are due today or tomorrow

        return new Todo[0];
    }

    public static Todo[] getTodosInCategory(int categoryId) {
        // TODO: Build getTodosInCategory

        return new Todo[0];
    }
}
