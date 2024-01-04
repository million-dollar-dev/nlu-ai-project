package model;

import java.util.Random;

public class TwoPointCrossover implements IReproduceStrategy {

	@Override
	public Gen reproduce(Gen parent1, Gen parent2) {
		Random random = new Random();
		int number1 = random.nextInt(0, Gen.size);
		int number2 = random.nextInt(number1,  Gen.size + 1);
		Gen gen = new Gen(parent1.getIndex());
		for (int i = 0; i < number1; i++)
			gen.setStateAt(i, parent1.getStateAt(i));
		for (int i = number1; i < number2; i++) {
			gen.setStateAt(i, parent2.getStateAt(i));
		}
		for (int i =  number2; i < Gen.size; i++)
			gen.setStateAt(i, parent1.getStateAt(i));
		return gen;
	}
}
