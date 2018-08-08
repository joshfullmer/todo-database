import java.sql.*;
import java.util.ArrayList;

public class Category {

    private int id;
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    private void setId(int id) {
        this.id = id;
    }

    public static int create(Category category) {
        // returns the id of the created category

        Database database = new Database();

        // Check if category exists by the same name
        String categoryExists = "SELECT * FROM Category WHERE Name=?";

        String createCategory = "INSERT INTO Category(Name) VALUES(?);";

        try (Connection connection = database.connect();
             PreparedStatement preparedStatement1 = connection.prepareStatement(categoryExists);
             PreparedStatement preparedStatement2 = connection.prepareStatement(createCategory)) {
            preparedStatement1.setString(1, category.getName());

            try (ResultSet resultSet = preparedStatement1.executeQuery()) {
                if (resultSet.next()) {
                    throw new IllegalArgumentException("Category exists by this name");
                }
            }

            preparedStatement2.setString(1, category.getName());
            preparedStatement2.executeUpdate();

            try (ResultSet resultSet = preparedStatement2.getGeneratedKeys()) {
                if (resultSet.next()) {
                    category.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return category.getId();
    }

    public static ArrayList<Category> getAllCategories() {
        Database database = new Database();
        String sql = "SELECT * FROM Category;";

        ArrayList<Category> categories = new ArrayList<>();

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                Category category = new Category(name);
                categories.add(category);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }


        return categories;
    }

    public static String getCategoryName(int id) {
        Database database = new Database();
        String sql = "SELECT Name FROM Category WHERE Id=?;";

        String name = "";

        try (Connection connection = database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                name = resultSet.getString("Name");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return name;
    }
}
