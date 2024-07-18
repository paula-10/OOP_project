package app.user;

import app.exceptions.InvalidEventException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
public class Event {
    private String name;
    private String description;
    private String date;

    /**
     * Instantiates a new Event.
     *
     * @param name        the name
     * @param description the description
     * @param date        the date
     */
    public Event(final String name, final String description,
                 final String date) throws InvalidEventException {
        LocalDate localDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            localDate = LocalDate.parse(date, formatter);
            if (localDate.getYear() < 1900 || localDate.getYear() > 2023) {
                throw new InvalidEventException();
            }
        } catch (DateTimeParseException e) {
            throw new InvalidEventException();
        }

        this.name = name;
        this.description = description;
        this.date = date;
    }
}
