package view;
import javax.swing.JPanel;
public class CreatorGrid extends Grid{
	public CreatorGrid(JPanel padderPanel) {
		super(padderPanel);
		setGridSize(15, 15);
	}
	
	public void setGridSize(int rows, int cols) { //Set the grid to a specific size
		super.setGridSize(rows, cols);
		
		maxColLabelSize = 10;
		maxRowLabelSize = 10;
		
	}
	
	
	public void rightMouseButtonPressEvent(Tile tile) {
	}
	
	public void leftMouseButtonPressEvent(Tile tile) { //Runs when a tile is left clicked
		if (tile.getState() == 1) {
			tile.setState(0);
		} else {
			tile.setState(1);
		}
		
		tileStates[tile.getRow()][tile.getColumn()] = tile.getState();
		
	}
	
	public void mouseButtonPressEvent() {
	}
	
	
	public void clearGrid() { //Sets all tiles to white and clears the labels
		super.clearGrid();
		
		for(int i = 0; i < colLabels.length; i++) {
			colLabels[i] = "";
			
		}
		
		for(int i = 0; i < rowLabels.length; i++) {
			rowLabels[i] = "";
		}
		
	}
}
