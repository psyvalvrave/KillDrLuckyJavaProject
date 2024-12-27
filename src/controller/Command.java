package controller;

import java.io.IOException;

/**
 * The Command interface defines the contract for command objects used in the application.
 * Command objects encapsulate all the information needed to perform an action or trigger
 * an event at a later time.
 */
public interface Command {
  /**
   * Executes the command encapsulated by this instance, using the provided Appendable object
   * to output the results of the command execution. This method can throw an IOException
   * if an I/O error occurs during command execution, which might involve writing to the output.
   *
   * @param output The Appendable object where the command output will be written.
   * @return Return a execute result.
   * @throws IOException If an I/O error occurs during command execution.
   * @throws InterruptedException If the command execution is interrupted.
   * 
   */
  String execute(Appendable output) throws IOException, InterruptedException;
}

