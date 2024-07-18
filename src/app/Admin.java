package app;

import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.exceptions.InvalidEventException;
import app.exceptions.InvalidMerchException;
import app.user.*;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.*;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();

    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new NormalUser(userInput.getUsername(), userInput.getAge(),
                    userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            if (user instanceof NormalUser) {
                playlists.addAll(((NormalUser) user).getPlaylists());
            }
        }

        return playlists;
    }

    public static List<Host> getHosts() {
        List<Host> hosts = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Host host) {
                hosts.add(host);
            }
        }

        return hosts;
    }

    public static List<NormalUser> getNormalUsers() {
        List<NormalUser> normalUsers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof NormalUser normalUser) {
                normalUsers.add(normalUser);
            }
        }

        return normalUsers;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Gets artists.
     *
     * @return the artists
     */
    public static List<Artist> getArtists() {
        List<Artist> artists = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Artist artist) {
                artists.add(artist);
            }
        }

        return artists;
    }

    public static List<Album> getAlbums() {
        List<Artist> artists = getArtists();

        List<Album> albums = new ArrayList<>();
        for (Artist artist : artists) {
            albums.addAll(artist.getAlbums());
        }

        return albums;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user instanceof NormalUser normalUser) {
                normalUser.simulateTime(elapsed);

            }
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparingInt(Album::getLikes)
                .reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));

        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= LIMIT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * Gets top 5 artists.
     *
     * @return the top 5 artists
     */
    public static List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(getArtists());
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes).reversed());

        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }
            topArtists.add(artist.getName());
            count++;
        }
        return topArtists;
    }

    /**
     * Add a new user.
     *
     * @param userName the username
     * @param age      the age
     * @param city     the city
     * @param type     the user type
     * @return the string
     */
    public static String addUser(final String userName, final Integer age,
                                 final String city, final String type) {
        if (users.stream().anyMatch(user -> user.getUsername().equals(userName))) {
            return "The username " + userName + " is already taken.";
        }

        User user = UserFactory.createUser(userName, age, city, type);
        users.add(user);

        return "The username " + userName + " has been added successfully.";
    }

    /**
     * Deletes a user.
     *
     * @param userName the username
     * @return the string
     */
    public static String deleteUser(final String userName) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (user instanceof Host host) {
            for (NormalUser normalUser : getNormalUsers()) {
                if (normalUser.getCurrentPage().getUser() == user) {
                    return userName + " can't be deleted.";
                }
                for (Podcast podcast : host.getPodcasts()) {
                    for (Episode episode : podcast.getEpisodes()) {
                        if (normalUser.getPlayerStats().getName().equals(episode.getName())) {
                            return userName + " can't be deleted.";
                        }
                    }
                }
            }
        } else if (user instanceof Artist artist) {
            for (NormalUser normalUser : getNormalUsers()) {
                if (normalUser.getCurrentPage().getUser() == user) {
                    return userName + " can't be deleted.";
                }
                for (Album album : artist.getAlbums()) {
                    for (Song song : album.getSongs()) {
                        if (normalUser.getPlayerStats().getName().equals(song.getName())) {
                            return userName + " can't be deleted.";
                        }
                    }
                }
            }
            for (NormalUser normalUser : getNormalUsers()) {
                normalUser.getLikedSongs().removeAll(artist.getSongs());
            }
            songs.removeAll(artist.getSongs());
        } else if (user instanceof NormalUser normalUser) {
            for (NormalUser u : getNormalUsers()) {
                if (u == normalUser) {
                    continue;
                }

                for (Playlist playlist : normalUser.getPlaylists()) {
                    if (u.getPlayer().getSource() != null && u.getPlayer()
                            .getSource().getAudioCollection() != null) {
                        if (u.getPlayer().getSource().getAudioCollection()
                                .matchesName(playlist.getName())) {
                            return userName + " can't be deleted.";
                        }
                    }
                }

            }
            for (NormalUser u : getNormalUsers()) {
                for (Playlist playlist : normalUser.getPlaylists()) {
                    u.getFollowedPlaylists().remove(playlist);
                }
            }
            for (Playlist playlist : normalUser.getFollowedPlaylists()) {
                playlist.decreaseFollowers();
            }
            for (Song song : normalUser.getLikedSongs()) {
                song.dislike();
            }
        }


        users.removeIf(u -> u.getUsername().equals(userName));
        return userName + " was successfully deleted.";
    }

    /**
     * Switches the connection status of a user.
     *
     * @return the string
     */
    public static String switchConnectionStatus(final String userName) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (user instanceof Host || user instanceof Artist) {
            return userName + " is not a normal user.";
        }


        ((NormalUser) user).switchConnectionStatus();
        return userName + " has changed status successfully.";
    }

    /**
     * Gets all users.
     *
     * @return all users
     */
    public static List<String> getAllUsers() {
        List<String> alLUsers = new ArrayList<>();

        for (User user : users) {
            if (!(user instanceof Artist) && !(user instanceof Host)) {
                alLUsers.add(user.getUsername());
            }
        }

        for (User user : users) {
            if (user instanceof Artist) {
                alLUsers.add(user.getUsername());
            }
        }

        for (User user : users) {
            if (user instanceof Host) {
                alLUsers.add(user.getUsername());
            }
        }

        return alLUsers;
    }

    /**
     * Gets the online users.
     *
     * @return all online users
     */
    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();

        for (User user : users) {
            if (user instanceof NormalUser && ((NormalUser) user).getStatus()
                    == Enums.ConnectionStatus.ONLINE) {
                onlineUsers.add(user.getUsername());
            }
        }

        return onlineUsers;
    }


    /**
     * Adds a new album.
     *
     * @param userName    the username
     * @param name        the name
     * @param releaseYear the release year
     * @param description the description
     * @param songInputs  the list of song inputs
     * @return the string
     */
    public static String addAlbum(final String userName, final String name,
                                  final Integer releaseYear, final String description,
                                  final List<SongInput> songInputs) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Artist artist)) {
            return userName + " is not an artist.";
        }

        if (artist.getAlbums().stream().anyMatch(album -> album.getName().equals(name))) {
            return userName + " has another album with the same name.";
        }

        List<Song> albumSongs = new ArrayList<>();
        for (SongInput songInput : songInputs) {
            albumSongs.add(new Song(songInput));
        }
        int unique = albumSongs.stream().map(Song::getName).distinct().toList().size();
        if (unique != albumSongs.size()) {
            return userName + " has the same song at least twice in this album.";
        }

        Album album = new Album(name, userName, releaseYear, description, albumSongs);
        songs.addAll(albumSongs);
        return artist.addAlbum(album);
    }

    /**
     * Removes an album.
     *
     * @param userName the username
     * @param name     the name
     * @return the string
     */
    public static String removeAlbum(final String userName, final String name) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Artist artist)) {
            return userName + " is not an artist.";
        }

        return artist.removeAlbum(name);
    }

    /**
     * Gets the albums of an artist.
     *
     * @param userName the username
     * @return a list with the albums of the artist
     */
    public static List<AlbumOutput> showAlbums(final String userName) {
        Artist artist = (Artist) getUser(userName);

        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : artist.getAlbums()) {
            albumOutputs.add(new AlbumOutput(album));
        }

        return albumOutputs;
    }

    /**
     * Adds a new event.
     *
     * @param userName    the username
     * @param name        the name
     * @param description the description
     * @param date        the date
     * @return the string
     */
    public static String addEvent(final String userName, final String name,
                                  final String description, final String date) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Artist artist)) {
            return userName + " is not an artist.";
        }

        Event event;
        try {
            event = new Event(name, description, date);
        } catch (InvalidEventException e) {
            return "Event for " + userName + " does not have a valid date.";
        }

        return artist.addEvent(event);
    }

    /**
     * Removes an event.
     *
     * @param userName the username
     * @param name     the name
     * @return the string
     */
    public static String removeEvent(final String userName, final String name) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Artist artist)) {
            return userName + " is not an artist.";
        }

        return artist.removeEvent(name);
    }

    /**
     * Adds a new merch.
     *
     * @param userName    the username
     * @param name        the name
     * @param description the description
     * @param price       the price
     * @return the string
     */
    public static String addMerch(final String userName, final String name,
                                  final String description, final Integer price) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Artist artist)) {
            return userName + " is not an artist.";
        }

        Merch merch;
        try {
            merch = new Merch(name, description, price);
        } catch (InvalidMerchException e) {
            return "Price for merchandise can not be negative.";
        }

        return artist.addMerch(merch);
    }

    /**
     * Adds a new podcast.
     *
     * @param userName      the username
     * @param name          the name
     * @param episodeInputs the list of episode inputs
     * @return the string
     */
    public static String addPodcast(final String userName, final String name,
                                    final List<EpisodeInput> episodeInputs) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Host host)) {
            return userName + " is not a host.";
        }

        List<Episode> episodes = new ArrayList<>();
        Set<String> episodeNames = new HashSet<>();
        for (EpisodeInput episodeInput : episodeInputs) {
            if (!episodeNames.add(episodeInput.getName())) {
                return userName + " has the same episode in this podcast.";
            }
            episodes.add(new Episode(episodeInput));
        }

        Podcast podcast = new Podcast(name, userName, episodes);
        podcasts.add(podcast);
        return host.addPodcast(podcast);
    }

    /**
     * Removes a podcast.
     *
     * @param userName the username
     * @param name     the name
     * @return the string
     */
    public static String removePodcast(final String userName, final String name) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Host host)) {
            return userName + " is not a host.";
        }

        return host.removePodcast(name);
    }

    /**
     * Adds a new announcement.
     *
     * @param userName    the username
     * @param name        the name
     * @param description the description
     * @return the string
     */
    public static String addAnnouncement(final String userName, final String name,
                                         final String description) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Host host)) {
            return userName + " is not a host.";
        }

        Announcement announcement = new Announcement(name, description);
        return host.addAnnouncement(announcement);
    }

    /**
     * Removes an announcement.
     *
     * @param userName the username
     * @param name     the name
     * @return the string
     */
    public static String removeAnnouncement(final String userName, final String name) {
        User user = getUser(userName);

        if (user == null) {
            return "The username " + userName + " doesn't exist.";
        }

        if (!(user instanceof Host host)) {
            return userName + " is not a host.";
        }

        return host.removeAnnouncement(name);
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
