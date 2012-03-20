/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity(name = "WWIUSER_EPISODE")
public class WWIUserEpisode extends Model {
    @ManyToOne
    @JoinColumn(name = "WWIUSERS_ID")
    @Cascade(CascadeType.REFRESH)
    public WWIUser user;
    @ManyToOne
    @JoinColumn(name = "EPISODES_ID")
    public Episode episode;
}
