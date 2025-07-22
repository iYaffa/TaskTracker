import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	static TaskRepository taskRepository = new TaskRepository();
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {

		boolean stop = false;
		while (!stop) {
			String timeOfTheDay = getTimeOfTheDay();
			System.out.println("Good " + timeOfTheDay + "!");
			System.out.println(
					"Choose one of the following: \n1. Add a new task \n2. Update task status. \n3. View tasks.\n4. View log file.\n5. Exit");

			if (input.hasNextInt()) {
				int choice = input.nextInt();
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

	private static void addTask() {
			
		HashMap<Integer, String> statusChoices = new HashMap<>();
		statusChoices.put(1, "pending");
		statusChoices.put(2, "inProgress");
		statusChoices.put(3, "done");
		statusChoices.put(4, "canceled");

		if (input.hasNext()) {
			System.out.println(
				"Please enter the task's title: ");
			String title = 
			String[] info = input.nextLine().split(" | ");
			System.out.println(info.length);
			taskRepository.addTask(info[0], info[1], statusChoices.get(Integer.valueOf(info[3])));
		}
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
