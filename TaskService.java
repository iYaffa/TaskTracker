import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.HashMap;

public class TaskService {
	static TaskRepository taskRepository = new TaskRepository();
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	public static HashMap<Integer, Task> cachedTasks = new HashMap<>();

	public static void main(String[] args) throws IOException {

		boolean stop = false;
		while (!stop) {
			String timeOfTheDay = getTimeOfTheDay();
			System.out.println("Good " + timeOfTheDay + "!");
			System.out.println(
					"Choose one of the following: \n1. Add a new task \n2. Update task status. \n3. View tasks.\n4. View log file.\n5. Exit");

			String line = input.readLine();
			if (line != null && !line.isEmpty()) {
				int choice = Integer.parseInt(line.trim());
				switch (choice) {

					case 1:
						addTask();
						stop = true;
						break;
					case 2:
						System.out.println("update");
						break;
					case 3:
						System.out.println("view tasks");
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
		}
	}

	// the method to add a new task
	private static void addTask() throws IOException {

		HashMap<Integer, String> statusChoices = new HashMap<>();
		statusChoices.put(1, "pending");
		statusChoices.put(2, "inProgress");
		statusChoices.put(3, "done");
		statusChoices.put(4, "canceled");

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
