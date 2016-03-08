package mikeHangmanGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Michael Setteducati
 * @since 10/28/14
 * This class represents a Database of words. The words are stored in a text file
 * and it is read from when the class is instantiated. When you are done using it, 
 * the text file should be overwritten with updated data for the word that was randomly
 * chosen. 
 */
public class WordDatabase implements Database {

	//instance variables
	private ArrayList<Word> allWords;
	private File listOfWords;
	
	public WordDatabase(String fileLocation){ //constructor
		allWords = new ArrayList<Word>();
		listOfWords = new File(fileLocation);
		this.readFromFile();
	}
	
	//Adds a new word to the Database
	public void addWord(Word w) {
		allWords.add(w);
	}
	
	//method that is used to chose a word based on the words that have been used the least and guessed the least
	public Word getRandomWord(int difficulty){ 
		ArrayList<Word> leastUsedWords = getLeastUsedWords(difficulty);
		ArrayList<Word> leastCorrect = getLeastCorrectWords(leastUsedWords); //out of the least used words, this determines the words that were guessed the least
		return leastCorrect.get((int) (Math.random()*leastCorrect.size())); //returns a random Word out of the Words that have been used and guessed the same 
	}
	
	private ArrayList<Word> getLeastUsedWords(int difficulty) {
		int least = 1000; //arbitrarily large number
		
		//loops through words and finds the least number of times that any word at the desired difficulty was used
		for(Word w : allWords) { 
			if(w.getTimesUsed()<least && w.getDifficulty() == difficulty)
				least = w.getTimesUsed();
		}
		
		ArrayList<Word> leastUsedWords = new ArrayList<Word>(); //ArrayList to write to
		//Loops through and adds every word that was used the least number of times to the arraylist
		for(Word w : allWords) {
			if(w.getTimesUsed() <= least && w.getDifficulty() == difficulty)
				leastUsedWords.add(w);
		}
		
		return leastUsedWords; //returns this arraylist
	}
	
	private ArrayList<Word> getLeastCorrectWords(ArrayList<Word> myWds) {
		int least = 1000; //arbitrarily large number
		
		for(Word w : myWds) 
			if(w.getTimesCorrectlyGuessed() < least)
				least = w.getTimesCorrectlyGuessed();
		
		ArrayList<Word> leastGuessed = new ArrayList<Word>();
		for(Word w : myWds)
			if(w.getTimesCorrectlyGuessed() == least)
				leastGuessed.add(w);
		
		return leastGuessed;
	}
	
	//Method used after a word has been used in the game. It updates the times it has been used and guessed.
	public void updateWord(Word update) {
		for(int i = 0; i<allWords.size(); i++) { //loops through arraylist of all words
			if(update.getWord().equalsIgnoreCase(allWords.get(i).getWord())) //compares Words based on their String instance variable
				allWords.set(i, update); //once it finds the index of the word, it updates it to the new word 
		}
	}
	 
	//Prints all of the words, used mostly for testing
	public void printWords(){
		for(Word w : allWords) {
			System.out.println(w.stringRepresentationForDatabase());
		}
	}
	
	//reads words from file
	public void readFromFile(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(listOfWords)); //creates buffered reader
			String oneWordInfo; //String used for each line of the text file
			
			while(true) { //loops until end of file
				oneWordInfo = br.readLine(); //reads line
				
				if(oneWordInfo == null) //end of file
					break;
				else if(oneWordInfo.startsWith("#")) //first line is comment line
					continue;
				else {
					allWords.add(processSingleWord(oneWordInfo)); //creates a word object and adds it to arraylist
				}
			}
			
			br.close(); //closes BR
		} catch (IOException ex) { //catches any IOException
			System.out.println(ex.getMessage());
		}
	}
	
	//private method used for taking a line from the text file and creating a Word object
	private Word processSingleWord(String str) {
		try { 
			String[] ary = str.split(","); //creates a String array with each element split with a comma
			
			//a line of the text file may have two tokens (Word and difficulty) or four tokens (Word, used, sucessfully guessed, and difficulty)
			if(ary.length==2) { //if there are only two, sets the word and difficulty to variables and returns the Word
				String word = ary[0];
				int difficulty = Integer.parseInt(ary[1]);
				return new Word(word, difficulty);
			} else if (ary.length==4) { //same as above, but includes used and successfullyGuessed
				String word = ary[0];
				int used = Integer.parseInt(ary[1]);
				int successfullyGuessed = Integer.parseInt(ary[2]);
				int difficulty = Integer.parseInt(ary[3]);
				return new Word(word, used, successfullyGuessed, difficulty);
			}
		} catch(Exception ex) { //so program doesnt crash if an unexpected number of tokens exist
			System.out.println(ex.getMessage());
		} 
		return null; //if a word cannot be made for whatever reason, null is returned
	}
	
	//method used for writing the arraylist back to the file
	public void writeToFile() {
		try{
			PrintWriter pw = new PrintWriter(new FileOutputStream(listOfWords, false)); //creates a printwriter
			pw.println("#word,used,successfully guessed,difficulty"); //prints the header to the file
			for(Word w : allWords) { //loops through all of the words and prints them all to the file
				pw.println(w.stringRepresentationForDatabase());
			}
			pw.close(); //closes the printwriter
		} catch(IOException ex) { //catches any exceptions thrown
			System.out.println(ex.getMessage());
		}
	}

}