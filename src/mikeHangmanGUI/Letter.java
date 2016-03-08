package mikeHangmanGUI;

/**
 * @author Michael Setteducati
 * @since 10/28/14
 * This class represents a single letter and has two properties, 
 * a String representation of the letter and a boolean that 
 * tells if it is viewable or not
 */

public class Letter {
	
	//instance variables
	String str;
	boolean viewable;
	
	/**
	 * Constructor
	 * @param single letter long String, that represents one letter
	 */
	public Letter(String myStr) {
		str = myStr;
		if(str.equals(" ")) //if the letter is a space, it is viewable
			viewable = true;
		else //if it is anything else, it is not
			viewable = false;
	}
	
	/**
	 * @return the letter (of type String), no matter if it is viewable or not
	 */
	public String getStr(){ 
		return str;
	}
	
	/**
	 * @return the representation of the letter, depends on if it is viewable or not
	 */
	@Override
	public String toString(){
		if(viewable) //if the letter has been guessed, it returns the letter
			return str;
		else //if not, it returns an underscore
			return "_";
	}
	
	
	/**
	 * 
	 * @param another Letter object
	 * @return true if the Letter is the same as this instance of letter
	 */
	public boolean equals(Letter l) {
		if(str.equalsIgnoreCase(l.getStr())) //if the string is the same, it is the same
			return true;
		else 
			return false;
	}
	
	/**
	 * Mutator method that sets this letter to be viewable.
	 */
	public void setViewable(){ 
		viewable = true;
	}
	
	/**
	 * @return true if this letter is viewable
	 */
	public boolean getViewable(){
		return viewable;
	}

}
