package controllers;

import models.BetaUser;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Scope.Session;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {

        validation.required(username);
        validation.required(password);
        
        if (!validation.hasErrors()) {
            BetaUser user = BetaUser.find("username", username).first();

            if (user != null && user.password.equals(password)) {
                Session.current().put("userid", user.id);
                return true;
            }

            return false;
        }
        else {
            return false;
        }
    }

    static void onAuthenticated() {
        Series.userSeries();
    }

    static void onDisconnected() {
        Application.index();
    }

    static boolean check(String profile) {
        if ("admin".equals(profile)) {
            return Security.connected().equals("admin");
        }
        return false;
    }
}