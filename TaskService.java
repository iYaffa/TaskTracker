import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskService {
	static TaskRepository taskRepository = new TaskRepository();
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static HashMap<Integer, String> statusChoices = new HashMap<>();
	static HashMap<Integer, Task> cachedTasks = new HashMap<>();

	public static void main(String[] args) throws Exception {
		statusChoices.put(1, "pending");
		statusChoices.put(2, "inProgress");
		statusChoices.put(3, "done");
		statusChoices.put(4, "canceled");

		boolean stop = false;
		try {
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
							System.out.println("view log");
							break;

						case 5:
							stop = true;
							System.out.println("Thank you for trying our system! Have a good day!");
							input.close();
							break;
						default:
							System.out.println("Enter a valid number pleas!");
					}
				}
				System.out.println(
						"**************************");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException r) {
			r.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		String status;
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
			id = tasks.get(0).getId();
		}
		System.out.println("Please enter the corresponding number for status to change: \n" +
				"1. Pending\n" +
				"2. In progress\n" +
				"3. Done\n" +
				"4. Canceled\n");

		status = statusChoices.get(Integer.parseInt(input.readLine().trim()));
		System.out.println("sttt" + status);
		taskRepository.updateTaskStatus(id, status);
	}

	private static ArrayList<Task> searchForTasks() throws Exception {
		System.out.println(
				"Please enter choose from the following: " + "\n1. Find task by id number. \n2. Find task by title.\n" + //
						"3. Find task by status.");
		int filter = Integer.parseInt(input.readLine().trim());
		String answer = "";
		if (filter == 3) {
			System.out.print(
					"Please enter the corresponding number for status: \n" +
							"1. Pending\n" +
							"2. In progress\n" +
							"3. Done\n" +
							"4. Canceled\n");

			answer = statusChoices.get(input.read());
		} else {
			System.out.print(
					"Please enter the word you want to search for: \n");

			answer = input.readLine();
		}
		return taskRepository.findTasks(filter, answer);
	}

	// the method to add a new task
	private static void addTask() throws IOException {

		System.out.print("Please enter the task's title: ");
		String title = input.readLine();

		System.out.print("Please enter the task's description: ");
		String description = input.readLine();

		System.out.print(
				"Please enter the corresponding number for status: \n" +
						"1. Pending\n" +
						"2. In progress\n" +
						"3. Done\n" +
						"4. Canceled\n");

		String statusInput = input.readLine();
		taskRepository.addTask(title, description, statusChoices.get(Integer.valueOf(statusInput)));
	}

	// method to get the time of the day for the welcoming message
	private static String getTimeOfTheDay() {
		String message = "morning";
		int time = Integer.valueOf(LocalTime.now().toString().split(":")[0]); // getting the hour from the local time
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
