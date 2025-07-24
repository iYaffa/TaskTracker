import java.sql.SQLException;

public class TaskCleaner implements Runnable {
    private TaskRepository taskRepository;
    boolean running = true;

    public TaskCleaner(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run() {
        while (running) {
            try {

                taskRepository.deleteOldCompletedTasks();
                Thread.sleep(60 * 1 * 1000);
            } catch (InterruptedException e) {
                running = false;
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}
