package duke.commands;

import java.io.IOException;
import java.util.ArrayList;

import duke.Storage;
import duke.Task;
import duke.TaskList;
import duke.Ui;

/**
 * Represents the command for unmarking tasks in the Duke's taskList and hard disk.
 *
 * @author MrTwit99
 * @since 2023-02-06
 */
public class UnmarkCommand implements Command {

    /**
     * Returns a String message signifying whether the unmark command managed to run. Unable to run if task selected is
     * out of range.
     *
     * @param parsedCmd ArrayList of String type that contains parsed information about the task description and
     *                  status generated by Parser.parse().
     * @param tasks Duke's TaskList object that stores all Tasks objects.
     * @param storage Duke's Storage object to allow file access.
     * @param ui Duke's Ui object to display and print messages to the user. Acts as a screen.
     * @return String message of the unmark command run status.
     */
    @Override
    public String execute(ArrayList<String> parsedCmd, TaskList tasks, Storage storage, Ui ui) {
        String message;
        StringBuilder sb = new StringBuilder();
        ArrayList<Task> taskList = tasks.getTaskList();
        int taskNumber = Integer.parseInt(parsedCmd.get(1));

        if ((taskNumber <= taskList.size()) && (taskNumber > 0)) { // checks if the task to unmark is within list
            Task tempTask = taskList.get(taskNumber - 1);
            String oldTaskInfo = tempTask.getTaskInfo();
            sb.append("    ____________________________________________________________________________________\n");
            sb.append(tempTask.setIncomplete()).append("\n");
            sb.append("    ____________________________________________________________________________________\n");
            taskList.set(taskNumber - 1, tempTask);
            message = sb.toString();
            sb.setLength(0);
            try {
                storage.writeToFile(oldTaskInfo, tempTask.getTaskInfo(), taskNumber - 1, taskList);
            } catch (IOException e) {
                message = "An unexpected error has occurred: " + e.getMessage();
            }
        } else {
            sb.append("    ____________________________________________________________________________________\n");
            sb.append("    The task you are trying to unmark is out of range! Try again!\n");
            sb.append("    ____________________________________________________________________________________\n");
            message = sb.toString();
            sb.setLength(0);
        }
        ui.printCommand(message);
        tasks.updateTaskList(taskList);
        return message;
    }
}
