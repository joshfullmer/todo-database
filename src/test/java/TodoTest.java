import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoTest {

    private Database database;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        database = new Database();
        database.initializeDatabase();
    }

    @Test
    public void throwsIllegalArgumentExceptionIfDateIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("DueDate cannot be null");
        Date date = null;
        new Todo(date, "Test Title", "Test Description");
    }

    @Test
    public void throwsIllegalArgumentExceptionIfTitleIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Title cannot be null");
        new Todo(new Date(), null, "Test Description");
    }

    @Test
    public void throwsIllegalArgumentExceptionIfTitleIsEmpty() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Title cannot be empty");
        new Todo(new Date(), "", "Test Description");
    }

    @Test
    public void throwsIllegalArgumentExceptionIfDescriptionIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Description cannot be null");
        new Todo(new Date(), "Test Title", null);
    }

    @Test
    public void throwsIllegalArgumentExceptionIfDescriptionIsEmpty() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Description cannot be empty");
        new Todo(new Date(), "Test Title", "");
    }

    @Test
    public void testTodoCreation() {
        Date date = new Date();
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
        Date date = new Date();
        String title = "Test Title";
        String description = "Test Description";
        Todo todo = new Todo(date, title, description);
        int todoCount = 3;
        for (int i = 0; i < todoCount; i++) {
            Todo.create(todo);
        }
        List<Todo> todos = Todo.getAllTodos();
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

        List<Todo> todos =  Todo.getTodosDueSoon();
        assertEquals(2, todos.size());
        List<Todo> allTodos = Todo.getAllTodos();
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

        List<Todo> todos = Todo.getTodosInCategory(1);
        assertEquals(1, todos.size());
    }

}
