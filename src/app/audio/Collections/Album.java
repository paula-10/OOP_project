package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Album extends AudioCollection {
    private Integer releaseYear;
    private String description;

    private ArrayList<Song> songs;

    public Album(final String name, final String owner, final Integer releaseYear,
                 final String description, final List<Song> songs) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.songs.addAll(songs);
        this.releaseYear = releaseYear;
        this.description = description;
    }


    public int getLikes() {
        return songs.stream().mapToInt(Song::getLikes).sum();
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean matchesDescription(final String description) {
        return this.description.equals(description);
    }

}
