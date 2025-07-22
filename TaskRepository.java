import java.sql.Connection;
import java.sql.DriverManager;

public class TaskRepository {
    private final String URL = "jdbc:mariadb://localhost:3306/TaskTracker";
    private final String USERNAME = "root";
    private final String PASSWORD = System.getenv("DB_PASSWORD");

    public TaskRepository() {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}