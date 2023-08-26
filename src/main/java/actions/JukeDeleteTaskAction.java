package main.java.actions;

import main.java.exceptions.storage.JukeStorageException;
import main.java.tasks.JukeTaskManager;
import main.java.exceptions.JukeException;
import main.java.tasks.JukeTask;

/**
 * Action that deletes a Task from the Task Manager.
 */
public class JukeDeleteTaskAction extends JukeAction {
    /** JukeTaskManager to manage all tasks. */
    private final JukeTaskManager taskManager;

    /** JukeTask to remove. */
    private final int task;

    /**
     * Constructor for JukeDeleteTaskAction.
     * @param taskManager JukeTaskManager instance
     * @param task Index of task to delete
     */
    public JukeDeleteTaskAction(JukeTaskManager taskManager, int task) {
        this.taskManager = taskManager;
        this.task = task;
    }

    @Override
    public void complete() throws JukeStorageException {
        JukeTask jt = this.taskManager.deleteTask(this.task);
        System.out.print("Task deleted: " + jt);
    }
}
