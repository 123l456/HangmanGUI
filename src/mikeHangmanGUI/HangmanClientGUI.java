package mikeHangmanGUI;
/**
 * Constructs a GUI for a hangman game.
 * @author Michael Setteducati
 * @since November 20, 2014
 */
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class HangmanClientGUI extends JFrame {
	
	//Instance variables for the GUI
	private Container content;
	private JMenuBar menuBar;
	private JMenu menuFile, menuNewGame, menuHelp;
	private JMenuItem menuItemEasy, menuItemHard, menuItemEnd, menuItemDirections, menuItemAddWord;
	private JTextField txtWord;
	private HangmanVisual myHangman;
	private JPanel pnlLetters;
	private JButton[][] btnaryLetters;
	private String[][] straryLetters = 
		{{"A", "B", "C", "D", "E", "F", "G"} , 
		{"H", "I", "J", "K", "L", "M", "N"},
		{"O", "P", "Q", "R", "S", "T", "U"}, 
		{"V", "W", "X", "Y", "Z"}};
	private JPanel pnlWrongGuesses;
	private JLabel lblWrongGuesses;
	private JTextField txtWrongGuesses;
	
	private Word myWord; 
	private WordDatabase myWordDB;
	
	
	/**
	 * Constructor 
	 */
	public HangmanClientGUI(){
		super("Hangman"); 
		
		//Creates Database that will be used to select a word.
		myWordDB = new WordDatabase("src/mikeHangmanGUI/hmlistofwords.txt"); 
		
		/* The following code creates a JMenuBar at the top of the GUI. This will be used
		 * to create a new game, end a game, or get help on how to use this program. 
		 */
		//Creates objects
		menuBar = new JMenuBar(); 
		menuFile = new JMenu("File"); 
		menuHelp = new JMenu("Help");
		menuNewGame = new JMenu("New Game");
		menuItemEasy = new JMenuItem("Easy");
		menuItemHard = new JMenuItem("Hard");
		menuItemEnd = new JMenuItem("End Game");
		menuItemDirections = new JMenuItem("Directions");
		menuItemAddWord = new JMenuItem("Add Word");
		
		//Adds ActionListener to objects
		menuItemEasy.addActionListener(new MenuHandler());
		menuItemHard.addActionListener(new MenuHandler());
		menuItemAddWord.addActionListener(new MenuHandler());
		menuItemEnd.addActionListener(new MenuHandler());
		menuItemDirections.addActionListener(new MenuHandler());
		
		//Adds appropriate object to File, Help, and New Game menus, and adds File and Help to JMenuBar
		menuBar.add(menuFile);
		menuBar.add(menuHelp);
		menuNewGame.add(menuItemEasy);
		menuNewGame.add(menuItemHard);
		menuFile.add(menuNewGame);
		menuFile.add(menuItemAddWord);
		menuFile.add(menuItemEnd);
		menuHelp.add(menuItemDirections);
		
		content = getContentPane(); //Creates Object of type Container from the contentPane
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS)); //Sets the layout for content as a vertical BoxLayout

		myHangman = new HangmanVisual(); //Creates a HangmanVisual Object, which is a subclass of JPanel

		txtWord = new JTextField(25); //creates a new JTextField object with length up to 25
		txtWord.setMaximumSize(new Dimension(400, 50)); //sets the maximum size for txtWord, because if not it took up the whole JFrame
		txtWord.setEditable(false); //Will allow user to edit
		txtWord.setHorizontalAlignment(JTextField.CENTER); //Centers the text (asthetic) 
		
		Font curFont = txtWord.getFont();//Gets the default Font
		//PUT IN SOMETHING TO CREATE SPACE BETWEEN EACH LETTER
		Font newFont = new Font(Font.SANS_SERIF,Font.PLAIN, 28); //Creates a new Font that is the same as the default, just bigger
		txtWord.setFont(newFont); //Sets the font of the JTextField as the newly made Font
		
		pnlLetters = new JPanel(new GridLayout(4,7)); //Creates a JPanel for the letters to choose from as a GridLayout
		pnlLetters.setMaximumSize(new Dimension(400, 100)); //sets maximum size of the JPanel for formatting reasons
		 
		btnaryLetters = new JButton[4][7]; //Creates an array of JButton, the same size as the String array
		initializeBtnaryLetters(); //This creates, gives functionality, and formats every JButton in the array
		
		pnlWrongGuesses = new JPanel(new FlowLayout());
		lblWrongGuesses = new JLabel("Wrong Guesses:");
		txtWrongGuesses = new JTextField(15);
		txtWrongGuesses.setEditable(false);
		pnlWrongGuesses.add(lblWrongGuesses);
		pnlWrongGuesses.add(txtWrongGuesses);
		pnlWrongGuesses.setMaximumSize(new Dimension(400, 50));
	
		content.add(myHangman); //Adds the HangmanVisual object to content
		content.add(txtWord); //Adds the JTextBox to content
		content.add(pnlLetters); //Adds the JPanel of JButtons to content
		content.add(pnlWrongGuesses);
		setJMenuBar(menuBar); //Sets the JMenuBar of content as the menuBar that was created
		
		setSize(400, 600); //sets the size of the JFrame
		this.setResizable(false);
		setVisible(true);  //makes the JFrame visible
		addWindowListener(new CloseWindow()); //Sets a WindowListener that saves the WordDatabase
	}
	
	/**
	 * Private inner class that implements ActionListener interface. This will guess the 
	 * letter that is clicked. The myWord's guess(Letter myL) method will be invoked, and
	 * the returned boolean is used to update the manRepresentation's number of wrongGuesses.
	 * The man is then repainted. The button will also be greyed out once it is clicked to 
	 * indicate to the user that (s)he has already guessed this letter.
	 * 
	 * @author Michael Setteducati
	 * 
	 */
	private class LetterButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!(myHangman.getWrongGuesses()>=HangmanVisual.MAX_WRONG_GUESSES) && !myWord.allViewable()) { //makes sure that the game is not over
				JButton thisButton = (JButton) (e.getSource()); //Determines what button created this ActionListener, and creates a temporary variable for it
				boolean correct = myWord.guess(new Letter(thisButton.getText())); //Creates a boolean based on the results of the myWord.guess method 
				thisButton.setEnabled(false); //Greys out the JButton so it cannot  be pressed
				
				txtWord.setText(myWord.toString()); //Updates the hidden word based on whether or not the user guessed correctly
				
				if(!correct) { //If user was wrong, increment wrongGuesses (this also calls repaint() method so hangman is redrawn), also add the wrongly guessed letter to textfield for wrong guesses
					myHangman.incrementWrongGuesses();
					txtWrongGuesses.setText(myWord.getWrongGuesses());
				}
				
				if(myHangman.getWrongGuesses()>=HangmanVisual.MAX_WRONG_GUESSES) { //If the user has gone of the allowed number of wrong guesses
					JOptionPane.showMessageDialog(content, "You've run out of guesses!\nThe word was: " + myWord.getWord()); //Shows popup telling user
					txtWord.setText(myWord.getWord()); //Sets the JTextField as the entire word without blanks
					endGame(); //Calls endGame() method (see below)
				}
				
				if(myWord.allViewable()) { //If this method returns true, the user has won the game
					JOptionPane.showMessageDialog(content, "Congratulations, you've won!\nThe word was: " + myWord.getWord()); //Tells user
					endGame(); //Calls endGame() method (see below)
				}	
			}
		}
	}
	
	/**
	 * This private method is used in the LetterButtonHandler and MenuHandler private classes, and updates data 
	 * for the random Word that was chosen. It then updates the WordDatabase object so it has
	 * up to date information on how well the word was guessed. 
	 */
	private void endGame(){ 
		myWord.updateTimesUsedAndCorrectlyGuesssed();
		myWordDB.updateWord(myWord);
	}
	
	/**
	 * This private inner class is used for the JMenu items in the JMenuBar. It gives
	 * the buttons functionality to start a new game, end a game, or get help.
	 * 
	 * @author michaelsetteducati
	 * 
	 */
	private class MenuHandler implements ActionListener {
		@Override
		public void actionPerformed (ActionEvent e) {
			if(e.getSource() == menuItemEasy) { 
				newGame(0); //Calls newGame method (see below) with difficulty being easy
			} else if (e.getSource() == menuItemHard) {
				newGame(1); //Calls newGame method (see below) with difficulty being hard
			} else if (e.getSource() == menuItemAddWord) { //calls addWord() method which will prompt user for a new word
				addWord();
			} else if (e.getSource() == menuItemEnd) {
				myHangman.endGame(); //Draws the entire hangman
				txtWord.setText(myWord.getWord()); //Sets teh JTextField as the word 
				endGame(); //Calls endGame() method of this class to update the Word's data
			} else if (e.getSource() == menuItemDirections) { //A popup box tells the user how to use the program
				JOptionPane.showMessageDialog(content, 
						"To start a new game, go to File then select the difficulty\n"
						+"that you would like to play. You can also end the game from\n"
						+ "the File menu. Finally, you may add a new word to the list\n"
						+ "of possible words from the File menu.\n\n"
						+ "To play, you have six guesses to correctly guess the hidden\n"
						+ "word. The visual depiction of the hangman shows how many\n"
						+ "guesses you have remaining. Once the entire hangman is drawn,\n"
						+ "you lose!");
			} 
		}
	}
	
	/**
	 * This method creates a JOptionPane for inserting a new word into the WordDatabse. It will 
	 * eventually be saved to the text file that stores all of the words when the user closes
	 * the JFrame. 
	 */
	private void addWord() {
		//Creates GUI objects necessary for the popup box
		JLabel lblWord = new JLabel("Word:");
		JTextField txtWord = new JTextField(20);
		ButtonGroup grp = new ButtonGroup();
		JRadioButton btnEasy = new JRadioButton("Easy");
		JRadioButton btnHard = new JRadioButton("Hard");
		grp.add(btnEasy);
		grp.add(btnHard);
		
		Object[] ob = {lblWord, txtWord, btnEasy, btnHard};
		int result = JOptionPane.showConfirmDialog(content, ob, "Please input new word and difficulty.", JOptionPane.OK_CANCEL_OPTION);
		
		String strWord = ""; 
		int dif = -1;
		if(result == JOptionPane.OK_OPTION) {
			strWord = txtWord.getText();
			
			if(btnEasy.isSelected())
				dif = 0;
			else if (btnHard.isSelected())
				dif = 1;
		}
		
		if(!strWord.equals("") && dif != -1) { //makes sure that strWord and dif have changed
			myWordDB.addWord(new Word(strWord, dif)); //creates a Word object and adds it to the Database
		}
	}

	/**
	 * This private inner class is going to be used when the program is closed, to save
	 * the updates to each word that was used in the WordDatbase class.
	 * 
	 * @author michaelsetteducati
	 *
	 */
	private class CloseWindow implements WindowListener {
		@Override
		public void windowClosing(WindowEvent e) {
			myWordDB.writeToFile();
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}
	}
	/**
	 * This creates a new game. It gets a random word based on the difficulty parameter.
	 * It then resets myHangman's wrongGuesses, puts the new word in the JTextField 
	 * that contains the word, enables every JButton with letters to guess, and resets
	 * the JTextField for wrongly guessed letters.
	 * @param difficulty
	 */
	private void newGame(int difficulty) {
		myWord = myWordDB.getRandomWord(difficulty);
		myHangman.resetWrongGuesses();
		txtWord.setText(myWord.toString());
		enableEveryButton();
		txtWrongGuesses.setText("");
	}
	
	/**
	 * Populates the btnaryLetters instance variable so it is a two dimensional array that
	 * contains JButtons. There is no ActionListener associated with the JButton at this 
	 * point since this method is used before a new game is created.
	 */
	private void initializeBtnaryLetters(){
		for(int i = 0; i<straryLetters.length; i++)
			for(int j = 0; j<straryLetters[i].length; j++) {
				btnaryLetters[i][j] = new JButton(straryLetters[i][j]);
				pnlLetters.add(btnaryLetters[i][j]);
				btnaryLetters[i][j].addActionListener(new LetterButtonHandler());
			}
	}
	
	/**
	 * Loops through the 2D array of JButtons and enables every one. This method should be run
	 * inside of the newGame method.
	 */
	private void enableEveryButton(){
		for(int i = 0; i<btnaryLetters.length; i++)
			for(int j = 0; j<btnaryLetters[i].length; j++)
				if(btnaryLetters[i][j] != null)
					btnaryLetters[i][j].setEnabled(true);
	}
	
	public static void main(String[] args) {
		HangmanClientGUI hangman = new HangmanClientGUI(); //creates object
	}

}