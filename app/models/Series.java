/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
public class Series extends Model {
    @Required
    public String serviceSeriesId;
    @ManyToMany(mappedBy="series")
    public List<BetaUser> users;
    @OneToMany(mappedBy="series")
    public List<Episode> episodes;
    public String name;

    @Override
    public String toString() {
        return this.name;
    }
       
}
