import java.sql.*;

class TodoCategoryAssign {

    static boolean categorizeTodo(int todoId, int categoryId) {
        // returns true if td was categorized and false if it was already in the category
        Database database = new Database();
        String todoExists = "SELECT * FROM Todo WHERE Id=?";
        String categoryExists = "SELECT * FROM Category WHERE Id=?";
        String categorizationExists = "SELECT * FROM TodoCategoryAssign\n" +
                                       "WHERE TodoId=? AND CategoryId=?;";
        String categorize = "INSERT INTO TodoCategoryAssign(TodoId, CategoryId)\n" +
                            "VALUES(?, ?);";

        boolean success = false;

        try (Connection connection = database.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(todoExists);
            preparedStatement.setInt(1, todoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("Todo not found");
                }
            }

            preparedStatement = connection.prepareStatement(categoryExists);
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("Category not found");
                }
            }

            preparedStatement = connection.prepareStatement(categorizationExists);
            preparedStatement.setInt(1, todoId);
            preparedStatement.setInt(2, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    success = true;
                    preparedStatement = connection.prepareStatement(categorize);
                    preparedStatement.setInt(1, todoId);
                    preparedStatement.setInt(2, categoryId);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return success;
    }
}
