/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"serviceSeriesId"})})
public class Series extends Model {
    @Required
    public String serviceSeriesId;
    @ManyToMany(mappedBy="series")
    public List<WWIUser> users;
    @OneToMany
    public List<Episode> episodes;
    public String name;

    @Transient
    public int userCount;
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public static List<Series> getPopularSeries() {
        return JPA.em().createQuery("SELECT s FROM Series s ORDER BY SIZE(s.users) desc")
                .setMaxResults(9)
                .getResultList();
    }
       
}
