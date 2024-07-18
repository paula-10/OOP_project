package app.searchBar;


import app.Admin;
import app.search.SearchEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;


/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<SearchEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private SearchEntry lastSelected;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<SearchEntry> search(final Filters filters, final String type) {
        List<SearchEntry> entries;

        switch (type) {
            case "song" -> {
                entries = new ArrayList<>(Admin.getSongs());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }
                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }
                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }
                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }
                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }
                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }
            }
            case "playlist" -> {
                entries = new ArrayList<>(Admin.getPlaylists());
                entries = filterByPlaylistVisibility(entries, user);
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }
                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }
            }
            case "podcast" -> {
                entries = new ArrayList<>(Admin.getPodcasts());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }
            }
            case "artist" -> {
                entries = new ArrayList<>(Admin.getArtists());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
            }
            case "album" -> {
                entries = new ArrayList<>(Admin.getAlbums());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }
                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getDescription());
                }
            }
            case "host" -> {
                entries = new ArrayList<>(Admin.getHosts());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
            }
            default -> entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    /**
     * Select search entry.
     *
     * @param itemNumber the item number
     * @return the search entry
     */
    public SearchEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected = this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }
}
