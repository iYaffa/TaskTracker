import java.util.Scanner;

public class Main {

public static void main(String[] args){
	while(true){
	String timeOfTheDay = "";
	System.out.println("Good" + timeOfTheDay + "!");
	System.out.println("Choose one of the following: \n1. Add a new task \n2. Update task status. \n3. View tasks.\n4. View log file.\n5. Exit");
	Scanner input = new Scanner(System.in);
	if(input.hasNextInt()){
	int choice = input.nextInt();
	switch(choice){

		case 1: System.out.println("add");
			break;
		case 2: System.out.println("add");
			break;
		case 3: System.out.println("add");
break;
		case 4: System.out.println("add");
			break;
		case 5: break;
		default: System.out.println("Enter a valid number pleas!");

	}}
	break;
	}
	}
}


