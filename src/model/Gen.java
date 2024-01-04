package model;

import java.util.Arrays;
import java.util.Random;

public class Gen implements Comparable<Gen>{
	public static int[][] tiles;
	public static int size;
	private int[] state;
	private int index;
	private Random rd = new Random();
	public Gen(int index) {
		this.index = index;
		this.state = new int[size];
		for (int i = 0; i < size; i++) {
			state[i] = rd.nextInt(0, 2);
		}
	}
	
	public int getFitness() {
		int h = 0;
		for (int i = 0; i < size; i++) {
			if (state[i] != tiles[index][i])
				h++;
		}
		return h;
	}
	
	public void setStateAt(int index, int value) {
		this.state[index] = value;
	}
	
	public int getStateAt(int index) {
		return state[index];
	}

	@Override
	public String toString() {
		return "" + Arrays.toString(state) + " Fitness: " + getFitness();
	}
	
	@Override
	public int compareTo(Gen o) {
		// TODO Auto-generated method stub
		return this.getFitness() - o.getFitness();
	}

	public static int getSize() {
		return size;
	}

	public static void setSize(int size) {
		Gen.size = size;
	}

	public int[] getState() {
		return state;
	}

	public void setState(int[] state) {
		this.state = state;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void changeState(int index) {
		if (state[index] == 0)
			state[index] = 1;
		else
			state[index] = 0;
	}

	public static void setTiles(int[][] tiles) {
		Gen.tiles = tiles;
	}
	
}
