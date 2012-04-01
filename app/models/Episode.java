/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint( columnNames= {"seasonNumber", "episodeNumber", "series_id"} ))
public class Episode extends Model {

    @Required
    public int seasonNumber;
    @Required
    public int episodeNumber;
    @ManyToOne(optional=false)
    @JoinColumn(name="series_id")
    @Required
    public Series series;
    @ManyToMany(mappedBy = "episodes")
    public List<WWIUser> users;
}
