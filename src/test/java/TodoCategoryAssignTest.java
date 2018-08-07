import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class TodoCategoryAssignTest {

    private Database database;

    @Before
    public void setUp() {
        database = new Database();
        database.initializeDatabase();
    }

    @Test
    public void testCategorizeTodo() {
        int todoId = Todo.create(new Todo(new Date(), "Test Todo", "Test Description"));
        int categoryId = Category.create(new Category("Test Category"));
        boolean success = TodoCategoryAssign.categorizeTodo(todoId, categoryId);
        assertTrue(success);
        assertEquals(1, Todo.getTodosInCategory(categoryId).size());

        // Check for duplicate categorization. Method returns false if categorization already exists
        success = TodoCategoryAssign.categorizeTodo(todoId, categoryId);
        assertFalse(success);
    }
}
