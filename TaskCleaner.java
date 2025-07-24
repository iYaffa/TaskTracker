public class TaskCleaner implements Runnable {
    private TaskRepository taskRepository;
    boolean running = false;

    public TaskCleaner(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            taskRepository.deleteOldCompletedTasks();
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                running = false;
                e.printStackTrace();
            }

        }
    }

}
