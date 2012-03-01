/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
public class BetaUser extends Model {
    @Unique
    @Required
    public String username;
    @Required
    public String password;
    @ManyToMany
    public List<Series> series;
    @ManyToMany
    public List<Episode> episodes;
    
    public BetaUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String toString() {
        return this.username;
    }
            
    
}
