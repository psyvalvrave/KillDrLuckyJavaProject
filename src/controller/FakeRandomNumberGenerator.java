package controller;

/**
 * A specialized version of {@link RandomNumberGenerator} that allows for controlled outputs
 * in testing scenarios. This class is designed to always return a predefined integer
 * set via the {@code setNextIntResult} method, ignoring any bounds.
 */
class FakeRandomNumberGenerator extends RandomNumberGenerator {
  private int nextIntResult;

  /**
   * Sets the next integer to be returned by the {@code nextInt} method.
   * This method allows for the deterministic and repeatable behavior necessary during testing.
   *
   * @param result the integer result that will be returned on subsequent calls to {@code nextInt}.
   */
  public void setNextIntResult(int result) {
    nextIntResult = result;
  }

  /**
   * Returns the preset integer value regardless of the provided bound, 
   * effectively ignoring the bound parameter. This method 
   * ensures consistent results during testing.
   *
   * @param bound the upper bound (exclusive) for the random number, 
   *        which is ignored in this implementation.
   * @return the predefined integer set via {@code setNextIntResult}.
   */
  @Override
  public int nextInt(int bound) {
    return nextIntResult;
  }
}
