/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
public class Series extends Model {
    public String serviceSeriesId;
    @ManyToMany(mappedBy="series")
    public List<User> users;
    @OneToMany(mappedBy="series")
    public List<Episode> episodes;
    public String name;
}
