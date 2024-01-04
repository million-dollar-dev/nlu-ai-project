package model;

import java.util.*;

public class GA {
	public static int population_size = 20;
	public static double selection_rate = 0.9;
	public static double mutation_rate = 0.025;
	public static int generation_number = 100;
	private Random rd = new Random();
	private List<Gen> population;
	public static IReproduceStrategy reproduceStrategy = new SinglePointCrossover();
	
	public int index;
	public GA(int index) {
		this.index = index;
		init();
	}
	
	public void init() {
		population = new ArrayList<>();
		for (int i = 0; i < population_size; i++) {
			Gen aGen = new Gen(index);
			population.add(aGen);
		}
	}
	
	
	public void show() {
		for (Gen gen : population) {
			System.out.println(gen.toString() + ": " + gen.getFitness());
		}
		System.out.println("-----------------------------------");
	}
	
	public void execute() {
		List<Gen> newPopulation = new ArrayList<>();
		for (int i = 0; i < population_size; i++) {
			Gen parent1 = getParentByTournamentSelection(selection_rate);
			Gen parent2 = getParentByRandomSelection(selection_rate);

			Gen child1 = reproduce(parent1, parent2);
			if (rd.nextDouble() < mutation_rate) {
				mutate(child1);
			}
			Gen child2 = reproduce(parent2, parent1);
			if (rd.nextDouble() < mutation_rate) {
				mutate(child1);
			}
			if (rd.nextDouble() < mutation_rate) {
				mutate(child2);
			}
			
			newPopulation.add(child1);
			newPopulation.add(child2);
		}
		population = newPopulation;
		Collections.sort(population);
	}
	
	public Gen getBestSolution() {
		return population.get(0);
	}
	
	public Gen getWorstSolution() {
		return population.get(population_size - 1);
	}
	
	private void mutate(Gen child) {
		int index = rd.nextInt(0, Gen.size);
		child.changeState(index);
	}

	public Gen reproduce(Gen parent1, Gen parent2) {
		return reproduceStrategy.reproduce(parent1, parent2);
	}

	private Gen getParentByTournamentSelection(double selection_rate) {
		int k = 4;
		Gen bestNode = getParentByRandomSelection(selection_rate);
		for (int i = 0; i < k; i++) {
			Gen randomNode = getParentByRandomSelection(selection_rate);
			if (bestNode.getFitness() > randomNode.getFitness())
				bestNode = randomNode;

		}
		return bestNode;
	}

	private Gen getParentByRandomSelection(double selection_rate) {
		int k = (int) (selection_rate * population.size());
		int num = rd.nextInt(0, k);
		Gen gen = population.get(num);
		return gen;
	}
	
	public static void setPopulation_size(int population_size) {
		GA.population_size = population_size;
	}

	public static void setSelection_rate(double selection_rate) {
		GA.selection_rate = selection_rate;
	}

	public static void setMutation_rate(double mutation_rate) {
		GA.mutation_rate = mutation_rate;
	}

	public static void setGeneration_number(int generation_number) {
		GA.generation_number = generation_number;
	}

	public static void setReproduceStrategy(IReproduceStrategy reproduceStrategy) {
		GA.reproduceStrategy = reproduceStrategy;
	}
	
}
