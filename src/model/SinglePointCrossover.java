package model;

import java.util.Random;

public class SinglePointCrossover implements IReproduceStrategy{

	@Override
	public Gen reproduce(Gen parent1, Gen parent2) {
		Random random = new Random();
		int number = random.nextInt(0, Gen.size + 1);
		Gen gen = new Gen(parent1.getIndex());
		if (number == 0) {
			gen = parent1;
		} else if (number == Gen.size - 1) {
			gen = parent2;
		} else {
			for (int i = 0; i < number; i++) {
				gen.setStateAt(i, parent1.getStateAt(i));
			}

			for (int i = number; i < Gen.size; i++) {
				gen.setStateAt(i, parent2.getStateAt(i));
			}
		}
		return gen;
	}
	
}
