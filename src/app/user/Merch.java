package app.user;

import app.exceptions.InvalidMerchException;
import lombok.Getter;

@Getter
public class Merch {
    private String name;
    private String description;
    private Integer price;


    /**
     * Instantiates a new Event.
     *
     * @param name        the name
     * @param description the description
     * @param price       the price
     */
    public Merch(final String name, final String description,
                 final Integer price) throws InvalidMerchException {
        if (price < 0) {
            throw new InvalidMerchException();
        }

        this.description = description;
        this.price = price;
        this.name = name;
    }
}
