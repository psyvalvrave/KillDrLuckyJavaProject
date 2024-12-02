package controller;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * This class is designed to encapsulate the behavior
 * of output operations, allowing the destination of the output 
 * to be defined externally via a consumer function.
 *
 * This allows for flexible output management, where the output 
 * could be directed to various endpoints such as logging, console printing, 
 * or other forms of string handling without changing the underlying
 * logic that generates the output.
 */
public class TextOutputHandler implements Appendable {
  private Consumer<String> outputConsumer;

  public TextOutputHandler(Consumer<String> outputConsumerInput) {
    this.outputConsumer = outputConsumerInput;
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
