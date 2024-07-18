package app.user;

import app.search.UserEntry;
import lombok.Getter;
import lombok.Setter;


/**
 * The type User.
 */

@Setter
@Getter
public abstract class User extends UserEntry {
    protected String username;
    protected int age;
    protected String city;


    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
    }
}
