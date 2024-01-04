package view;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import model.Puzzle;
public class GameGrid extends Grid{
	private JProgressBar progressBar;
	private int currTileCount = 0;

	public GameGrid(JPanel padderPanel, JProgressBar progressBar) {
		super(padderPanel);
		this.progressBar = progressBar;

	}

	public void loadPuzzle(Puzzle puzzle) { //Load a puzzle by storing all the info into attributes 
		super.loadPuzzle(puzzle);
		
		progressBar.setMaximum(tileCount);
		resetProgressBar();

	}

	

	public void rightMouseButtonPressEvent(Tile tile) { //Runs when a right click occurs on a tile
		
		if (tile.getState() == -1) {
			tile.setState(0);
		} else if (tile.getState() == 0) {
			tile.setState(-1);
		} else {
			tile.setState(-1);
			currTileCount--;
		}
	
		//Update the tileStates 2D array
		tileStates[tile.getRow()][tile.getColumn()] = tile.getState();

	}

	public void leftMouseButtonPressEvent(Tile tile) { //Runs when a left click occurs on a tile
		if (tile.getState() == 1) {
			tile.setState(0);
			currTileCount--;
		} else {
			tile.setState(1);
			currTileCount++;
		}
		
		//Update the tileStates 2D array
		tileStates[tile.getRow()][tile.getColumn()] = tile.getState();
		
	}

	public void mouseButtonPressEvent() { //Occurs when a tile is pressed
		progressBar.setValue(currTileCount);
	}
	
	public void drawGrid() { //Override drawGrid() to draw the progress bar as well
		progressBar.setForeground(gridColour);
		progressBar.setValue(currTileCount);
		
		progressBar.revalidate();
		progressBar.repaint();
		super.drawGrid();
	}
	
	
	public void clearGrid() { //Set all the tiles to white and reset the progress bar
		super.clearGrid();
		
		resetProgressBar();
	}

	private void resetProgressBar() { //Reset the progress bar
		currTileCount = 0;
		progressBar.setValue(0);
	}
}
