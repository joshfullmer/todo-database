import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

public class CategoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Database database;

    @Before
    public void setUp() {
        database = new Database();
        database.initializeDatabase();
    }

    @Test
    public void throwsIllegalArgumentExceptionIfCategoryExists() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Category exists by this name");
        Category.create(new Category("Test Category"));
        Category.create(new Category("Test Category"));
    }

    @Test
    public void testCategoryCreate() {
        String name = "Test Category";
        Category category = new Category(name);
        int categoryId = Category.create(category);
        assertEquals(1, categoryId);
        assertEquals("Test Category", category.getName());
    }

    @Test
    public void testCategoryListRetrieval() {
        String cat1 = "Test Category 1";
        String cat2 = "Test Category 2";
        String cat3 = "Test Category 3";
        Category category1 = new Category(cat1);
        Category category2 = new Category(cat2);
        Category category3 = new Category(cat3);
        Category.create(category1);
        Category.create(category2);
        Category.create(category3);

        ArrayList<Category> categories = Category.getAllCategories();
        assertEquals(3, categories.size());
    }

    @Test
    public void testCategoryNameRetrieval() {
        String categoryName = "Test Category";
        Category category = new Category(categoryName);
        int categoryId = Category.create(category);
        assertEquals(categoryName, Category.getCategoryName(categoryId));

    }
}
