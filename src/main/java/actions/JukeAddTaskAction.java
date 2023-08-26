package main.java.actions;

import main.java.exceptions.JukeStateException;
import main.java.exceptions.storage.JukeStorageException;
import main.java.exceptions.JukeException;
import main.java.tasks.JukeTask;
import main.java.tasks.JukeTaskManager;

/**
 * Action that adds a Task to the Task Manager.
 */
public class JukeAddTaskAction extends JukeAction {
    /** JukeTaskManager to manage all tasks. */
    private final JukeTaskManager taskManager;

    /** JukeTask to add. */
    private final JukeTask task;

    /**
     * Constructor for JukeAddTaskAction
     * @param taskManager JukeTaskManager
     * @param task JukeTask to add
     */
    public JukeAddTaskAction(JukeTaskManager taskManager, JukeTask task) {
        this.taskManager = taskManager;
        this.task = task;
    }

    /**
     * Necessary method that is invoked when the action is carried out.
     * @throws {@code JukeStorageException} If there is an issue with storing changes
     */
    @Override
    public void complete() throws JukeStorageException {
        this.taskManager.addTask(this.task);
        System.out.print("Task added: " + this.task);
    }
}
