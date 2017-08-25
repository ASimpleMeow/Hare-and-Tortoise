/**
 * The Driver class will communicate to the users,
 * it will receive and display text from the user.
 * It has the main menu from which commands can be selected and 
 * the game effectively is played here.
 * 
 * @author 		Oleksandr Kononov, Jake Phillips
 * @version		3.0 (29.Feb.2016)
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
public class Driver {
	
	private static Scanner input; //Used for user input
	private ArrayList<User> users; //ArrayList of users active in the game
	private ArrayList<User> finishedUsers; //ArrayList of users finished (and inactive in the game)
	private Board board; //The board object.
	private Game game; //The game object.
	
	/**
	 * This will be the starting point for the whole program
	 * and will create the constructor for the driver class
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Driver();
	}
	
	/**
	 * The constructor for the Driver class is where the 
	 * constructors for out objects and ArrayLists will be called 
	 * and finally the mainMenu will start
	 */
	public Driver()
	{
		input = new Scanner(System.in);
		users = new ArrayList<User>();
		finishedUsers = new ArrayList<User>();
		board = new Board();
		game = new Game();
		mainMenu();
	}
	
	/**
	 * mainMenuDisplay will display the main menu options
	 * to the user and receive the input.
	 * 
	 * @return An integer which the user inputs
	 */
	private int mainMenuDisplay() throws Exception
	{
		System.out.println("********************************************************");
		System.out.println("*                                                      *");
		System.out.println("*   	       HARE AND TORTOISE GAME                  *");
		System.out.println("*   ------------------------------------------------   *");
		System.out.println("*                                                      *");
		System.out.println("*    1) Play Game                                      *");
		System.out.println("*    2) Rules and Instructions                         *");
		System.out.println("*    0) Exit                                           *");
		System.out.println("*                                                      *");
		System.out.println("********************************************************");
		System.out.print(">>");
		return input.nextInt();
	}
	
	/**
	 * mainMenu will receive input from mainMenuDisplay 
	 * and handle the input to navigate through the menu options.
	 */
	private void mainMenu()
	{
		int selection; 
		try
		{
			selection = mainMenuDisplay(); 
		}
		catch (Exception e)
		{
			selection = -1; //In case of an exception -1 will signify error
		}
		while (selection != 0)
		{
			switch(selection)
			{
			case 1:
				userSetUp();
				break;
			case 2:
				game.displayHelp();
				break;
			default:
				System.out.println("You entered an invalid selection");
				break;
			}
			//Pause the screen so the user can read the response
			System.out.println("\n\nPress Enter to proceed");
			input.nextLine();
			input.nextLine(); //Scanner bug fix.
			try
			{
				selection = mainMenuDisplay();
			}
			catch (Exception e)
			{
				selection = -1;
			}
		}
		//Selection zero was received, therefore exit the game.
		System.out.println("Exiting The Game");
		System.exit(0);
	}
	
	/**
	 * This method will ask the user for the number of players
	 * which are going to play the game and after validation 
	 * they will be added to the ArrayList.
	 */
	private void userSetUp()
	{
		System.out.print("Plese enter number of players (2-6):");
		int numberOfUsers;
		try
		{
			numberOfUsers = input.nextInt();
		}
		catch (Exception e)
		{
			System.out.println("Please enter a valid integer!");
			input.nextLine();
			input.nextLine();
			numberOfUsers = -1;
			userSetUp();
		}
		if ((numberOfUsers >=2) && (numberOfUsers <= 6))
		{
			for (int i=0; i<numberOfUsers; i++)
			{
				users.add(new User(i+1));
			}
			input.nextLine(); //Scanner bug fix
			runGame();
		}
		else
		{
			userSetUp();
		}
		
	}
	
	/**
	 * runGame is the main method for the game, and all
	 * game related tasks will be carried out here.
	 * Here the game starts. It will also take user input and send
	 * it to the game class to be processed.
	 */
	private void runGame()
	{
		while(true)//Run the following code forever
		{
			for (User user:users) //Run the following code for each user
			{
				game.autoFinish(users, finishedUsers, user); //Checks if all other users finished
				int command=0; 
				int currentUserPosition = user.getPosition();
				if (user.getSkipTurn()==false && user.getUserActive())//If user's still not finished and didn't skip a turn
				{
					while(command!=1)//Loop until you get confirmation to pass the turn
					{
						System.out.println("*******************************************");//Adding extra space
						System.out.print("User "+user.getUserNumber()+" >>");
						HashSet<String> message = game.inputReader(input.nextLine());//takes in full message input
						String actionCommand = game.recogniseCommand(message);//key-word command
						int actionInteger = game.recogniseInteger(message);//integer to be used with key-word
						game.calculateUserPlace(user, users);//Check your's place (relative to others)
						command = game.executeCommand(actionCommand,actionInteger, user,users,board,finishedUsers);//Execute command
						game.calculateUserPlace(user, users);//Check your's place (relative to others)
						if (user.getPosition() != currentUserPosition)//If user hasn't moved check the square effect he's on
							game.checkSquare(user,currentUserPosition,users,board);
					}
				}
				if (currentUserPosition == user.getPosition())//Reset user's skipTurn
					user.setSkipTurn(false);
				System.out.println("*******************************************");
			}
		}
	}
}
