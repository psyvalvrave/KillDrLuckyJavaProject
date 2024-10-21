package worldController;

class FakeRandomNumberGenerator extends RandomNumberGenerator {
    private int nextIntResult;

    public void setNextIntResult(int result) {
        nextIntResult = result;
    }

    @Override
    public int nextInt(int bound) {
        return nextIntResult;
    }
}
