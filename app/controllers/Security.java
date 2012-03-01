package controllers;

import models.BetaUser;
import play.mvc.Scope.Session;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        BetaUser user = BetaUser.find("username", username).first();
        
        if (user != null && user.password.equals(password))
            Session.current().put("userid", user.id);
        
        return user != null && user.password.equals(password);
    }

    static void onAuthenticated() {
        Series.userSeries();
    }

    static void onDisconnected() {
        Application.index();
    }
    
    static boolean check(String profile) {
    if("admin".equals(profile)) {
        return Security.connected().equals("admin");
    }
    return false;
}
}