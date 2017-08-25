/**
 * The Board class will hold all the squares on the 
 * playing board and their respective values/effects.
 * When the user changes position, the position for that
 * user will have to go through here and the user
 * will be affected by the squares effect (if there is any).
 * 
 * @author 		Oleksandr Kononov
 * @version		1.0 (23.Feb.2016)
 */
import java.util.ArrayList;

public class Board {
	private ArrayList<String> squares = new ArrayList<String>();//ArrayList for squares on the board
	private ArrayList<String> occupied = new ArrayList<String>();//ArrayList for user occupied squares
	
	/**
	 * The constructor for the class will execute a method to
	 * add the squares (with their effects) into the ArrayList.
	 * Also fill fill the occupied ArrayList with empty Strings to 
	 * provide it with sufficient space.
	 * 
	 */
	public Board()
	{
		startingSquares();
		for (int i=0; i < squares.size();i++)
		{
			occupied.add("");
		}
	}
	
	/**
	 * startingSquares method will add the squares into the 
	 * ArrayList for the game, this will not be changed 
	 * for the duration of the game.
	 */
	private void startingSquares()
	{
		squares.add("start    ");
		squares.add("hare     ");
		squares.add("carrots  ");
		squares.add("hare     ");
		squares.add("no.3     ");
		squares.add("carrots  ");
		squares.add("hare     ");
		squares.add("no.1,5,6 ");
		squares.add("no.2     ");
		squares.add("no.4     ");
		squares.add("lettuce ");
		squares.add("tortoise");
		squares.add("no.3    ");
		squares.add("carrots ");
		squares.add("hare    ");
		squares.add("tortoise");
		squares.add("no.1,5,6");
		squares.add("no.2    ");
		squares.add("no.4    ");
		squares.add("tortoise");
		squares.add("no.3    ");
		squares.add("carrots ");
		squares.add("lettuce ");
		squares.add("no.2    ");
		squares.add("tortoise");
		squares.add("hare    ");
		squares.add("carrots ");
		squares.add("no.4    ");
		squares.add("no.3    ");
		squares.add("no.2    ");
		squares.add("tortoise");
		squares.add("hare    "); //half-way point - 32nd AND 31st (zero based counting).
		squares.add("no.1,5,6");
		squares.add("carrots ");
		squares.add("hare    ");
		squares.add("no.2    ");
		squares.add("no.3    ");
		squares.add("tortoise");
		squares.add("carrots ");
		squares.add("hare    ");
		squares.add("carrots ");
		squares.add("no.2    ");
		squares.add("lettuce ");
		squares.add("tortoise");
		squares.add("no.3    ");
		squares.add("no.4    ");
		squares.add("hare    ");
		squares.add("no.2    ");
		squares.add("no.1,5,6");
		squares.add("carrots ");
		squares.add("tortoise");
		squares.add("hare    ");
		squares.add("no.3    ");
		squares.add("no.2    ");
		squares.add("no.4    ");
		squares.add("carrots ");
		squares.add("tortoise");
		squares.add("lettuce ");
		squares.add("hare    ");
		squares.add("carrots ");
		squares.add("no.1,5,6");
		squares.add("carrots ");
		squares.add("hare    ");
		squares.add("carrots ");
		squares.add("finish  "); //65th place and 64th (counting from zero).
	}
	
	/**
	 * addOccupiedSquare method will add(set) the square from the 
	 * occupied ArrayList that is going to be occupied by the user.
	 * 
	 * @param square
	 */
	public void addOccupiedSquare(int square)
	{
		occupied.set(square,squares.get(square));
	}
	
	/**
	 * removeOccupiedSquare method will remove (set to "") the square from the 
	 * occupied ArrayList that is no longer occupied by the user.
	 * 
	 * @param square
	 */
	public void removeOccupiedSquare(int square)
	{
		occupied.set(square,"");
	}
	
	/**
	 * Checks the occupied ArrayList if the square is 
	 * occupied by another user
	 * 
	 * @param index
	 * @return a boolean
	 */
	public boolean checkOccupied(int index)
	{
		if (!occupied.get(index).equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Returns the String - effect of the square you want at a certain index
	 * that's also occupied by a user
	 * 
	 * @param index
	 * @return String of the square you want that's occupied
	 */
	public String checkSquareEffect(int index)
	{
		return occupied.get(index).replaceAll("\\s","");
	}
	
	/**
	 * Returns the whole ArrayList of squares 
	 * 
	 * @return squares ArrayList
	 */
	public ArrayList<String> getSquares()
	{
		return squares;
	}
	
	/**
	 * Returns the String - effect of the square you want at a certain index
	 * 
	 * @param index
	 * @return String of any square you want
	 */
	public String checkSquare(int index)
	{
		return squares.get(index).replaceAll("\\s", "");
	}
}
