package app.page;

import app.audio.Files.Episode;
import app.user.Host;

import java.util.List;

public class HostPage extends Page {
    public HostPage(final Host host) {
        super(host);
    }

    @Override
    public String print() {
        Host host = (Host) user;
        StringBuilder content = new StringBuilder();
        content.append("Podcasts:\n\t[");
        for (int i = 0; i < host.getPodcasts().size(); i++) {
            content.append(host.getPodcasts().get(i).getName());
            content.append(":\n\t[");

            List<Episode> episodes = host.getPodcasts().get(i).getEpisodes();
            for (int j = 0; j < episodes.size(); j++) {
                content.append(episodes.get(j).getName());
                content.append(" - ");
                content.append(episodes.get(j).getDescription());
                if (j != episodes.size() - 1) {
                    content.append(", ");
                }
            }
            content.append("]\n");
            if (i != host.getPodcasts().size() - 1) {
                content.append(", ");
            }
        }

        content.append("]\n\nAnnouncements:\n\t[");
        for (int i = 0; i < host.getAnnouncements().size(); i++) {
            content.append(host.getAnnouncements().get(i).getName());
            content.append(":\n\t");
            content.append(host.getAnnouncements().get(i).getDescription());
            content.append("\n");
            if (i != host.getAnnouncements().size() - 1) {
                content.append(", ");
            }
        }
        content.append("]");

        return content.toString();
    }
}
