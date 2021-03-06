/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import exceptions.SeriesNotFoundException;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.JPA;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})})
public class WWIUser extends Model {

    @Unique
    @Required
    public String email;
    @ManyToMany
    @JoinTable(name = "WWIUSER_SERIES", joinColumns = {
        @JoinColumn(name = "WWIUSERS_ID")
    }, inverseJoinColumns = {
        @JoinColumn(name = "SERIES_ID")
    }, uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "WWIUSERS_ID",
            "SERIES_ID"
        })
    })
    public List<Series> series;
    @ManyToMany
    @JoinTable(name = "WWIUSER_EPISODE", joinColumns = {
        @JoinColumn(name = "WWIUSERS_ID")
    }, inverseJoinColumns = {
        @JoinColumn(name = "EPISODES_ID")
    }, uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "WWIUSERS_ID",
            "EPISODES_ID"
        })
    })
    public List<Episode> episodes;
    
    @OneToOne(orphanRemoval=true)
    public WWIUserPassword password;

    public WWIUser() {
    }

    public WWIUser(String email) {
        this.email = email;
    }

    /*
     * Track a Episode
     * returns true if the episode is tracked, returns false if it is untracked
     * 
     * TODO: Check if the WWIUser has added this series to "his series", if not, add it
     */
    public static boolean trackEpisode(long userid, String seriesId, int seasonNumber, int episodeNumber) throws SeriesNotFoundException {

        WWIUser user = WWIUser.findById(userid);

        Series series = models.Series.find("serviceSeriesId", seriesId).first();

        if (series == null) {
            throw new SeriesNotFoundException();
        }

        Episode episode = Episode.find("episodeNumber = ? and seasonNumber = ? and series_id = ?", episodeNumber, seasonNumber, series.id).first();

        if (episode == null) {
            episode = new Episode();
            episode.episodeNumber = episodeNumber;
            episode.seasonNumber = seasonNumber;
            episode.series = series;
            episode.save();

            WWIUserEpisode userEpisode = new WWIUserEpisode();
            userEpisode.user = user;
            userEpisode.episode = episode;
            
            userEpisode.save();
            
            return true;
        }

        if (user.episodes.contains(episode)) {
            JPA.em().createQuery("DELETE FROM WWIUSER_EPISODE WHERE user = ? AND episode = ?").setParameter(1, user).setParameter(2, episode).executeUpdate();

            return false;

        } else {
            WWIUserEpisode userEpisode = new WWIUserEpisode();
            userEpisode.user = user;
            userEpisode.episode = episode;
            
            userEpisode.save();
            
            return true;
        }

    }

    /*
     * returns the episodes of a series (series that are taken track of)
     */
    public static List<Episode> getEpisodes(long userid, String serviceSeriesId) throws SeriesNotFoundException {
        models.Series series = models.Series.find("serviceSeriesId", serviceSeriesId).first();
        if (series == null) {
            throw new SeriesNotFoundException();
        }

        WWIUser user = WWIUser.find("id", userid).first();

        return JPA.em().createQuery("FROM Episode e WHERE e.series = ?2 AND ?1 MEMBER OF e.users").setParameter(1, user).setParameter(2, series).getResultList();
    }
}
