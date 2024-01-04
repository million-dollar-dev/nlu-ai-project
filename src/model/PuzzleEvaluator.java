package model;

public class PuzzleEvaluator {
	//A method to check if a grid state matches the row and column labels
		public static boolean evaluate(int[][] attempt, String[] rowLabels, String[] colLabels) {
			
			//For each row label...
			for(int r = 0; r < attempt.length; r++) {
				//Generate a row label based on grid states and check if it matches the given row label
				if(!PuzzleGenerator.genRowLabel(attempt[r]).strip().equals(rowLabels[r].strip())) {
					return false;
				}
			}
			
			//For each column label...
			for(int c = 0; c < attempt[0].length; c++) {
				//Generate a column label based on grid states and check if it matches the given column label
				if(!PuzzleGenerator.genColLabel(getColumn(attempt,c)).strip().equals(colLabels[c].strip())){
					return false;
				}
			}
			
			return true;
		}
		
		
		//Returns an array of states of a column of the input grid
		public static int[] getColumn(int[][] input, int col) {
			
			int[] output = new int[input.length];
			
			//Loop through each row and copy the value in the specific column into the array
			for(int i = 0; i < input.length; i++) {
				output[i] = input[i][col];
			}
			
			return output;
			
		}
}
