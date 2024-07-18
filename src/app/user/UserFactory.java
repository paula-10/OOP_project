package app.user;

/**
 * Class with static methods used for constructing different types of users.
 */
public final class UserFactory {
    private UserFactory() {

    }

    /**
     * Creates a user based on the given type.
     *
     * @return the user
     */
    public static User createUser(final String userName, final Integer age,
                                  final String city, final String type) {
        return switch (type) {
            case "user" -> new NormalUser(userName, age, city);
            case "artist" -> new Artist(userName, age, city);
            case "host" -> new Host(userName, age, city);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
