package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.*;

@Getter
public final class Artist extends User {
    private List<Album> albums;
    private List<Event> events;
    private List<Merch> merches;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merches = new ArrayList<>();
    }

    /**
     * Adds an album.
     *
     * @param album the album
     * @return the string
     */
    public String addAlbum(final Album album) {
        albums.add(album);
        return getUsername() + " has added new album successfully.";
    }

    /**
     * Removes an album.
     *
     * @param name the name
     * @return the string
     */
    public String removeAlbum(final String name) {
        Optional<Album> albumOpt = albums.stream().filter(a -> a.matchesName(name)).findFirst();

        if (albumOpt.isEmpty()) {
            return getUsername() + " doesn't have an album with the given name.";
        }

        Album album = albumOpt.get();
        List<NormalUser> normalUsers = Admin.getNormalUsers();
        for (NormalUser normalUser : normalUsers) {
            if (normalUser.getPlayerStats().getName().equals(name)) {
                return getUsername() + " can't delete this album.";
            }

            for (Song song : album.getSongs()) {
                if (song.matchesName(normalUser.getPlayerStats().getName())) {
                    return getUsername() + " can't delete this album.";
                }
            }
        }

        albums.remove(album);
        return getUsername() + " deleted the album successfully.";
    }

    /**
     * Add event string.
     *
     * @param event the event
     * @return the string
     */
    public String addEvent(final Event event) {
        if (events.stream().anyMatch(e -> e.getName().equals(event.getName()))) {
            return getUsername() + " has another event with the same name.";
        }

        events.add(event);
        return getUsername() + " has added new event successfully.";
    }

    /**
     * Removes an event.
     *
     * @param name the name
     * @return the string
     */
    public String removeEvent(final String name) {
        Optional<Event> eventOpt = events.stream()
                .filter(e -> e.getName().equals(name)).findFirst();

        if (eventOpt.isEmpty()) {
            return getUsername() + " doesn't have an event with the given name.";
        }

        events.remove(eventOpt.get());
        return getUsername() + " deleted the event successfully.";
    }


    /**
     * Adds a new merch.
     *
     * @param merch the merch
     * @return the string
     */
    public String addMerch(final Merch merch) {
        if (merches.stream().anyMatch(m -> m.getName().equals(merch.getName()))) {
            return getUsername() + " has merchandise with the same name.";
        }

        merches.add(merch);
        return getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Gets all songs.
     *
     * @return all songs.
     */
    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();

        for (Album album : albums) {
            songs.addAll(album.getSongs());
        }

        return songs;
    }

    public int getLikes() {
        return albums.stream().mapToInt(Album::getLikes).sum();
    }
}
