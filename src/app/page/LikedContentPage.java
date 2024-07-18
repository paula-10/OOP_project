package app.page;

import app.user.NormalUser;

public class LikedContentPage extends Page {
    public LikedContentPage(final NormalUser normalUser) {
        super(normalUser);
    }

    @Override
    public String print() {
        NormalUser normalUser = (NormalUser) user;

        StringBuilder content = new StringBuilder();
        content.append("Liked songs:\n\t[");
        for (int i = 0; i < normalUser.getLikedSongs().size(); i++) {
            content.append(normalUser.getLikedSongs().get(i).getName());
            content.append(" - ");
            content.append(normalUser.getLikedSongs().get(i).getArtist());
            if (i != normalUser.getLikedSongs().size() - 1) {
                content.append(", ");
            }
        }

        content.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < normalUser.getFollowedPlaylists().size(); i++) {
            content.append(normalUser.getFollowedPlaylists().get(i).getName());
            content.append(" - ");
            content.append(normalUser.getFollowedPlaylists().get(i).getOwner());
            if (i != normalUser.getFollowedPlaylists().size() - 1) {
                content.append(", ");
            }
        }
        content.append("]");

        return content.toString();
    }
}
