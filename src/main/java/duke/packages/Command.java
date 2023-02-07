package duke.packages;

import java.util.ArrayList;

import duke.Storage;
import duke.TaskList;
import duke.Ui;

/**
 * An interface that represents a command entered by the user.
 *
 * @author MrTwit99
 * @since 2023-02-06
 */
public interface Command {
    /**
     * Returns a String message generated by the command execution.
     *
     * @param parsedCmd ArrayList of String type that contains parsed information about the task description and
     *                  status generated by Parser.parse().
     * @param tasks Duke's TaskList object that stores all Tasks objects.
     * @param storage Duke's Storage object to allow file access.
     * @param ui Duke's Ui object to display and print messages to the user. Acts as a screen.
     * @return String message generated by the command execution.
     */
    public String execute(ArrayList<String> parsedCmd, TaskList tasks, Storage storage, Ui ui);
}