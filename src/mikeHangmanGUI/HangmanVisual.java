package mikeHangmanGUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class HangmanVisual extends JPanel{
	
	private int wrongGuesses; //instance variable
	public static final int MAX_WRONG_GUESSES = 6;
	
	private static final int HEAD_DIAMETER = 50;
	private static final int BODY_LENGTH = 120;
	private static final int ARM_LENGTH = 40;
	private static final int ARM_POSITION = 30; //The number down from the "shoulders" of the man
	private static final int LEG_LENGTH = 55;
	private static final int ARM_ANGLE = 35; //In degrees, the angle created from the body of the man to his arm
	private static final int LEG_ANGLE = 20;
	
	/**
	 * Default Constructor
	 */
	public HangmanVisual(){
		wrongGuesses = 0;
		setSize(400, 400);
		repaint();
		setVisible(true);
	}
	
	/*
	 * Getter method for the number of Wrong Guesses
	 */
	public int getWrongGuesses(){
		return wrongGuesses;
	}
	
	/**
	 * Increments wrongGuesses, then repaints the man
	 */
	public void incrementWrongGuesses(){
		wrongGuesses++;
		repaint();
	}
	
	/*
	 * Resets wrongGuesses back to zero and repaints the pain
	 */
	public void resetWrongGuesses(){
		wrongGuesses = 0;
		repaint();
	}
	
	/**
	 * The following seven methods paint parts of the hangman. These methods are invoked in the paint() method. 
	 * They use final static ints so that the lengths of different body parts may be changed without having
	 * to recalculate where the lines are drawn. Note that the stage should not be changed because the alignment
	 * of the man will be off, unless that is also appropriately changed. 
	 * @param Graphics object 
	 */
	private void paintStage(Graphics g) { 
	    g.drawLine(75, 325, 325, 325);
	    g.drawLine(150, 325, 150, 25);
	    g.drawLine(150, 25, 275, 25);
	    g.drawLine(275, 25, 275, 75);
	}
	
	private void paintHead(Graphics g) {
		g.drawOval(275-(HEAD_DIAMETER/2), 75, HEAD_DIAMETER, HEAD_DIAMETER);
    }
	
	private void paintBody(Graphics g) {
		paintHead(g);
		g.drawLine(275, 75+HEAD_DIAMETER, 275, 75+HEAD_DIAMETER+BODY_LENGTH);
	}
	
	private void paintLeftArm(Graphics g) {
		int x = 275;
		int y = 75+HEAD_DIAMETER+ARM_POSITION;
		g.drawLine(x, y, x - distanceOfX(ARM_LENGTH, ARM_ANGLE), y + distanceOfY(ARM_LENGTH, ARM_ANGLE));
	
	}

	private void paintRightArm(Graphics g) {
		int x = 275;
		int y = 75+HEAD_DIAMETER+ARM_POSITION;
        g.drawLine(x, y, x + distanceOfX(ARM_LENGTH, ARM_ANGLE), y + distanceOfY(ARM_LENGTH, ARM_ANGLE));
	}
	
	private void paintLeftLeg(Graphics g) {
		int x = 275;
		int y = 75+HEAD_DIAMETER+BODY_LENGTH;
        g.drawLine(x, y, x - distanceOfX(LEG_LENGTH, LEG_ANGLE), y + distanceOfY(LEG_LENGTH, LEG_ANGLE));
	}
	
	private void paintRightLeg(Graphics g) {
		int x = 275;
		int y = 75+HEAD_DIAMETER+BODY_LENGTH;
        g.drawLine(x, y, x + distanceOfX(LEG_LENGTH, LEG_ANGLE), y + distanceOfY(LEG_LENGTH, LEG_ANGLE));
	}
	
	/**
	 * @return the distance of the imaginary leg of a triangle that is parallel with the X-Axis.
	 */
	private int distanceOfX(int length, int angle){
		return (int) (Math.sin(Math.toRadians(angle)) * length);
	}
	
	/**
	 * @return the distance of the imaginary leg of a triangle that is parallel with the Y-Axis.
	 */
	private int distanceOfY(int length, int angle){
		return (int) (Math.cos(Math.toRadians(angle)) *length);
	}
	
	/**
	 * This method should never be directly invoked, but is invoked by the repaint() method.
	 * Depending on the number of wrongGuesses, it knows which body parts to draw. It also
	 * sets the stroke of the lines so they are thicker than the default setting. 
	 */
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g; 
	    g2.setStroke(new BasicStroke(5,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
	    g2.setColor(Color.BLACK); 
		
		paintStage(g); //always printed

	    if (wrongGuesses==1) {
	    	paintHead(g);
	    } else if (wrongGuesses==2) {
	    	paintHead(g);
	    	paintBody(g);
	    } else if (wrongGuesses==3) {
	    	paintHead(g);
			paintBody(g);
	    	paintLeftArm(g);
	    } else if (wrongGuesses==4) {
	    	paintHead(g);
			paintBody(g);
			paintLeftArm(g);
	    	paintRightArm(g);
	    } else if (wrongGuesses==5) {
	    	paintHead(g);
			paintBody(g);
			paintLeftArm(g);
			paintRightArm(g);
	    	paintLeftLeg(g);
	    } else if (wrongGuesses==6) {
	    	paintHead(g);
			paintBody(g);
			paintLeftArm(g);
			paintRightArm(g);
			paintLeftLeg(g);
	    	paintRightLeg(g);
	    }
	}
	
	/**
	 * If you invoke this, the entire man is drawn. 
	 */
	public void endGame(){
		wrongGuesses = 6;
		repaint();
	}
	
}