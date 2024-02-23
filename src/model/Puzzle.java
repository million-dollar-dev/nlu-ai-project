package model;

import java.awt.Color;
import java.util.Arrays;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import view.GameView;

public class Puzzle {
	private int numRows;
	private int numCols;

	private int[][] tileStates;

	private String[] colLabels;
	private String[] rowLabels;

	private Gen[] gens;
	private GA[] gas;

	private int maxColLabelSize = 5;
	private int maxRowLabelSize = 5;

	private int tileCount = 0;


	public Puzzle(int rows, int columns) {
		numRows = rows;
		numCols = columns;

		tileStates = new int[rows][columns];

		colLabels = new String[columns];
		rowLabels = new String[rows];
	}

	public void generateGen() {
		Gen.setSize(numCols);
		Gen.setTiles(getTiles());
		gens = new Gen[numRows];
		gas = new GA[numRows];
		for (int i = 0; i < numCols; i++) {
			gas[i] = new GA(i);
		}

	}

	public void solveWithGA() {
		generateGen();
		for (int iterator = 0; iterator < GA.generation_number; iterator++) {
			int bestFitness = 0;
			int worstFitness = 0;
			for (int i = 0; i < numCols; i++) {
				gas[i].execute();
				gens[i] = gas[i].getBestSolution();

				bestFitness += gens[i].getFitness();
				worstFitness += gas[i].getWorstSolution().getFitness();
			}
//			bestFitness = (int) Math.pow(bestFitness, 2);
//			worstFitness = (int) Math.pow(worstFitness, 2);
			convertGenToTiles();
			GameView.gameGrid.repaintGrid(getTiles());
			GameView.addChartData(bestFitness, "Best finess", iterator);
			GameView.addChartData(worstFitness, "Worst finess", iterator);
			try {
				Thread.sleep(400); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
			if (bestFitness == 0) {
				GameView.lbStatus.setForeground(Color.GREEN);;
				GameView.lbStatus.setText("Completed!");
				GameView.lbGene.setText(iterator + 1 + "");
				break;
			}
			
			if (iterator == GA.generation_number - 1 && bestFitness != 0) {
				GameView.lbStatus.setForeground(Color.RED);
				GameView.lbStatus.setText("Failed!");
				GameView.lbGene.setText(iterator + 1 + "");
			}
		}
		
		convertGenToTiles();
	}

	public void convertGenToTiles() {
		int[][] temp = new int[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				int[] arr = gens[i].getState();
				temp[i][j] = arr[j];
			}
		}
		setTiles(temp);
	}

	public void setColLabels(String[] labels) { // Set the column labels and update the maximum column label size
		colLabels = labels;

		maxColLabelSize = getMaxColLabelSize(labels);
	}

	public void setRowLabels(String[] labels) { // Set the row labels and update the maximum row label size
		rowLabels = labels;

		maxRowLabelSize = getMaxRowLabelSize(labels);
	}

	public static int getMaxRowLabelSize(String[] labels) { // Determines the maximum size of a list of row labels
		int maxRowLabelSize = 5;

		for (String lbl : labels) {
			maxRowLabelSize = Math.max(maxRowLabelSize, lbl.length());
		}

		return maxRowLabelSize;

	}

	public static int getMaxColLabelSize(String[] labels) { // Determines the maximum size of a list of column labels
		int maxColLabelSize = 5;

		for (String lbl : labels) {
			maxColLabelSize = Math.max(maxColLabelSize, findVertLength(lbl));
		}

		return maxColLabelSize;

	}

	// ACCESSOR METHODS
	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public int[][] getTiles() {
		return tileStates;
	}

	public String[] getColLabels() {
		return colLabels;

	}

	public String[] getRowLabels() {
		return rowLabels;
	}

	public int getMaxRowLabelSize() {
		return maxRowLabelSize;
	}

	public int getMaxColLabelSize() {
		return maxColLabelSize;
	}

	public int getTileCount() {
		return tileCount;

	}

	public void setTileCount(int n) {
		tileCount = n;
	}

	public void setTiles(int[][] tiles) {
		this.tileStates = tiles;

	}

	public static int findVertLength(String str) { // Determines the vertical size of a column label (number of lines)
		// Check for empty or null label cases
		if (str == null || str.length() == 0)
			return 0;

		// Count the number of new line characters in the array
		int i = 1;
		for (char c : str.toCharArray()) {
			if (c == '\n') {
				i++;
			}
		}
		return i;

	}

	public void setMaxColLabelSize(int maxColLabelSize) {
		this.maxColLabelSize = maxColLabelSize;
	}

	public void setMaxRowLabelSize(int maxRowLabelSize) {
		this.maxRowLabelSize = maxRowLabelSize;
	}

	public void showPuzzle() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				System.out.print(tileStates[i][j] + " ");
			}
			System.out.println();
		}
	}

}
