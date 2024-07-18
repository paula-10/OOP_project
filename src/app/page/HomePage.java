package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.NormalUser;

import java.util.Comparator;
import java.util.List;

public class HomePage extends Page {

    public HomePage(final NormalUser normalUser) {
        super(normalUser);
    }

    @Override
    public String print() {
        NormalUser normalUser = (NormalUser) user;

        List<String> topLikedSongs = normalUser.getLikedSongs().stream()
                .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                .limit(5).map(Song::getName).toList();
        List<String> topFollowedPlaylists = normalUser.getFollowedPlaylists()
                .stream().sorted(Comparator.comparingInt(Playlist::getFollowers).reversed())
                .limit(5).map(Playlist::getName).toList();

        StringBuilder content = new StringBuilder();
        content.append("Liked songs:\n\t[");
        for (int i = 0; i < topLikedSongs.size(); i++) {
            content.append(topLikedSongs.get(i));
            if (i != topLikedSongs.size() - 1) {
                content.append(", ");
            }

        }

        content.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < topFollowedPlaylists.size(); i++) {
            content.append(topFollowedPlaylists.get(i));
            if (i != topFollowedPlaylists.size() - 1) {
                content.append(", ");
            }
        }
        content.append("]");

        return content.toString();
    }
}
