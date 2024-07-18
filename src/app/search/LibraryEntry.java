package app.search;

import lombok.Getter;

/**
 * The type Library entry.
 */
@Getter
public abstract class LibraryEntry extends SearchEntry {

    /**
     * Initialises the library entry parameters.
     *
     * @param name the name
     */
    public LibraryEntry(final String name) {
        super(name);
    }
}
