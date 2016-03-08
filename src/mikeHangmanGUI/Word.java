package mikeHangmanGUI;

import java.util.ArrayList;

/**
 * @author Michael Setteducati
 * @since 10/28/14
 * This class represents a Word object in the hangman game. 
 */

public class Word {

	//instance variables
	private String word; //string representation of the word 
	private Letter[] _letters; //representation of the word using an array of etters 
	private int timesUsed; 
	private int timesCorrectlyGuessed;
	private int difficulty; 
	private ArrayList<Letter> wrongGuesses;
	
	public static final int EASY = 0;
	public static final int HARD = 1;
	
	//overloaded constructor
	public Word(String str, int myDifficulty) {
		word = str;
		this.initializeLettersArray();
		timesUsed = 0; //default
		timesCorrectlyGuessed = 0; //default
		difficulty = myDifficulty;
		wrongGuesses = new ArrayList<Letter>();
	}
	
	//overloaded constructor
	public Word(String str, int myTimesUsed, int myTimesCorrectlyGuessed, int myDifficulty) {
		word = str;
		this.initializeLettersArray();
		timesUsed = myTimesUsed;
		timesCorrectlyGuessed = myTimesCorrectlyGuessed;
		difficulty = myDifficulty;
		wrongGuesses = new ArrayList<Letter>();
	}
	
	//uses the String representation of a word and sets it to the letter array instance variable
	private void initializeLettersArray(){ 
		_letters = new Letter[word.length()]; //instantiates array using the length of the word
		for(int i = 0; i<_letters.length; i++) { //loops through
			_letters[i] = new Letter(word.substring(i, i+1)); //sets each element as a new Letter
		}
	}
	
	//overrides Object's toString() method
	@Override
	public String toString(){
		String str = ""; //creates a string
		//adds each letter to the String. If it is not viewable yet, it adds an underscore 
		for(Letter l : _letters) 
			str += l.toString() + " ";  
		return str;
	}

	//for writing a word to the database
	public String stringRepresentationForDatabase(){ 
		return word + "," + timesUsed + "," + timesCorrectlyGuessed + "," + difficulty;
	}
	
	//getter method
	public String getWord(){
		return word;
	}
	
	//getter method
	public int getTimesUsed(){
		return timesUsed;
	}
	
	//getter method
	public int getDifficulty(){
		return difficulty;
	}
	
	//getter method
	public int getTimesCorrectlyGuessed(){
		return timesCorrectlyGuessed;
	}
	
	//updates the word's instance variables for timesUsed and timesCorrectlyGuessed
	public void updateTimesUsedAndCorrectlyGuesssed() {
		timesUsed++; //this will definitely increment because it was used
		if(allViewable()) //if every element is viewable, the word was correctly guessed
			timesCorrectlyGuessed++;
	}
	
	//guesses a letter and returns true if it was in the word and false if not
	public boolean guess(Letter myL) {
		boolean correct = false; //sets this boolean to false so it can be changed to true if it was in teh word
		for(Letter l : _letters) { //loops through the letters
			if(l.equals(myL)) { //if the letters are equal
				l.setViewable(); //it sets the letter to be viewable
				correct = true; //and changes the boolean to true
			}
		}
		
		if(!correct)
			wrongGuesses.add(myL);
		
		return correct;
	}
	
	//method determines if every element of the word is viewable, ie the player has won
	public boolean allViewable(){
		for(Letter l : _letters) //loops through array
			if(!l.getViewable()) //if any element is not viewable, it returns false
				return false;
		return true; //if nothing was returned, every letter is viewable
	}
	
	//Gets a String representation of the wrongly guessed letters
	public String getWrongGuesses(){
		String str = "";
		for(Letter l : wrongGuesses) {
			str += l.getStr() + ", ";
		}
		return str.substring(0, str.length()-2); //this removes the final ", " at the end of str
	}
	
}
