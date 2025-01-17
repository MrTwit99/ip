package duke;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import duke.commands.AddCommand;
import duke.commands.ByeCommand;
import duke.commands.DeleteCommand;
import duke.commands.FindCommand;
import duke.commands.HelpCommand;
import duke.commands.ListCommand;
import duke.commands.MarkCommand;
import duke.commands.ReminderCommand;
import duke.commands.UnmarkCommand;
import javafx.application.Platform;

/**
 * Represents the Duke application, capable of storing 3 types of Tasks ("ToDos", "Deadline" & "Event")
 * in the hard disk for users to refer to at any point of time.
 * <p></p>
 * Acts as a form of reminder / tracker and supports the following functions:
 * add, remove, mark, unmark and display list.
 * @author MrTwit99
 * @since 2023-02-01
 */
public class Duke {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    private boolean isExit = false;

    /**
     * Returns a Duke object that requires the filePath and folderPath to initialize the application,
     * retrieving tasks stored in the hard disk.
     *
     * @param filePath  Relative path to locate the file with the stored tasks.
     * @param folderPath Relative path to locate the directory storing the file.
     */
    public Duke(String filePath, String folderPath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath, folderPath);
        try {
            this.tasks = new TaskList(storage.load());
        } catch (IOException e) {
            this.ui.showLoadingError(e);
            this.tasks = new TaskList(new ArrayList<String>());
        }
    }

    /**
     * Executes the Duke application, requires user input to perform necessary actions and stops the Duke application
     * when user inputs the given command to end.
     * <p></p>
     * Functions currently supported by Duke: add Task, remove Task, mark Task, unmark Task, delete Task, list, bye.
     * @throws IOException On input error.
     * @see IOException
     */
    public void run() throws IOException {
        ui.showWelcome();
        while (!isExit) {
            ArrayList<String> commandInfoList;
            commandInfoList = Parser.parse(ui.readCommand());
            assert commandInfoList.size() > 0 : "Assertion Error has occurred";
            if (commandInfoList.size() != 0) { // checks if the command to run is VALID
                switch (commandInfoList.get(0)) { // runs the command as per the user request
                case "bye":
                    this.isExit = true;
                    new ByeCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "list":
                    new ListCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "find":
                    new FindCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "mark":
                    new MarkCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "unmark":
                    new UnmarkCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "delete":
                    new DeleteCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "reminder":
                    new ReminderCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "help":
                    new HelpCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    break;
                case "error":
                    break;
                default:
                    try {
                        new AddCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                    } catch (DateTimeParseException e) {
                        ui.printInvalidDateError();
                        break;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Returns a String message generated by the Commands that is to be displayed to the user via the Stage.
     *
     * @param input Input given by users via GUI.
     * @return String message generated by the Commands.
     */
    public String getResponse(String input) {
        String message = "";
        ArrayList<String> commandInfoList = Parser.parse(input);
        if (isExit) {
            Platform.exit();
            System.exit(0);
        }
        if (commandInfoList.size() != 0) { // checks if the command to run is VALID
            switch (commandInfoList.get(0)) { // runs the command as per the user request
            case "bye":
                message = new ByeCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                this.isExit = true;
                break;
            case "list":
                message = new ListCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "find":
                message = new FindCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "mark":
                message = new MarkCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "unmark":
                message = new UnmarkCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "delete":
                message = new DeleteCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "reminder":
                message = new ReminderCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "help":
                message = new HelpCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                break;
            case "error":
                message = commandInfoList.get(1);
                break;
            default:
                try {
                    message = new AddCommand().execute(commandInfoList, this.tasks, this.storage, this.ui);
                } catch (DateTimeParseException e) {
                    message = ui.printInvalidDateError();
                    break;
                }
                break;
            }
        }
        assert commandInfoList.size() > 0 : "Assertion Error has occurred";
        assert !message.equals("");
        return message;
    }

    /**
     * This is the main method that creates the Duke object and boots the application up via run() method.
     *
     * @param args Input given by users via the CLI
     * @throws IOException On input error.
     * @see IOException
     */
    public static void main(String[] args) throws IOException {
        new Duke("data/storage.txt", "data").run();
    }
}
