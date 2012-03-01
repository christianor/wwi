/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import play.data.validation.Required;
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
    @ManyToMany(mappedBy="episodes")
    public List<BetaUser> users;
    
    @Override
    public String toString() {
        return this.series.name + " Season " + this.seasonNumber + " Episode " + this.episodeNumber;
    }
       
}
