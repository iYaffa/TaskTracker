import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TaskRepository {
    private final String URL = "jdbc:mariadb://localhost:3306/TaskTracker";
    private final String USERNAME = "yaffa";
    private final String PASSWORD = "1234";
    private Connection connection = null;

    public TaskRepository() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean addTask(String title, String description, String status) {
        boolean result = false;
        try {
            String sqlQuery = "INSERT INTO Tasks (title, description, status) VALUES (?, ?, ?)";
            PreparedStatement prepStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, title);
            prepStatement.setString(2, description);
            prepStatement.setString(3, status);
            result = prepStatement.executeUpdate() > 0;
            if (result) {
                ResultSet res = prepStatement.getGeneratedKeys();
                // String id = res.getInt(1) + "";
                // System.out.println(id);
                if (res.next()) {
                    int id = res.getInt(1);
                    System.out.println("Inserted ID: " + id);
                }

            } else {
                System.out.println("rerasdjkfalkjf");
            }
            // Task t = new Task(title, description, status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}