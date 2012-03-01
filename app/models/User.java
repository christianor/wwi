/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.Table;
import play.data.validation.Unique;
import play.db.jpa.Model;

/**
 *
 * @author Christian
 */
@Entity
@Table(appliesTo="\"User\"")
public class User extends Model {
    @Unique
    public String username;
    public String password;
    @ManyToMany
    public List<Series> series;
    @ManyToMany
    public List<Episode> episodes;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
            
    
}
