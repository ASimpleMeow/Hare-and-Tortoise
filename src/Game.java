/**
 * This class is used to receive and process game related
 * actions by the user, then call for methods in the Driver class,
 * which has access to all data to perform that action.
 * 
 * @author 		Oleksandr Kononov, Jake Phillips, Nigel Farrell.
 * @version 	7.0 (11.Mar.2016)
 */

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;


public class Game {
	private Scanner input;
	private HashMap<String,String> commands; 
	private Random random;
	
	/**
	 * The constructor for the class will fulfill all pre-game
	 * conditions, such as filling in the command map.
	 */
	public Game()
	{
		input = new Scanner(System.in);
		commands = new HashMap<String,String>();
		random = new Random();
		fillCommandMap();
	}
	
	/**
	 * This method will take in the command input from the user 
	 * and process it to recognise the command in the sentence.
	 * 
	 * @return A HashSet with broken up words from the input line.
	 */
	public HashSet<String> inputReader(String message)
	{
		String[] inputWords = message.trim().toLowerCase().split(" ");
		HashSet<String> words = new HashSet<String>();
		for (String word : inputWords)
		{
			words.add(word);
		}
		return words;
	}
	
	/**
	 * This method will fill the command map with commands that
	 * will be used and recognised to play the game.
	 */
	private void fillCommandMap()
	{
		commands.put("forward", "forward"); //Command for moving forward
		commands.put("stay", "stay"); //Command for staying on the same square
		commands.put("backward", "backward"); //Command for moving backward
		commands.put("status", "status"); //Command to check user status
		commands.put("exit", "exit"); //Command to exit the game
		commands.put("map", "map"); //Command to check the squares and their numbers
		commands.put("cost", "cost"); //Command to check the MOVEMENT<=>CARROT cost
		commands.put("help", "help"); //Command for help
		commands.put("reset", "reset");//Resets player to the beginning
	}
	
	/**
	 * This method will go over all the words supplied from the 
	 * inputReader and pick out the command.
	 * 
	 * @param words
	 * @return Returns a single String that is the key-word command.
	 */
	public String recogniseCommand(HashSet<String> words)
	{
		Iterator<String> it = words.iterator();
		while(it.hasNext())
		{
			String command = commands.get(it.next());
			if (command != null)
			{
				return command;
			}
		}
		return "Command Not Found";
	}
	
	/**
	 * 
	 * @param HashSet of words from the initial input message
	 * @return Returns the integer from the message.
	 */
	public int recogniseInteger(HashSet<String> words)
	{
		Iterator<String> it = words.iterator();
		int integer = 0;
		while(it.hasNext())
		{
			try
			{
				integer = Integer.parseInt(it.next());
				if (integer != 0)
				{
					if (integer > 65 || integer < 0)
					{
						System.out.println("Moves out of bound, will move 0 squares");
					}
					else
						return integer;
				}
			}
			catch (Exception e)
			{
			}
		}
		return 0; //Doesn't consume users move phase
	}
	
	/**
	 * This function will test the command string input with 
	 * a series of if statements. After locating the correct command,
	 * it will call the appropriate function.
	 * 
	 * @param command
	 * @param userIndex
	 */
	public int executeCommand(String command,int integer, User user, ArrayList<User> users, Board board,ArrayList<User> finishedUsers)
	{
		if (command.equals("forward"))
		{
			return movementForward(user,integer,board,users,finishedUsers);
		}
		else if (command.equals("stay"))
		{
			return movementStay(user,board,users);
		}
		else if (command.equals("backward"))
		{
			return movementBackward(user,board);
		}
		else if (command.equals("status"))
		{
			return displayUserStatus(user,users);
		}
		else if (command.equals("reset"))
		{
			return resetUser(user,board);
		}
		else if (command.equals("map"))
		{
			return displayBoard(board,user,users);
		}
		else if (command.equals("cost"))
		{
			return displayCost(integer,user);
		}
		else if (command.equals("help"))
		{
			return displayHelp();
		}
		else if (command.equals("exit"))
		{
			System.out.println("Exiting...");
			System.exit(0);
			return 1; //This return doesn't matter  since exiting
		}
		
		else
		{
			System.out.println("Invalid Command Input!");
			System.out.println("Please input a proper command!");
			return -1; //Invalid option
		}
	}
	
	/**
	 * This method will ask for input of how many squares to
	 * move forward, validate the input, check if the square is
	 * occupied, check to make sure you have enough carrots, check
	 * the square isn't a tortoise square.
	 * Then move the players position and update the square occupied.
	 * 
	 * @param A user of class User
	 * @return An Integer to confirm that the command executed successfully
	 */
	private int movementForward(User user, int integer,Board board,ArrayList<User> users,ArrayList<User> finishedUsers)
	{
		if (integer == 0)
		{
			System.out.println("If you want to move zero squares, use 'stay' command");
			return -1; //Invalid option
		}
		if (board.checkSquare(user.getPosition()+integer).equals("tortoise"))
		{
			System.out.println("You can't land on a tortoise going forward!");
			return -1; //Invalid option
		}
		if (board.checkSquare(user.getPosition()+integer).equals("lettuce") && user.getLettuce()<1)
		{
			System.out.println("You don't have the lettuce to land on a lettuce square!");
			return -1; //Invalid option
		}
		if (board.checkOccupied(user.getPosition()+integer))
		{
			if (user.getCarrots() >= carrotCalculation(integer))
			{
				if (board.checkSquare(user.getPosition()+integer).equals("finish"))
				{
					if (preFinish(user,finishedUsers,users,board) == 1)
						return 1; //Consumes users move phase
					System.out.println("You have too many carrots to finish");
					System.out.println("You're "+user.getPlace()+" place and need "
							+ user.getPlace()*10 + " carrots to finish (or less) to finish");
					return -1; //Invalid option
				}
				else
				{
					user.setCarrots(user.getCarrots()-carrotCalculation(integer));
					board.removeOccupiedSquare(user.getPosition());
					board.addOccupiedSquare(user.getPosition()+integer);
					user.setPosition(user.getPosition()+integer);
					checkUserCarrots(user);
					return 1; //Consumes users move phase
				}
			}
			System.out.println("You don't have enough carrots to move that far");
			return -1;//Invalid option
		}
		System.out.println("Sorry, This Square Is Occupied By Another Player");
		return -1; //Invalid option
	}
	
	/**
	 * This method will perform a loop to calculate where the nearest 
	 * tortoise square is (behind the user) and check if it's occupied.
	 * If it is, then inform the user of invalid move.
	 * Otherwise move the users position to it.
	 * 
	 * @param A user of class User
	 * @return An Integer to confirm that the command executed successfully
	 */
	private int movementBackward(User user,Board board)
	{
		String square = "";
		int currentSquare = user.getPosition();
		while(!square.equals("tortoise") && currentSquare>0)
		{
			currentSquare--;
			square = board.checkSquare(currentSquare);
			if (square.equals("tortoise"))
			{
				if (board.checkOccupied(currentSquare))
				{
					int squareDifference = user.getPosition() - currentSquare;
					int carrotsReceived = squareDifference * 10;
					user.setCarrots(carrotsReceived + user.getCarrots());
					user.setPosition(currentSquare);
					System.out.println("You moved to sqaure "+currentSquare+":Tortoise");
					System.out.println("You recieved "+carrotsReceived+" carrots");
					return 1; //Consumes users move phase
				}
				System.out.print("Can't move to nearest tortoise square, it's occupied");
				return -1;//Invalid option
			}
		}
		System.out.print("Failed to move to nearest tortoise");
		return -1;//Invalid option
	}
	
	/**
	 * This method will simply pass the turn for the current
	 * user to the next (skipping your turn).
	 * 
	 * @param A user of class User
	 * @return An Integer to confirm that the command executed successfully
	 */
	private int movementStay(User user,Board board,ArrayList<User> users)
	{
		if(board.checkSquareEffect(user.getPosition()).equals("carrots"))
		{
			checkSquare(user,0,users,board);
			return 1; //Consumes users move phase
		}
		System.out.println("You can only stay on Carrots squares");
		return 0; //Doesn't consume users move phase
	}
	
	/**
	 * This method will print out the status of the user.
	 * Including his current position, carrots, lettuce and 
	 * place in the race relative to other players.
	 * 
	 * @param A user of class User
	 * @param ArrayList<User> users 
	 * @return An Integer to confirm that the command executed successfully
	 */
	private int displayUserStatus(User user,ArrayList<User> users)
	{
		System.out.println("\n"+user);
		return 0; //Doesn't consume users move phase
	}
	
	private int resetUser(User user , Board board)
	{
		user.setCarrots(65);
		board.removeOccupiedSquare(user.getPosition());
		user.setPosition(0);
		System.out.println("You have been reset to the start with 65 carrots");
		return 0;
	}
	
	/**
	 * This method will print out all the squares on the board
	 * and will show what their effects are (the picture on them).
	 * 
	 * @return A String containing all squares numbers and their effects
	 * @return An Integer to confirm that the command executed successfully
	 */
	private int displayBoard(Board board, User user,ArrayList<User> users)
	{
		int i = 0;
		String boardMap = "";
		for (String square: board.getSquares())
		{
			boolean mapSquarePrinted = false;
			if (user.getPosition() == i)
			{
				boardMap = boardMap +"||"+ i + ":"+ square +"<YOU ARE HERE";
				mapSquarePrinted=true;
			}
			for (int index=0;index<users.size();index++)
			{
				if (user != users.get(index) && users.get(index).getPosition()==i && i!=0)
				{
					boardMap = boardMap +"||"+ i + ":"+ square +"<User "+users.get(index).getUserNumber()+"      ";
					mapSquarePrinted=true;
				}
			}	
			if (!mapSquarePrinted)
				boardMap = boardMap +"||"+ i + ":"+ square +"             ";
			i++;
			if (i%4==0)
				boardMap = boardMap + "\n";	
		}
		System.out.println(boardMap);
		return 0; //Doesn't consume users move phase
	}
	
	/**
	 * This method will print out the cost of carrots per movement
	 * using the carrotCalculation method.
	 * @param integer
	 * @param user
	 * @return integer 
	 */
	private int displayCost(int integer, User user)
	{
		int cost = carrotCalculation(integer);
		System.out.println("The carrot cost to move "+integer+" squares is: "+cost);
		System.out.println("You currently have "+user.getCarrots()+" carrots");
		return 0; //Doesn't consume users move phase
	}
	
	/**
	 * displayRules will simple display the rules and 
	 * instructions to the user on how to play the game.
	 * 
	 * @return An Integer to confirm that the command executed successfully
	 */
	public int displayHelp()
	{
		System.out.println();
		System.out.print("********************************************************************************");
		System.out.println("\n*                                   RULES                                      *\n" + 
				"*------------------------------------------------------------------------------*\n*"+
				"No.1: You can move forward to any unoccupied square if you have enough carrots*\n" +
				"*                                                                              *\n*" + 
				"Number Squares: On your next turn check if your position in the               " + 
				"*\n*" + "race is equal to the number on the square then multiply your                  " + 
				"*\n*" + "position by 10 and draw that many carrots. Move on same turn.                 *\n" + 
				"*                                                                              *\n*" + 
				"Carrot Squares: Stay as long as you like. Each turn you miss, collect         " + 
				"*\n*" + "OR pay 10 carrots                                                             *\n" +
				"*                                                                              *\n*" + 
				"Lettuce Squares: You must hold a Lettuce to land on this square.              " +
				"*\n*" + "NEXT TURN discard 1X lettuce card and then multiply your position             " + 
				"*\n*" + "by 10 and draw that many carrots and move on this turn                        *\n" +
				"*                                                                              *\n*" + 
				"Tortoise Squares: You can ONLY move backwards to the nearest                  " +
				"*\n*" + "tortoise square if it is empty. Immediately collect 10 carrots                " +
				"*\n*" + "for each square you moved back. Next turn move back or forward again.         *\n" +
				"*                                                                              *\n*" + 
				"Hare Squares: Draw a Hare card and follow the instructions.                   *\n" +
				"********************************************************************************"  +
				"\n*" + "                                INSTRUCTIONS                                  " +
				"*\n*------------------------------------------------------------------------------"+
				"*\n*" + "Command for moving forward: 'forward' + number of spaces to move              " +
				"*\n*" + "Command for staying on the same square: 'stay' (only on carrots squares)      " +
				"*\n*" + "Command to take carrots when on the carrot square: 'take'                     " +
				"*\n*" + "Command to put carrots when on the carrot square: 'put'                       " +
				"*\n*" + "Command to check your status: 'status'                                        " +
				"*\n*" + "Command to exit the game: 'exit'                                              " +
				"*\n*" + "Command to check all players position and the squares on the board: 'map'     " +
				"*\n*" + "Command to check the MOVEMENT<=>CARROT cost: 'cost' + number of spaces to move" +
				"*\n*" + "Command to reset the player back to start (if you're stuck): 'reset'          " +
				"*\n*" + "Command for help: 'help'                                                      *");
		System.out.print("********************************************************************************");
		return 0; //Doesn't consume users move phase
	}
	
	/**
	 * This method with execute effects of the squares that the 
	 * user lands on during his turn.
	 * @param user
	 * @param currentUserPosition
	 * @param users
	 * @param board
	 */
	public void checkSquare(User user,int currentUserPosition,ArrayList<User> users, Board board)
	{
		String testSquare = board.checkSquareEffect(user.getPosition());
		if (testSquare.equals("hare"))
		{
			int option = random.nextInt(8);//Picks a random hare card from 0 to 7 inclusive.
			switch (option)
			{
			case 0:
				System.out.println("If there are more players behind you than in front of"
						+ " you, miss a turn, if not play again.");
				int usersBehind = 0;
				int usersAhead = 0;
				for (User testUser : users)//Finds out how many players are in front and behind the user
				{
					if (testUser.getPosition()<user.getPosition())
						usersBehind++;
					else 
						usersAhead++;
				}
				if ((usersBehind > usersAhead))
					user.setSkipTurn(true);
				if (users.size()==2)//Fixes a bug when there was only 2 users.
				{
					if(user.getPlace()==1)
						user.setSkipTurn(true);
				}
			break;
			case 1:
				System.out.println("Draw 10 carrots for each lettuce you still hold.");
				int carrotGain = user.getLettuce()*10;
				user.setCarrots(carrotGain + user.getCarrots());
			break;
			case 2:
				System.out.println("Restore your carrots to exactly 65.");
				user.setCarrots(65);
			break;
			case 3:
				System.out.println("Lose half of your carrots.");
				user.setCarrots(user.getCarrots()/2);
			break;
			case 4:
				System.out.println("Receive 1 carrot from each player.");
				user.setCarrots((users.size()-1) + user.getCarrots());
			break;
			case 5:
				System.out.println("Free ride, retrieve the carrots you paid to get to"
						+ " this square");
				int movementDifference = user.getPosition() - currentUserPosition;
				int returnCarrots = (int) ((0.5*movementDifference)*(movementDifference+1));
				user.setCarrots(user.getCarrots()+returnCarrots);
			break;
			case 6:
				System.out.println("Show how many carrots you are holding to all other players");
				System.out.println("User "+user.getUserNumber()+" has "+user.getCarrots()+" carrots");
			break;
			case 7:
				System.out.println("Give 10 carrots to each player behind you in the race.");
				for (User testUser : users)
				{
					if (testUser.getPosition()<user.getPosition())
					{
						user.setCarrots(user.getCarrots()-10);
						testUser.setCarrots(testUser.getCarrots()+10);
					}
				}
			break;
			}
		}
		else if (testSquare.equals("lettuce"))//This will check if the user has landed on a lettuce square.
		{
			System.out.println("You landed on a lettuce square, you'll skip a turn eating a lettuce");
			user.setLettuce(user.getLettuce()-1);
			System.out.println("You gained "+(10*user.getPlace())+" carrots!");
			user.setCarrots((user.getCarrots()+(10*user.getPlace())));
			user.setSkipTurn(true);
		}
		else if (testSquare.equals("no.2"))//This will check if the user has landed on a no.2 square.
		{
			if (user.getPlace()==2)
			{
				user.setCarrots(user.getCarrots()+20);
				System.out.println("You received 20 carrots!");
			}
		}
		else if (testSquare.equals("no.3"))//This will check if the user has landed on a no.3 square.
		{
			if (user.getPlace()==3)
			{
				user.setCarrots(user.getCarrots()+30);
				System.out.println("You received 30 carrots!");
			}
		}
		else if (testSquare.equals("no.4"))//This will check if the user has landed on a no.4 square.
		{
			if (user.getPlace()==4)
			{
				user.setCarrots(user.getCarrots()+40);
				System.out.println("You received 40 carrots!");
			}
		}
		else if (testSquare.equals("no.1,5,6"))//This will check if the user has landed on the no.1,5,6 square.
		{
			if (user.getPlace()==1)
			{
				user.setCarrots(user.getCarrots()+10);
				System.out.println("You received 10 carrots!");
			}
			else if (user.getPlace()==5)
			{
				user.setCarrots(user.getCarrots()+50);
				System.out.println("You received 50 carrots!");
			}
			else if (user.getPlace()==6)
			{
				user.setCarrots(user.getCarrots()+60);
				System.out.println("You received 60 carrots!");
			}
		}
		else if (testSquare.equals("carrots"))//This will check if the user has landed on a carrots square.
		{
			boolean commandInputed = false;
			while(!commandInputed)
			{
				System.out.println("You landed on a Carrot Square, you MUST 'put' or 'take' 10 carrots");
				System.out.print("User "+user.getUserNumber()+" >>");
				String command = input.nextLine().toLowerCase().trim();
				if (command.equals("take"))
				{
					System.out.println("10 CARROTS ADDED");
					user.setCarrots(user.getCarrots()+10);
					commandInputed=true;
				}
				else if (command.equals("put"))
				{
					if (user.getCarrots()>=10)
					{
						System.out.println("10 CARROTS REMOVED");
						user.setCarrots(user.getCarrots()-10);
						commandInputed=true;
					}
				}
			}
		}
		
	}
	
	/**
	 * carrotCalculation method uses the formula to calculate 
	 * the carrot cost for the move that the user inputs.
	 * 
	 * @param n
	 * @return The carrot cost for the move input
	 */
	private int carrotCalculation(int n)
	{
		//FORMULA: an = 0.5*n*(n+1)
		return (int) ((0.5*n)*(n+1));
	}
	
	/**
	 * This method will calculate the users place relative to the other 
	 * users in the race. 
	 * 
	 * @param user
	 * @param users
	 */
	public void calculateUserPlace(User user,ArrayList<User> users)
	{
		int usersBehind = 0;
		for (User testUser : users)
		{
			if (testUser.getPosition()<user.getPosition())
				usersBehind++;
		}
		for (int i=1;i<=users.size();i++)
		{
			if (usersBehind == users.size()-i)
			{
				user.setPlace(i);
			}
		}
	}
	/**
	 * Check if the user meets the requirements to finish the race 
	 * ie. lettuce =0 and carrots <= userPlace*10 and then adds them to
	 * the finishedUsers array.
	 * @param user
	 * @param finishedUsers
	 * @param users
	 * @param board
	 * @return
	 */
	private int preFinish(User user, ArrayList<User> finishedUsers,ArrayList<User> users,Board board)
	{
		if (user.getLettuce()==0)
		{
			int carrotCheck=10;
			for (int i=0; i<=finishedUsers.size();i++)
			{
				if (user.getCarrots()<=carrotCheck)
				{
					board.removeOccupiedSquare(user.getPosition());
					finishedUsers.add(user);
					user.setUserActive(false);
					System.out.println("You finished in place "+(i+1));
					return 1; //Consumes users move phase
				}
				if (carrotCheck==50) //Ensures the carrotCheck variable doesn't go above 50
					carrotCheck = 10;
				else
					carrotCheck = carrotCheck+10;
			}
		}
		return 0; //Doesn't consume users move phase
	}
	/**
	 * This method will check if the user had ran out of carrots
	 * and if they have it will place them back at the start of the race.
	 * @param user
	 * @param users
	 */
	private void checkUserCarrots(User user)
	{
		if (user.getCarrots() < 1)
		{
			user.setPosition(0);
			System.out.println("You have ran out of carrots and have to go back to the start");
		}		
	}
	/**
	 * This method will check if there is only one user left in the race and
	 * if there is it will automatically add them to the end of the
	 * finishedUsers ArrayList. it then calls the printResults method.
	 * @param users
	 * @param finishedUsers
	 * @param user
	 */
	public void autoFinish(ArrayList<User> users, ArrayList<User> finishedUsers, User user)
	{
		if (users.size()-1==finishedUsers.size())
		{
			finishedUsers.add(user);
			user.setUserActive(false); //Disable the user from the game if they have finished
			System.out.println("\n ***GAME OVER***\n");
			printResults(finishedUsers);
		}
	}
	/**
	 * This method prints out the finishedUsers arraylist giving the
	 * place each user finished in the race.
	 * @param finishedUsers
	 */
	public void printResults(ArrayList<User> finishedUsers)
	{
		System.out.println();//Adding space
		for (int i=0; i<finishedUsers.size(); i++)
		{
			System.out.println("User "+finishedUsers.get(i).getUserNumber() + " finished in place " + (i+1));
		}
		System.exit(0); //Quits the game
	}
}
