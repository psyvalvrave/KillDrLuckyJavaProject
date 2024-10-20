package worldController;

import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;

public class RandomDecisionMaker {
    private Random random;
    private Queue<Integer> presetDecisions;

    public RandomDecisionMaker() {
        this.random = new Random();
    }

    public RandomDecisionMaker(Integer... decisions) {
        this.presetDecisions = new LinkedList<>();
        for (int decision : decisions) {
            this.presetDecisions.add(decision);
        }
    }

    public int getNextDecision(int bound) {
        if (presetDecisions != null && !presetDecisions.isEmpty()) {
            return presetDecisions.poll();
        }
        return random.nextInt(bound);
    }
}

