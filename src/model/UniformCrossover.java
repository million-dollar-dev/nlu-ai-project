package model;

public class UniformCrossover implements IReproduceStrategy{

	@Override
	public Gen reproduce(Gen parent1, Gen parent2) {
		
		Gen gen = new Gen(parent1.getIndex());
		for (int i = 0; i < Gen.size; i++) {
			if (i % 2 == 0)
				gen.setStateAt(i, parent1.getStateAt(i));
			else
				gen.setStateAt(i, parent2.getStateAt(i));
		}		
		return gen;
	}

}
