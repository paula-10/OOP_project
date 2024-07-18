package app.exceptions;

public class UserOfflineException extends Exception {
    public UserOfflineException(final String userName) {
        super("User " + userName + " is offline!");
    }
}
