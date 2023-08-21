package main.java;

import main.java.actions.JukeExceptionAction;
import main.java.actions.JukeExitAction;
import main.java.exceptions.JukeException;
import main.java.primitivies.JukeObject;
import main.java.actions.JukeAction;

import java.util.Optional;
import java.util.Scanner;

/**
 * Orchestrates the operation of Juke by accepting commands and dispatching
 * them to the correct target methods.
 */
public class JukeOrchestrator extends JukeObject {
    /** Separator used by the virtual assistant to demarcate the start or end of a conversation */
    private static final String SEPARATOR = "\n-----------------------------------------------------------------------\n";

    /**
     * Juke Logo made from ASCII Art. Credits go to:
     * <a href="https://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20">...</a>
     */
    private static final String LOGO =
            "       __      __\n" +
                    "      / /_  __/ /_____\n" +
                    " __  / / / / / //_/ _ \\\n" +
                    "/ /_/ / /_/ / ,< /  __/\n" +
                    "\\____/\\__,_/_/|_|\\___/";

    /** Introductory statement used by the assistant when first booting up. */
    private static final String INTRO = "What's up! My name is Juke (J|ava D|uke) and I will be your personal\n" +
            "assistant for today! How may I assist you?";

    /** The Virtual Assistant's output CLI prompt. */
    private static final String JUKEOUTPUT = ">>> ";

    /** The user's input CLI prompt. */
    private static final String JUKEINPUT = "juke> ";

    /** Scanner instance used to capture user input. */
    private final Scanner jukeScanner;

    /** Instance of JukeTaskManager that handles all JukeTasks. */
    private final JukeTaskManager jukeTaskManager;

    /**
     * Constructor for JukeOrchestrator.
     * @param jukeScanner Scanner object to read in user input
     */
    private JukeOrchestrator(Scanner jukeScanner) {
        this.jukeScanner = jukeScanner;
        this.jukeTaskManager = JukeTaskManager.of();
    }

    /**
     * Factory method that creates a JukeOrchestrator with the necessary Scanner object.
     * @param scanner Scanner object to read in user input
     */
    public static JukeOrchestrator of(Scanner scanner) {
        return new JukeOrchestrator(scanner);
    }


    /**
     * Method that begins the operation of the Assistant.
     */
    public void start() {
        this.printIntroduction();
        this.dispatch();
    }

    /**
     * Dispatches the command and acts on it.
     */
    private void dispatch() {
        JukeAction action = null;

        do {
            try {
                // obtain user input
                System.out.print(JukeOrchestrator.JUKEINPUT);
                String inputCommand = this.jukeScanner.nextLine();

                // parse the action into a JukeAction object
                action = JukeAction.of(inputCommand, this.jukeTaskManager);
                System.out.print(JukeOrchestrator.JUKEOUTPUT);

                // act on it, or any other future generated actions
                action.complete();

                System.out.print(JukeOrchestrator.SEPARATOR);
            } catch (JukeException ex) {
                new JukeExceptionAction(ex).complete();
                System.out.print(JukeOrchestrator.SEPARATOR);
            }
        } while (!(action instanceof JukeExitAction));
    }

    /**
     * Prints out the Introduction statments.
     */
    private void printIntroduction() {
        String builder = JukeOrchestrator.LOGO +
                JukeOrchestrator.SEPARATOR +
                JukeOrchestrator.INTRO +
                JukeOrchestrator.SEPARATOR;

        System.out.print(builder);
    }
}
