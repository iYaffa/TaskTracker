import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
            PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);
            prepStatement.setString(1, title);
            prepStatement.setString(2, description);
            prepStatement.setString(3, status);
            result = prepStatement.execute();
            String id = prepStatement.getGeneratedKeys().toString();
            System.out.println(id);
            // Task t = new Task(title, description, status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}