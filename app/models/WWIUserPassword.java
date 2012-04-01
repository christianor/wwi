/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import play.db.jpa.Model;
import play.libs.Crypto;

/**
 *
 * @author Christian
 */
@Entity
public class WWIUserPassword extends Model {
    
    public String passwordHash;
    @OneToOne(mappedBy="password")
    public WWIUser user;
    
    public WWIUserPassword(String password) {
        passwordHash = Crypto.passwordHash(password);
    }
}
