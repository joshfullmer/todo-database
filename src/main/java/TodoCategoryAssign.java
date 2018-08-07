import java.sql.*;

public class TodoCategoryAssign {

    private int todoId;
    private int categoryId;

    public static boolean categorizeTodo(int todoId, int categoryId) {
        // returns true if td was categorized and false if it was already in the category
        Database database = new Database();
        String existingCategoryCheck = "SELECT * FROM TodoCategoryAssign\n" +
                                       "WHERE TodoId=? AND CategoryId=?;";
        String categorize = "INSERT INTO TodoCategoryAssign(TodoId, CategoryId)\n" +
                            "VALUES(?, ?);";

        boolean success = false;

        try (Connection connection = database.connect()) {
            int resultCount = 0;
            PreparedStatement preparedStatement = connection.prepareStatement(existingCategoryCheck);
            preparedStatement.setInt(1, todoId);
            preparedStatement.setInt(2, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                success = true;
                preparedStatement = connection.prepareStatement(categorize);
                preparedStatement.setInt(1, todoId);
                preparedStatement.setInt(2, categoryId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return success;
    }
}
