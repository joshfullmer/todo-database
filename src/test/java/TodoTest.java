import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoTest {

    private Database database;

    @Before
    public void setUp() {
        database = new Database();
        database.initializeDatabase();
    }

    @Test
    public void testTodoCreation() {
        String dateString = "Aug 6 2018 11:29:33.893 UTC";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
        String title = "Test Title";
        String description = "Test Description";
        Todo todo = new Todo(date, title, description);
        int todoId = Todo.create(todo);
        assertEquals(1, todoId);
        assertEquals(date, todo.getDueDate());
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
    }

    @Test
    public void testTodoListRetrieval() {
        String dateString = "Aug 6 2018 11:29:33.893 UTC";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
        String title = "Test Title";
        String description = "Test Description";
        Todo todo = new Todo(date, title, description);
        int todoCount = 3;
        for (int i = 0; i < todoCount; i++) {
            Todo.create(todo);
        }
        ArrayList<Todo> todos = Todo.getAllTodos();
        assertEquals(todoCount, todos.size());
    }

    @Test
    public void testTodoListRetrievalDueSoon() {
        Date date = new Date();
        DateTimeZone timeZone = DateTimeZone.forID("America/Phoenix");
        DateTime now = new DateTime(date, timeZone);
        DateTime tomorrow = now.plusDays(1);
        DateTime nextWeek = now.plusWeeks(1);
        String title = "Test Title";
        String description = "Test Description";

        Todo todo1 = new Todo(now.toDate(), title, description);
        Todo todo2 = new Todo(tomorrow.toDate(), title, description);
        Todo todo3 = new Todo(nextWeek.toDate(), title, description);
        Todo.create(todo1);
        Todo.create(todo2);
        Todo.create(todo3);

        ArrayList<Todo> todos =  Todo.getTodosDueSoon();
        assertEquals(2, todos.size());
        ArrayList<Todo> allTodos = Todo.getAllTodos();
        assertEquals(3, allTodos.size());
    }

    @Test
    public void testTodoListRetrievalByCategory() {
        String categoryInsert = "INSERT INTO Category(Name) VALUES('Test Category')";

        String categoryAssign = "INSERT INTO TodoCategoryAssign(TodoId, CategoryId) VALUES(1, 1)";

        Todo todo = new Todo(new Date(), "Test Title", "Test Description");
        Todo.create(todo);

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(categoryInsert);
            statement.execute(categoryAssign);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        todo = new Todo(new Date(), "Test Title", "Test Description");
        Todo.create(todo);

        ArrayList<Todo> todos = Todo.getTodosInCategory(1);
        assertEquals(1, todos.size());
    }

}
