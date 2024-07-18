package fileio.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UserInput {
    private String username;
    private int age;
    private String city;
    private String type;

    public UserInput() {
    }

    public UserInput(final String username, final int age, final String city, final String type) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserInput{"
                + "username='" + username + '\''
                + ", age=" + age
                + ", city='" + city + '\''
                + ", type='" + type + '\''
                + '}';
    }
}
