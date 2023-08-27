package juke.exceptions.storage;

import juke.exceptions.JukeException;

/**
 * Represents a generic error with the storing, retrieving or modifying of data
 * in the datafile.
 */
public class JukeStorageException extends JukeException {
    /**
     * Constructor to create a JukeStorageException.
     * @param err Error description
     */
    public JukeStorageException(String err) {
        super(err);
    }
}
