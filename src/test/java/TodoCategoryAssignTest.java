import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class TodoCategoryAssignTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        Database database = new Database();
        database.initializeDatabase();
    }

    @Test
    public void throwsIllegalArgumentExceptionIfTodoDoesNotExist() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Todo not found");
        Category.create(new Category("Test Category"));
        TodoCategoryAssign.categorizeTodo(1, 1);
    }

    @Test
    public void throwsIllegalArgumentExceptionIfCategoryDoesNotExist() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Category not found");
        Todo.create(new Todo(new Date(), "Test Title", "Test Description"));
        TodoCategoryAssign.categorizeTodo(1, 1);
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
