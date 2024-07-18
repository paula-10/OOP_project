package app.user;

import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.exceptions.UserOfflineException;
import app.page.*;
import app.player.Player;
import app.player.PlayerStats;
import app.search.LibraryEntry;
import app.search.SearchEntry;
import app.search.UserEntry;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class NormalUser extends User {
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    private Enums.ConnectionStatus status;
    @Getter
    private Page currentPage;

    /**
     * Instantiates a new NormalUser.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        status = Enums.ConnectionStatus.ONLINE;

        currentPage = new HomePage(this);
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters,
                                    final String type) throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<SearchEntry> searchEntries = searchBar.search(filters, type);

        for (SearchEntry searchEntry : searchEntries) {
            results.add(searchEntry.getName());
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        SearchEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }

        if (selected instanceof LibraryEntry) {
            return "Successfully selected %s.".formatted(selected.getName());
        } else if (selected instanceof UserEntry) {
            if (selected instanceof Artist) {
                currentPage = new ArtistPage((Artist) selected);
            } else if (selected instanceof Host) {
                currentPage = new HostPage((Host) selected);
            }
            return "Successfully selected %s's page.".formatted(selected.getName());
        }

        return "";
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("album")
                && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp)
            throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        SearchEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (status == Enums.ConnectionStatus.ONLINE) {
            player.simulatePlayer(time);
        }
    }

    /**
     * Changes current page.
     *
     * @param nextPage next page
     * @return information from the current page
     */
    public String changePage(final String nextPage) {
        if (nextPage.equals("Home")) {
            currentPage = new HomePage(this);
        } else if (nextPage.equals("LikedContent")) {
            currentPage = new LikedContentPage(this);
        } else {
            return username + " is trying to access a non-existent page.";
        }

        return username + " accessed " + nextPage + " successfully.";
    }

    /**
     * Prints current page.
     *
     * @return information from the current page
     */
    public String printCurrentPage() throws UserOfflineException {
        if (status == Enums.ConnectionStatus.OFFLINE) {
            throw new UserOfflineException(username);
        }

        return currentPage.print();
    }

    public void switchConnectionStatus() {
        if (status == Enums.ConnectionStatus.ONLINE) {
            status = Enums.ConnectionStatus.OFFLINE;
        } else {
            status = Enums.ConnectionStatus.ONLINE;
        }
    }
}
