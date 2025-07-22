import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDateTime;

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
            LocalDateTime time = LocalDateTime.now();
            String sqlQuery = "INSERT INTO Tasks (title, description, status, creationTime) VALUES (?, ?, ?, ?)";
            PreparedStatement prepStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, title);
            prepStatement.setString(2, description);
            prepStatement.setString(3, status);
            prepStatement.setString(4, time.toString());

            result = prepStatement.executeUpdate() > 0;
            if (result) {
                ResultSet res = prepStatement.getGeneratedKeys();

                if (res.next()) {
                    int id = res.getInt(1);
                    Task t = new Task(id + "", title, description, status, time);
                    
                }

            } else {
                System.out.println("Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}