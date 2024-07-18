package app.page;

import app.user.User;
import lombok.Getter;

/**
 * The type Page.
 */
public abstract class Page {
    @Getter
    protected User user;

    public Page(final User user) {
        this.user = user;
    }
    public abstract String print();
}
