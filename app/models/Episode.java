/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import controllers.Security;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
public class Episode extends Model {

    @Required
    public int seasonNumber;
    @Required
    public int episodeNumber;
    @ManyToOne
    @Required
    public Series series;
    @ManyToMany(mappedBy = "episodes")
    public List<BetaUser> users;

    @Override
    public String toString() {
        return this.series.name + " Season " + this.seasonNumber + " Episode " + this.episodeNumber;
    }

    public static List<Episode> getEpisodes(String username, String serviceSeriesId) throws Exception {
        models.Series series = models.Series.find("serviceSeriesId", serviceSeriesId).first();
        if (series == null) {
            throw new Exception("Series not found");
        }

        BetaUser user = BetaUser.find("username", username).first();

        return JPA.em().createQuery("FROM Episode e WHERE e.series = ?2 AND ?1 MEMBER OF e.users")
                .setParameter(1, user)
                .setParameter(2, series)
                .getResultList();
    }
}
