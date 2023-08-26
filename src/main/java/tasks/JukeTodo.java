package main.java.tasks;

/**
 * Represents a Todo task.
 */
public class JukeTodo extends JukeTask {
    /** String which represents the Task Identifier. */
    private static final String TASK_DESCRIPTOR = "[T] ";

    /**
     * Constructor to create a Todo Task.
     * @param task Task description
     */
    public JukeTodo(String task) {
        super(task);
    }

    /**
     * Constructor to create a Todo Task.
     * @param task Task description
     * @param completion Status of completion of the task
     */
    public JukeTodo(String task, boolean completion) {
        super(task);
        if (completion) {
            this.markAsComplete();
        }
    }

    /**
     * Returns the string which represents this object when it is saved into the datafile.
     * @return Datafile representation of this object
     */
    @Override
    public String save() {
        return "T" + super.save();
    }

    /**
     * String representation of this {@code JukeTodo} object
     * @return String representation
     */
    @Override
    public String toString() {
        return JukeTodo.TASK_DESCRIPTOR + super.toString();
    }
}
