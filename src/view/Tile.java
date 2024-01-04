package view;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
public class Tile extends JButton{
	private int row;
	private int column;
	private int state = 0;
	private Color colour = Color.BLACK;
	
	public Tile(int size, int state, Color colour, int y, int x) {
		
		//Store values into attributes
		this.row = y;
		this.column = x;
		
		this.state = state;
		this.colour = colour;
		
		setPreferredSize(new Dimension(size, size));
		
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setFocusable(false);
		
		setForeground(Color.RED);
		setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
		
		setState(this.state);
		
		
		
	}
	
	//ACCESSOR METHODS
	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getState() {
		return state;
	}
	
	
	public void setState(int state) { //Set the state of the tile and change the color and text accordingly
		this.state = state;
		
		switch(state) {
		case -1: 
			setText("X");
			setBackground(Color.WHITE);
			break;
		case 0:
			setText("");
			setBackground(Color.WHITE);
			break;
		case 1:
			setText("");
			setBackground(colour);
			break;
		}
	}
}
