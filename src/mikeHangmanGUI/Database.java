package mikeHangmanGUI;


/*
 * @author Michael Setteducati
 * @since 10/31/14
 * This is an interface that a Database of Words should implement. 
 */

public interface Database {
	
	//abstract methods
	Word getRandomWord(int difficulty);
	void printWords();
	void updateWord(Word myWord);

}
