package duke.packages;

import java.io.IOException;
import java.util.ArrayList;

import duke.Deadline;
import duke.Event;
import duke.Storage;
import duke.Task;
import duke.TaskList;
import duke.ToDo;
import duke.Ui;

/**
 * Represents the command for adding tasks into the Duke's taskList and hard disk.
 *
 * @author MrTwit99
 * @since 2023-02-06
 */
public class AddCommand implements Command {

    /**
     * Returns a String message of the newly added task. Executes the add task command and adds the task
     * in the parsedCmd into the taskList and adds its information into the hard disk.
     *
     * @param parsedCmd ArrayList of String type that contains parsed information about the task description and
     *                  status generated by Parser.parse().
     * @param tasks Duke's TaskList object that stores all Tasks objects.
     * @param storage Duke's Storage object to allow file access.
     * @param ui Duke's Ui object to display and print messages to the user. Acts as a screen.
     * @return String message of the newly added task.
     */
    @Override
    public String execute(ArrayList<String> parsedCmd, TaskList tasks, Storage storage, Ui ui) {
        ArrayList<Task> taskList = tasks.getTaskList();
        StringBuilder sb = new StringBuilder();
        boolean hasIssue = false;
        String message;
        Task newTask;

        newTask = this.getTask(parsedCmd); // creates a new task
        if (newTask == null) {
            hasIssue = true;
        }
        if (!hasIssue) {
            taskList.add(newTask);
            try {
                storage.writeToFile(newTask.getTaskInfo() + "\n", taskList);
                sb.append("    ____________________________________________________________\n")
                        .append("    Got it. I've added this task to the list:\n")
                        .append("      ").append(newTask.getTaskInfoStatus())
                        .append("\n    Now you have ").append(taskList.size()).append(" tasks in the list.\n")
                        .append("    ____________________________________________________________\n");
                message = sb.toString();
                sb.setLength(0);
            } catch (IOException e) {
                message = "An unexpected error has occurred: " + e.getMessage();
            }
            ui.printCommand(message);
            tasks.updateTaskList(taskList);
        } else {
            message = parsedCmd.get(0);
        }
        return message;
    }

    /**
     * Creates a new Task object (ToDos, Deadline or Event) based on the parsed command information.
     *
     * @param parsedCmd ArrayList of String type containing the parsed command.
     * @return Task object which can be one of the following types: ToDos, Deadline or Event.
     */
    private Task getTask(ArrayList<String> parsedCmd) {
        Task newTask = null;
        switch (parsedCmd.size()) {
        case 2: // new ToDos task
            newTask = new ToDo(parsedCmd.get(1));
            break;
        case 3: // new ToDos (COMPLETED) task
            newTask = new ToDo(parsedCmd.get(2));
            newTask.setDone();
            break;
        case 4: // new Deadline task
            newTask = new Deadline(parsedCmd.get(1), parsedCmd.get(2), parsedCmd.get(3));
            break;
        case 5: // new Deadline (COMPLETED) task
            newTask = new Deadline(parsedCmd.get(2), parsedCmd.get(3), parsedCmd.get(4));
            newTask.setDone();
            break;
        case 6: // new Event task
            newTask = new Event(parsedCmd.get(1), parsedCmd.get(2), parsedCmd.get(4),
                    parsedCmd.get(3), parsedCmd.get(5));
            break;
        case 7: // new Event (COMPLETED) task
            newTask = new Event(parsedCmd.get(2), parsedCmd.get(3), parsedCmd.get(5),
                    parsedCmd.get(4), parsedCmd.get(6));
            newTask.setDone();
            break;
        default:
            break;
        }
        return newTask;
    }
}
