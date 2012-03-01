/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import play.data.validation.Unique;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
public class BetaUser extends Model {
    @Unique
    public String username;
    public String password;
    @ManyToMany
    public List<Series> series;
    @ManyToMany
    public List<Episode> episodes;
    
    public BetaUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
            
    
}
