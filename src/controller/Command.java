package controller;

import java.io.IOException;

public interface Command {
  void execute(Appendable output) throws IOException, InterruptedException;
}

