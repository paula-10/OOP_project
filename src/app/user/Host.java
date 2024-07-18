package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Episode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public final class Host extends User {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;

    /**
     * Instantiates a new Host.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Adds a new podcast.
     *
     * @param podcast the podcast
     * @return the string
     */
    public String addPodcast(final Podcast podcast) {
        if (podcasts.stream().anyMatch(p -> p.getName().equals(podcast.getName()))) {
            return getUsername() + " has another podcast with the same name.";
        }

        podcasts.add(podcast);
        return getUsername() + " has added new podcast successfully.";
    }

    /**
     * Removes a podcast.
     *
     * @param name the name
     * @return the string
     */
    public String removePodcast(final String name) {
        Optional<Podcast> podcastOpt = podcasts.stream()
                .filter(p -> p.matchesName(name)).findFirst();

        if (podcastOpt.isEmpty()) {
            return getUsername() + " doesn't have a podcast with the given name.";
        }

        Podcast podcast = podcastOpt.get();
        List<NormalUser> normalUsers = Admin.getNormalUsers();
        for (NormalUser normalUser : normalUsers) {
            for (Episode episode : podcast.getEpisodes()) {
                if (normalUser.getPlayerStats().getName().equals(episode.getName())) {
                    return getUsername() + " can't delete this podcast.";
                }
            }
        }

        podcasts.remove(podcast);
        return getUsername() + " deleted the podcast successfully.";
    }

    /**
     * Show podcasts array list.
     *
     * @return the array list
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }

        return podcastOutputs;
    }

    /**
     * Adds a new announcement.
     *
     * @param announcement the announcement
     * @return the string
     */
    public String addAnnouncement(final Announcement announcement) {
        if (announcements.stream().anyMatch(a -> a.getName().equals(announcement.getName()))) {
            return getUsername() + " has already added an announcement with this name.";
        }

        announcements.add(announcement);
        return getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes an announcement.
     *
     * @param name the name
     * @return the string
     */
    public String removeAnnouncement(final String name) {
        if (announcements.stream().noneMatch(announcement -> announcement.getName().equals(name))) {
            return getUsername() + " has no announcement with the given name.";
        }

        announcements.removeIf(announcement -> announcement.getName().equals(name));
        return getUsername() + " has successfully deleted the announcement.";
    }
}
