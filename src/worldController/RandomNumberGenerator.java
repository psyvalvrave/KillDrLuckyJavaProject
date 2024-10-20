package worldController;

import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;

public class RandomNumberGenerator {
    private Random random;
    private Queue<Integer> fixedOutputs;

    public RandomNumberGenerator() {
        random = new Random();
    }

    public RandomNumberGenerator(int... fixedValues) {
        fixedOutputs = new LinkedList<>();
        for (int value : fixedValues) {
            fixedOutputs.add(value);
        }
    }

    public int nextInt(int bound) {
        if (fixedOutputs != null) {
            return fixedOutputs.poll();
        }
        return random.nextInt(bound);
    }
}
