/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
public class Episode extends Model {
    public int seasonNumber;
    public int episodeNumber;
    @ManyToOne
    public Series series;
    @ManyToMany(mappedBy="episodes")
    public List<User> users;
}
