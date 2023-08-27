package juke.parsers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import juke.core.JukeObject;
import juke.exceptions.JukeParseException;

/**
 * Parses Datetime Strings into the relevant DateTime objects.
 */
public class DateTimeParser extends JukeObject {
    /** Regex for recognising DateTime inputs. */
    private static final String DATETIME_REGEX = "^(0?[1-9]|[12][0-9]|3[01])(\\/|-)(0?[1-9]|1[0-2])(\\/|-)\\d{4} "
            + "([01]?[0-9]|2[0-3])?(-|:)?[0-5][0-9]$";

    /** Regex for recognising Date inputs. */
    private static final String DATE_REGEX = "^(0?[1-9]|[12][0-9]|3[01])(\\/|-)(0?[1-9]|1[0-2])(\\/|-)\\d{4}";

    /**
     * Checks if the input date is of the correct Date format.
     * @param date Input date
     * @return true if the String is a valid Date format, else false
     */
    public static boolean isValidDate(String date) {
        return Pattern.matches(DateTimeParser.DATE_REGEX, date);
    }

    /**
     * Checks if the input date is of the correct DateTime format.
     * @param datetime Input datetime
     * @return true if the String is a valid DateTime format, else false
     */
    public static boolean isValidDateTime(String datetime) {
        return Pattern.matches(DateTimeParser.DATETIME_REGEX, datetime);
    }

    /**
     * Parses the input String into a LocalDateTime object, provided that the
     * string meets the requirement of either being a valid Date or DateTime.
     * @param datetime Input date/datetime string
     * @return
     */
    public static LocalDateTime parse(String datetime) {
        // strip the datetime first to avoid any errors
        datetime = datetime.strip();

        if (isValidDateTime(datetime)) {
            String[] dateTimeSplit = datetime.split(" ");
            String[] date = dateTimeSplit[0].split("\\/|-");
            String[] time = Pattern.matches("\\d{4}", dateTimeSplit[1])
                    ? new String[]{dateTimeSplit[1].substring(0, 2), dateTimeSplit[1].substring(2, 4)}
                    : dateTimeSplit[1].split("-|:");
            return LocalDateTime.parse(date[2] + "-" + date[1] + "-" + date[0] + "T" + time[0] + ":" + time[1] + ":00");
        } else if (isValidDate(datetime)) {
            String[] date = datetime.split("\\/|-");
            return LocalDateTime.parse(date[2] + "-" + date[1] + "-" + date[0] + "T00:00:00");
        }

        throw new JukeParseException("Oh no! I cannot understand the date format you have given me!\nEnsure that "
                                             + "datetimes are given as such: DD(/-)MM(/-)YYYY[ HH(:-)MM],\nwhere "
                                             + "(...) represents the set of acceptable symbols and [...] represents\n"
                                             + "optional arguments.");
    }

    /**
     * Directly parse stored datafile representation of {@code LocalDateTime}
     * objects into actual {@code LocalDateTime} objects.
     * @param dateTimeString Stored string
     * @return {@code LocalDateTime} object
     */
    public static LocalDateTime fromDateTimeString(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (DateTimeParseException ex) {
            throw new JukeParseException("Oh no! I cannot understand the date format you have given me!\nEnsure that "
                                                 + "datetimes are given as such: YYYY-MM-DDTHH:MM");
        }
    }
}
