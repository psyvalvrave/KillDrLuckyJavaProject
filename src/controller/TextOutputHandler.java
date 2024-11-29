package controller;

import java.io.IOException;
import java.util.function.Consumer;

public class TextOutputHandler implements Appendable {
  private Consumer<String> outputConsumer;

  public TextOutputHandler(Consumer<String> outputConsumer) {
      this.outputConsumer = outputConsumer;
  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
      outputConsumer.accept(String.valueOf(csq));
      return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
      outputConsumer.accept(csq.subSequence(start, end).toString());
      return this;
  }

  @Override
  public Appendable append(char c) throws IOException {
      outputConsumer.accept(String.valueOf(c));
      return this;
  }
}
