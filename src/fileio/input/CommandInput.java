package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // song / playlist / podcast
    private FiltersInput filters; // pentru search
    private Integer itemNumber; // pentru select
    private Integer repeatMode; // pentru repeat
    private Integer playlistId; // pentru add/remove song
    private String playlistName; // pentru create playlist
    private Integer seed; // pentru shuffle
    private Integer age; // pentru add user
    private String city; // pentru add user
    private String name; // pentru add album
    private Integer releaseYear; // pentru add album
    private String description; // pentru add album
    private ArrayList<SongInput> songs; // pentru add album
    private String date; // pentru add event
    private Integer price; // pentru add merch
    private ArrayList<EpisodeInput> episodes; // pentru add podcast
    private String nextPage; // pentru change page

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + ", age=" + age
                + ", city='" + city + '\''
                + ", name='" + name + '\''
                + ", releaseYear=" + releaseYear
                + ", description='" + description + '\''
                + ", songs=" + songs
                + ", date='" + date + '\''
                + ", price=" + price
                + ", episodes=" + episodes
                + ", nextPage='" + nextPage + '\''
                + '}';
    }
}
