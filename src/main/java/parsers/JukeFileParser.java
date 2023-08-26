package main.java.parsers;

import main.java.exceptions.JukeParseException;
import main.java.primitivies.JukeObject;
import main.java.tasks.*;

/**
 * Parses the datafile back into JukeTask objects.
 * <p>
 * This parser may not be instantiated. All methods are exposed via
 * static methods.
 */
public abstract class JukeFileParser extends JukeObject {
    /** Regex String used to parse the datafile lines. */
    private static final String REGEX = "\\|";

    /**
     * Parses a single task into a {@code JukeTask} object.
     * @param task Single task to parse
     * @return {@code JukeTask} object
     * @throws {@code JukeParseException} if there are errors with parsing the datafile
     */
    public static JukeTask parseTask(String task) {
        String[] data = task.split(REGEX);
        
        if (data.length == 0) {
            throw new JukeParseException("Oh no! Data \"" + task + "\" cannot be parsed!");
        }

        switch (data[0]) {
            case "T":
                return new JukeTodo(data[2], data[1].equals("T"));
            case "D":
                return new JukeDeadline(data[2], JukeDateTimeParser.fromParsedString(data[3]), data[1].equals("T"));
            case "E":
                return new JukeEvent(data[2], JukeDateTimeParser.fromParsedString(data[3]),
                                     JukeDateTimeParser.fromParsedString(data[4]), data[1].equals("T"));
            default:
                throw new JukeParseException("Oh no! Data \"" + task + "\" cannot be parsed!");
        }
    }
}
