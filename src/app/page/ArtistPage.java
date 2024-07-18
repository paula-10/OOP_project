package app.page;

import app.user.Artist;

public class ArtistPage extends Page {


    public ArtistPage(final Artist artist) {
        super(artist);
    }

    @Override
    public String print() {
        Artist artist = (Artist) user;

        StringBuilder content = new StringBuilder();
        content.append("Albums:\n\t[");
        for (int i = 0; i < artist.getAlbums().size(); i++) {
            content.append(artist.getAlbums().get(i).getName());
            if (i != artist.getAlbums().size() - 1) {
                content.append(", ");
            }
        }

        content.append("]\n\nMerch:\n\t[");
        for (int i = 0; i < artist.getMerches().size(); i++) {
            content.append(artist.getMerches().get(i).getName());
            content.append(" - ");
            content.append(artist.getMerches().get(i).getPrice());
            content.append(":\n\t");
            content.append(artist.getMerches().get(i).getDescription());
            if (i != artist.getMerches().size() - 1) {
                content.append(", ");
            }
        }

        content.append("]\n\nEvents:\n\t[");
        for (int i = 0; i < artist.getEvents().size(); i++) {
            content.append(artist.getEvents().get(i).getName());
            content.append(" - ");
            content.append(artist.getEvents().get(i).getDate());
            content.append(":\n\t");
            content.append(artist.getEvents().get(i).getDescription());
            if (i != artist.getEvents().size() - 1) {
                content.append(", ");
            }
        }
        content.append("]");

        return content.toString();
    }
}
