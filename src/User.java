/**
 * The User class will represent a user playing the game.
 * It will store the players carrots, lettuce, position and 
 * his/her's place in the overall game.
 * 
 * @author 		Oleksandr Kononov
 * @version 	1.2	(8.Mar.2016)
 */

public class User {
	private int userNumber; //The user number relative to other users (to distinguish from others)
	private int carrots; //The amount of carrots the user has.
	private int lettuce; //The amount of lettuce the user has.
	private int position; //The users position on the board.
	private int place; //The users place relative to the other users (1st,2nd,3rd,etc).
	private boolean skipTurn = false;//Variable to skip user's turn
	private boolean active = true;//Variable to de-activate user when finished
	
	/**
	 * The constructor for the User class.
	 * Takes no parameters and sets the initial values
	 * user's parameters.
	 */
	public User(int number)
	{
		userNumber = number;
		carrots = 65; //The initial amount of carrots at the start of the game.
		lettuce = 3; //The initial amount of lettuce at the start of the game.
		position = 0; //The initial position at the start of the game.
		place = 0; //The place is set to zero at the start of the game.
	}
	
	//ACCESSORS//
	public int getUserNumber()
	{
		return userNumber;
	}
	public int getCarrots()
	{
		return carrots;
	}
	public int getLettuce()
	{
		return lettuce;
	}
	public int getPosition()
	{
		return position;
	}
	public int getPlace()
	{
		return place;
	}
	public boolean getSkipTurn()
	{
		return skipTurn;
	}
	public boolean getUserActive()
	{
		return active;
	}
	
	//MUTATORS//
	public void setCarrots(int newCarrots)
	{
		if (newCarrots >= 0)
			carrots = newCarrots;
	}
	public void setLettuce(int newLettuce)
	{
		if (newLettuce >=0 && newLettuce <= 3)
			lettuce = newLettuce;
	}
	public void setPosition(int newPosition)
	{
		if (newPosition >= 0 && newPosition<=64)
			position = newPosition;
	}
	public void setPlace(int newPlace)
	{
		if (newPlace >= 1 && newPlace <=6)
			place = newPlace;
	}
	public void setSkipTurn(boolean newSkipTurn)
	{
		skipTurn = newSkipTurn;
	}
	public void setUserActive(boolean newActive)
	{
		active = newActive;
	}
	
	/**
	 * toString method will produce a user-friendly representation 
	 * of the data about the user.
	 * 
	 * @return A String comprised of the classes parameters.
	 */
	public String toString()
	{
		return  "*******************************************\n"
				+"User " + userNumber + "'s Status \n"+
				"Users carrots: "+carrots+"\n"+
				"Users lettuce: "+lettuce+"\n"+
				"Users position: "+position+"\n"+
				"User's place (relative to other users): "+place +"\n";
	}
}
