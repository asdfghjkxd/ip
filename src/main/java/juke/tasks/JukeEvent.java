package juke.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import juke.commons.enums.SortOrderEnum;
import juke.commons.enums.SortTypeEnum;
import juke.commons.interfaces.TaskSortable;
import juke.exceptions.JukeStateException;
import juke.exceptions.arguments.JukeIllegalArgumentException;

/**
 * Represents an Event task. Event tasks contain both a startTime and endTime
 * deadline, which are represented by {@code LocalDateTime} objects.
 */
public class JukeEvent extends JukeTask implements TaskSortable<JukeTask> {
    /** String which represents the Task Identifier. */
    private static final String TASK_DESCRIPTOR = "[E] ";

    /** Start Time. */
    private final LocalDateTime startTime;

    /** End Time. */
    private final LocalDateTime endTime;

    /**
     * Creates an instance of {@code JukeEvent}.
     *
     * @param taskName Task description
     * @param startTime Start date/time
     * @param endTime End date/time
     */
    public JukeEvent(String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        super(taskName);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Creates an instance of {@code JukeEvent}.
     *
     * @param taskName Task description
     * @param startTime Start date/time
     * @param endTime End date/time
     * @param isCompleted Status of completion of the task
     * @throws JukeStateException if the task is already completed
     */
    public JukeEvent(String taskName, LocalDateTime startTime, LocalDateTime endTime, boolean isCompleted) {
        this(taskName, startTime, endTime);

        if (isCompleted) {
            // the event should have a start date that is before the end date
            assert startTime.isBefore(endTime);
            this.setAsComplete();
        }
    }

    /**
     * Returns the String which represents this object when it is saved into the datafile.
     *
     * @return Datafile representation of this object
     */
    @Override
    public String save() {
        return "E" + super.save() + "|" + startTime + "|" + endTime;
    }

    /**
     * Compares the input {@code LocalDateTime} object with the end time of this {@code JukeEvent} object. This
     * method is mainly used for sorting, and should not be invoked directly by the user.
     *
     * @param dateTime input {@code LocalDateTime} object
     * @return -1 if the input {@code LocalDateTime} object is before the end time of this {@code JukeEvent} object,
     *     0 if they are the same, and 1 if the input {@code LocalDateTime} object is after the end time of this
     *     {@code JukeEvent} object
     */
    public int compareEndTime(LocalDateTime dateTime) {
        return dateTime.compareTo(this.endTime);
    }

    /**
     * Compares this {@code JukeEvent} object with the input {@code JukeTask} object for order.
     * This method should not be directly invoked by the user as it is mainly used for sorting.
     *
     * @param task the {@code JukeTask} object to be compared with
     * @param sortOrder the order to sort the tasks by
     * @param sortType the type of sort to perform on the tasks
     * @return -1 if this {@code JukeEvent} object is before the input {@code JukeTask} object, 0 if they
     *     are the same, and 1 if this {@code JukeEvent} object is after the input {@code JukeTask} object
     */
    @Override
    public int sortBy(JukeTask task, SortOrderEnum sortOrder, SortTypeEnum sortType) {
        switch (sortType) {
        case DESCRIPTION:
            // reuses the superclass's description comparator method
            return super.sortBy(task, sortOrder, sortType);
        case DEADLINE:
        case END_DATE:
            return this.compareDeadlineOrEndDate(task, sortOrder);
        case START_DATE:
            return this.compareStartDate(task, sortOrder);
        default:
            throw new JukeIllegalArgumentException("Oh no! I cannot sort the list on that field!");
        }
    }

    /**
     * Compares this {@code JukeEvent}'s deadline or end date with respect to the input {@code JukeTask} object.
     * This method should not be directly invoked by the user as it is mainly used for sorting.
     *
     * @param task the {@code JukeTask} object to be compared with
     * @param sortOrder the order to sort the tasks by
     * @return -1 if this {@code JukeEvent}'s end date is before the deadline/end date of the input
     *     {@code JukeTask} object, 0 if they are the same, and 1 if this {@code JukeEvent}'s end
     *     date is after the deadline/end date of the input {@code JukeTask} object
     */
    private int compareDeadlineOrEndDate(JukeTask task, SortOrderEnum sortOrder) {
        if (task instanceof JukeEvent) {
            JukeEvent event = (JukeEvent) task;
            return this.endTime.compareTo(event.endTime) * sortOrder.getMultiplier();
        } else if (task instanceof JukeDeadline) {
            JukeDeadline deadline = (JukeDeadline) task;
            int otherDeadlineComparedToSelf = deadline.compareDeadline(this.endTime);
            return otherDeadlineComparedToSelf * sortOrder.getMultiplier();
        } else if (task instanceof JukeTodo) {
            // since todos have an infinitely early deadline, they are always considered to be before events
            // and so events are always after todos, subjected to the constraints of the sort order
            return sortOrder.getMultiplier();
        } else {
            // should not reach here, unless there are other unknown subclasses of JukeTask
            throw new JukeIllegalArgumentException("Oh no! I cannot sort the list with an unknown task within it!");
        }
    }

    /**
     * Compares the start date between this {@code JukeEvent} object with another input {@code JukeTask} object.
     * This method should not be directly invoked by the user as it is mainly used for sorting.
     *
     * @param task the {@code JukeTask} object to be compared with
     * @param sortOrder the order to sort the tasks by
     * @return -1 if this {@code JukeEvent} object's start date is before the start date of the input {@code JukeTask}
     *     object, 0 if they are the same, and 1 if this {@code JukeEvent} object is after the start date of the input
     *     {@code JukeTask} object
     */
    private int compareStartDate(JukeTask task, SortOrderEnum sortOrder) {
        if (task instanceof JukeEvent) {
            JukeEvent event = (JukeEvent) task;
            return this.startTime.compareTo(event.startTime) * sortOrder.getMultiplier();
        } else if (task instanceof JukeDeadline || task instanceof JukeTodo) {
            // since todos have an infinitely early deadline, they are always considered to be before events
            // and so events are always after todos, subjected to the constraints of the sort order
            return sortOrder.getMultiplier();
        } else {
            // should not reach here, unless there are other unknown subclasses of JukeTask
            throw new JukeIllegalArgumentException("Oh no! I cannot sort the list with an unknown task within it!");
        }
    }

    /**
     * Returns String representation of this {@code JukeEvent} object.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return JukeEvent.TASK_DESCRIPTOR
                + super.toString()
                + " (from " + startTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HHmm"))
                + " hrs to " + endTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HHmm")) + " hrs)";
    }
}
