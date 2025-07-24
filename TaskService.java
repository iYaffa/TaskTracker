import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskService {
	static final TaskRepository taskRepository = new TaskRepository();
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static HashMap<Integer, Task> cachedTasks = new HashMap<>();
	static final LoggerService loggerService = new LoggerService();

	public static void main(String[] args) throws Exception {

		boolean stop = false;
		try {

			TaskCleaner cleaner = new TaskCleaner(taskRepository);
			Thread cleanerThread = new Thread(cleaner);
			cleanerThread.setDaemon(true); // so JVM can exit without waiting
			cleanerThread.start();
			while (!stop) {
				String timeOfTheDay = getTimeOfTheDay();
				System.out.println("Good " + timeOfTheDay + "!");
				System.out.println(
						"**************************\nChoose one of the following: \n1. Add a new task \n2. Update task status. \n3. View tasks.\n4. View log file.\n5. Exit");

				String line = input.readLine();
				if (line != null && !line.isEmpty()) {
					int choice = Integer.parseInt(line.trim());
					switch (choice) {

						case 1:
							addTask();
							break;
						case 2:
							updateTaskStatus();
							break;
						case 3:
							listTasks();
							break;
						case 4:
							viewLogs();
							break;
						case 5:
							stop = true;
							System.out.println("Thank you for trying our system! Have a good day!");
							input.close();
							break;
						default:
							System.out.println("Enter a valid number please!");
					}
				}
				System.out.println("**************************");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException r) {
			r.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void viewLogs() {
		String logFilePath = "taskLog.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// listing all tasks
	private static void listTasks() {
		taskRepository.listTasks();
	}

	private static void updateTaskStatus() throws Exception {
		ArrayList<Task> tasks = searchForTasks();
		String id;
		int size = tasks.size();

		if (size > 1) {
			System.out.println("Please choose the id of the desired task from the following tasks: ");
			for (Task task : tasks) {
				System.out.println(task);
			}
			id = input.readLine();
		} else if (size < 1) {
			throw new Exception("The id you've entered doesn't exist");
		} else {
			System.out.println(tasks.get(0));
			id = String.valueOf(tasks.get(0).getId());
		}

		String status = getStatusChoice();

		taskRepository.updateTaskStatus(id, status);
		String title = cachedTasks.get(Integer.parseInt(id)).getTitle();
		loggerService.log("Task updated: " + title + " -> '" + status + "'");
	}

	private static ArrayList<Task> searchForTasks() throws Exception {
		System.out.println(
				"Please choose from the following: " +
						"\n1. Find task by id number." +
						"\n2. Find task by title." +
						"\n3. Find task by status.");
		int filter = Integer.parseInt(input.readLine().trim());
		String answer;
		if (filter == 3) {
			answer = getStatusChoice();
		} else {
			System.out.println("\nPlease enter the title/id you want to search for:");
			answer = input.readLine();
		}

		return taskRepository.findTasks(filter, answer);
	}

	// method to add a new task
	private static void addTask() throws IOException {
		System.out.print("Please enter the task's title: ");
		String title = input.readLine();

		System.out.print("Please enter the task's description: ");
		String description = input.readLine();

		String status = getStatusChoice();

		if (taskRepository.addTask(title, description, status)) {
			loggerService.log("Task added: '" + title + "'");
		}
	}

	// method to get the status from the user
	private static String getStatusChoice() throws IOException {
		HashMap<Integer, String> statusChoices = new HashMap<>();

		statusChoices.put(1, "pending");
		statusChoices.put(2, "inProgress");
		statusChoices.put(3, "done");
		statusChoices.put(4, "canceled");
		System.out.print(
				"Please enter the corresponding number for status: \n" +
						"1. Pending\n" +
						"2. In progress\n" +
						"3. Done\n" +
						"4. Canceled\n");

		String choice = input.readLine().trim();
		String status = statusChoices.get(Integer.parseInt(choice));
		return status;
	}

	// method to get the time of the day for the welcoming message
	private static String getTimeOfTheDay() {
		String message = "morning";
		int time = Integer.parseInt(LocalTime.now().toString().split(":")[0]);
		if (time >= 6 && time < 12) {
			message = "morning";
		} else if (time >= 12 && time <= 19) {
			message = "afternoon";
		} else {
			message = "evening";
		}
		return message;
	}
}
