import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
                    Task t = new Task(id + "", title, description, status, time.toString());
                    TaskService.cachedTasks.put(id, t);
                }

            } else {
                System.out.println("Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void listTasks() {
        try {
            String listQuery = "SELECT * FROM Tasks;";
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery(listQuery);
            System.out.printf("%-5s | %-20s | %-30s | %-12s | %-20s%n",
                    "ID", "Title", "Description", "Status", "Created At");
            System.out.println(
                    "--------------------------------------------------------------------------------------------");
            while (results.next()) {
                System.out.printf("%-5d | %-20s | %-30s | %-12s | %-20s%n",
                        results.getInt("id"),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("status"),
                        results.getTimestamp("creationTime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> findTasks(int filter, String answer) throws Exception {
        ArrayList<Task> result = new ArrayList<>();
        switch (filter) {
            case 1:
                result.add(searchById(answer));
                break;
            case 2:
                result = searchByTitle(answer);
                break;
            case 3:
                result = searchByStatus(answer);
                break;

            default:
                throw new Exception("enter a correct number please!");
        }
        return result;
    }

    private Task searchById(String answer) throws SQLException {
        Task res = null;
        int id = Integer.parseInt(answer);
        if (TaskService.cachedTasks.containsKey(id)) {
            res = TaskService.cachedTasks.get(id);
        } else {
            String query = "SELECT * FROM Tasks WHERE id=(?)";
            PreparedStatement prepStatement = connection.prepareStatement(query);
            prepStatement.setInt(1, id);
            ResultSet results = prepStatement.executeQuery();
            while (results.next()) {
                res = new Task(results.getInt("id") + "",
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("status"),
                        results.getTimestamp("creationTime").toString());
            }
            TaskService.cachedTasks.put(id, res);
        }
        return res;

    }

    private ArrayList<Task> searchByStatus(String answer) throws SQLException {
        ArrayList<Task> res = new ArrayList<>();
        String query = "SELECT * FROM Tasks WHERE status=?";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, answer);
        ResultSet results = prepStatement.executeQuery();
        Task t = null;
        while (results.next()) {
            t = new Task(results.getInt("id") + "",
                    results.getString("title"),
                    results.getString("description"),
                    results.getString("status"),
                    results.getTimestamp("creationTime").toString());
            res.add(t);
        }
        return res;
    }

    private ArrayList<Task> searchByTitle(String answer) throws SQLException {
        ArrayList<Task> res = new ArrayList<>();
        String query = "SELECT * FROM Tasks WHERE title like ?";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, "%" + answer + "%");
        ResultSet results = prepStatement.executeQuery();
        Task t = null;
        while (results.next()) {
            t = new Task(results.getInt("id") + "",
                    results.getString("title"),
                    results.getString("description"),
                    results.getString("status"),
                    results.getTimestamp("creationTime").toString());
            res.add(t);
        }
        return res;
    }

    public void updateTaskStatus(String id, String status) throws SQLException {
        String updateQuery = "UPDATE Tasks set status = ? WHERE id= ?";
        PreparedStatement prep = connection.prepareStatement(updateQuery);
        prep.setString(2, id);
        prep.setString(1, status);

        String getTask = "Select * from Tasks WHERE id= ?";
        PreparedStatement stmt = connection.prepareStatement(getTask);
        stmt.setString(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            TaskService.cachedTasks.putIfAbsent(Integer.parseInt(id), new Task(results.getInt("id") + "",
                    results.getString("title"),
                    results.getString("description"),
                    results.getString("status"),
                    results.getTimestamp("creationTime").toString()));
        }

    }

    public void deleteOldCompletedTasks() throws SQLException {
        String searchQuery = "Select * from Tasks where status='done' ";
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(searchQuery);
        while (res.next()) {
            Timestamp time = res.getTimestamp("creationTime");
            long diff = System.currentTimeMillis() - time.getTime();
            long dayValue = (60 * 60 * 1000 * 24);
            if (diff >= dayValue) {
                deleteQuery(res.getInt("id"), res.getString("title"));
                System.out.println(diff + res.getString("title"));
            }
        }
    }

    private void deleteQuery(int id, String title) throws SQLException {
        String deleteQuery = "DELETE FROM Tasks WHERE id = ?";
        PreparedStatement prep = connection.prepareStatement(deleteQuery);
        prep.setInt(1, id);
        prep.executeQuery();
        TaskService.loggerService.log("Task deleted: '" + title + " '");
    }

}