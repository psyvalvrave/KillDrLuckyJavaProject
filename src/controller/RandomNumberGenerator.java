package controller;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * This class provides a mechanism for generating pseudo-random 
 * numbers with an optional fixed sequence
 * for predictable outcomes in scenarios such as testing. 
 */
public class RandomNumberGenerator {
  private Random random;
  private Queue<Integer> fixedOutputs;

  /**
   * Constructs a RandomNumberGenerator with a new random number generator instance.
   */
  public RandomNumberGenerator() {
    random = new Random();
  }

  /**
   * Constructs a RandomNumberGenerator with a predefined sequence of numbers.
   * This constructor is particularly useful for tests where 
   * deterministic output is required.
   *
   * @param fixedValues An array of integers that will 
   *        be returned sequentially in subsequent calls to {@code nextInt}.
   */
  public RandomNumberGenerator(int... fixedValues) {
    fixedOutputs = new LinkedList<>();
    for (int value : fixedValues) {
      fixedOutputs.add(value);
    }
  }

  /**
   * Generates the next pseudo-random number.
   * If a fixed sequence of outputs has been set up using the 
   * constructor with parameters, this method will return
   * the next number from that sequence. Once the sequence is exhausted, 
   * it behaves like a standard random number generator.
   *
   * @param bound the upper bound (exclusive) for the random number. 
   *        This is ignored if using fixed outputs.
   * @return an integer, either from the fixed sequence or pseudo-randomly 
   *        generated, that is less than the specified bound.
   */
  public int nextInt(int bound) {
    if (fixedOutputs != null) {
      return fixedOutputs.poll();
    }
    return random.nextInt(bound);
  }
}
