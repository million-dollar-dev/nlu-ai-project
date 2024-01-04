package model;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import view.GameView;

public class PuzzleGenerator {
	// Generates a randomized puzzle given the size and required tile count
	public static Puzzle genPuzzle(int height, int width, int tileCount) {

		// Make a new empty puzzle and store values
		Puzzle output = new Puzzle(height, width);
		output.setTileCount(tileCount);

		// Make empty labels and 2D tile state array
		String[] outputRowLabels = new String[height];
		String[] outputColLabels = new String[width];
		int[][] outputTiles = new int[height][width];

		int count = 0;

		Random rand = new Random();

		while (count < tileCount) {
			// Choose a random x and y coords
			int rx = rand.nextInt(width);
			int ry = rand.nextInt(height);

			// If that tile is white, change it to black and update count
			if (outputTiles[ry][rx] == 0) {
				outputTiles[ry][rx] = 1;
				count++;
			}
		}

		// For each row, generate the row label and store it in the array
		int maxRowLabel = 0;
		for (int r = 0; r < outputRowLabels.length; r++) {
			outputRowLabels[r] = genRowLabel(outputTiles[r]);
			if (outputRowLabels[r].length() > maxRowLabel)
				maxRowLabel = outputRowLabels[r].length();
		}
		output.setMaxRowLabelSize(maxRowLabel);

		// For each column, generate the column label and store it in the array
		int maxColLabel = 0;
		for (int c = 0; c < outputColLabels.length; c++) {
			outputColLabels[c] = genColLabel(PuzzleEvaluator.getColumn(outputTiles, c));
			if (outputColLabels[c].length() > maxColLabel)
				maxColLabel = outputColLabels[c].length();
		}
		output.setMaxColLabelSize(maxColLabel);

		// Add the labels to the puzzle
		output.setRowLabels(outputRowLabels);
		output.setColLabels(outputColLabels);
		output.setTiles(outputTiles);
		return output;
	}

	public static Puzzle genPuzzle(int[][] tiles) {

		// Make a new empty puzzle and store values
		Puzzle output = new Puzzle(tiles.length, tiles[0].length);

		// Make empty labels and 2D tile state array
		String[] outputRowLabels = new String[tiles.length];
		String[] outputColLabels = new String[tiles[0].length];
		int[][] outputTiles = tiles;

		// For each row, generate the row label and store it in the array
		int maxRowLabel = 0;
		for (int r = 0; r < outputRowLabels.length; r++) {
			outputRowLabels[r] = genRowLabel(outputTiles[r]);
			if (outputRowLabels[r].length() > maxRowLabel)
				maxRowLabel = outputRowLabels[r].length();
		}
		output.setMaxRowLabelSize(maxRowLabel);

		// For each column, generate the column label and store it in the array
		int maxColLabel = 0;
		for (int c = 0; c < outputColLabels.length; c++) {
			outputColLabels[c] = genColLabel(PuzzleEvaluator.getColumn(outputTiles, c));
			if (outputColLabels[c].length() > maxColLabel)
				maxColLabel = outputColLabels[c].length();
		}
		output.setMaxColLabelSize(maxColLabel);

		// Add the labels to the puzzle
		output.setRowLabels(outputRowLabels);
		output.setColLabels(outputColLabels);
		output.setTiles(outputTiles);
		
//		System.out.println("test");
//		output.showPuzzle();

		return output;
	}

	// Generate a row label based on a row's tile states
	public static String genRowLabel(int[] row) {
		String output = "";
		Stack<Integer> stack = new Stack<Integer>();

		// For each tile
		for (int i : row) {

			if (i == 1) {
				if (!stack.isEmpty() && stack.peek() > 0) {
					// If the tile is filled and the stack is currently counting a stretch of filled
					// tiles, increment the length of the stretch
					int x = stack.peek();
					stack.pop();
					stack.push(x + 1);
				} else {
					// If the tile is filled, but the stack is empty, add 1 to the stack
					stack.push(1);
				}
			} else {
				if (!stack.isEmpty() && stack.peek() > 0) {
					// If the tile is empty and the stack is currently counting a stretch of filled
					// tiles, add 0 to the stack
					stack.push(0);
				}
			}
		}

		while (!stack.isEmpty()) { // Loop through the stack

			// Create the label based on the lengths stored in the stack (which would be
			// seperated by 0s)
			int i = stack.pop();
			if (i == 0) {
				output = " " + output;
			} else {
				output = i + output;
			}
		}

		// Clean up the label of any whitespace on the ends
		output = output.strip();

		// If the label is currently empty (all white tiles), the label is just 0
		if (output.length() == 0) {
			output = "0";
		}

		return output;
	}

	// Generate a column label based on the row's tile states
	public static String genColLabel(int[] col) {
		// Repeat the same process as generating a row label, but just add new line
		// characters instead of spaces between stretch lengths
		String output = "";
		Stack<Integer> stack = new Stack<Integer>();

		for (int i : col) {
			if (i == 1) {
				if (!stack.isEmpty() && stack.peek() > 0) {
					int x = stack.peek();
					stack.pop();
					stack.push(x + 1);
				} else {
					stack.push(1);
				}
			} else {
				if (!stack.isEmpty() && stack.peek() > 0) {
					stack.push(0);
				}
			}
		}

		while (!stack.isEmpty()) {
			int i = stack.pop();
			if (i == 0) {
				output = "\n" + output;
			} else {
				output = i + output;
			}
		}
		output = output.strip();
		if (output.length() == 0) {
			output = "0";
		}
		return output;
	}
	public static void print(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static int[][] copyArray(int[][] source) {
		int[][] destination = new int[source.length][];
		for (int i = 0; i < source.length; ++i) {
            destination[i] = new int[source[i].length];
            System.arraycopy(source[i], 0, destination[i], 0, destination[i].length);
       }
		return destination;
	}
}
