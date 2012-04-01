/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.HashMap;
import models.WWIUser;
import models.WWIUserPassword;
import org.apache.commons.mail.SimpleEmail;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Crypto;
import play.libs.Mail;
import play.mvc.Controller;
import play.mvc.Router;

/**
 *
 * @author Christian
 */
public class Register extends Controller {
    public static void index() {
        render();
    }
    
    public static void save(@Email @Required String email, String repeatedEmail, 
            @Required @MinSize(6) String password, String repeatedPassword) throws Exception {
        
        if (password == null ? repeatedPassword != null : !password.equals(repeatedPassword))
            Validation.addError("repeatedPassword", "Passwords do not match");
        
        if (email == null ? repeatedEmail != null : !email.equals(repeatedEmail))
            Validation.addError("repeatedEmail", "Emails do not match");
        
        
        if (Validation.hasErrors()) {
            
            params.flash("email", "repeatedEmail");
            Validation.keep();
            index();
        }
        
        WWIUser user = WWIUser.find("email", email).first();
        
        boolean registrationSuccessful = false;
        
        if (user != null)
            render(registrationSuccessful);
            
        user = new WWIUser(email);
        user.password = new WWIUserPassword(password);
        
        
        String secretKey = Crypto.encryptAES(email);
        
        Cache.safeAdd(secretKey, user, "2d");
                
        SimpleEmail mail = new SimpleEmail();
        mail.addTo(email);
        
        mail.setSubject("Where Was I Registration");
        mail.setFrom("wherewasi@noreply.com");
        
        HashMap map = new HashMap();
        map.put("secretKey", secretKey);
        
        mail.setMsg("Click the following link to activate your account: wwi.herokuapp.com" + 
                Router.reverse("Register.activate", map));
        
        Mail.send(mail);
        
        registrationSuccessful = true;
        render(registrationSuccessful);
    }
    
    public static void activate(String secretKey) {
        
        WWIUser user = (WWIUser)Cache.get(secretKey);
        
        if (user == null)
        {
            flash.error("Activation was not possible, the activation link is expired. Please re-register");
            Secure.login();
        }
        else
        {
            user.password.save();
            user.save();
            
            Cache.delete(secretKey);
            
            flash.success("Activation was successful. Please login to continue");
            Secure.login();
            
        }
        
    }
}
