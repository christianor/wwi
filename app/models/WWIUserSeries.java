/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity(name = "WWIUSER_SERIES")
public class WWIUserSeries extends Model {
    @ManyToOne
    @JoinColumn(name = "WWIUSERS_ID")
    public WWIUser user;
    @ManyToOne
    @JoinColumn(name = "SERIES_ID")
    public Series series;
}
