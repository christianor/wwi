package controllers;

import models.WWIUser;
import play.libs.OpenID;
import play.libs.OpenID.UserInfo;
import play.mvc.Before;
import play.mvc.Controller;

public class Secure extends Controller {

    @Before(unless = {"login", "authenticate"})
    static void checkAuthenticated() {
        if (!session.contains("userEmail")) {
            login();
        }
    }

    public static void login() {
        render();
    }
    
    public static void logout() {
        session.clear();
        flash.success("You have been logged out");
        login();
    }

    public static void authenticate(String user) {
        if (OpenID.isAuthenticationResponse()) {
            UserInfo verifiedUser = OpenID.getVerifiedID();
            if (verifiedUser == null) {
                flash.error("Oops. Authentication has failed");
                login();
            }
            
            String email = verifiedUser.extensions.get("email");
            
            WWIUser wwiUser = WWIUser.find("email", email).first();
            if (wwiUser == null) {
                wwiUser = new WWIUser(email).save();
            }
             
            session.put("userId", wwiUser.id);
            session.put("userEmail", email);
            
            Series.userSeries();
            
        } else {
            if (!OpenID.id(request.params.get("openid_identifier"))
                    .required("email", "http://axschema.org/contact/email")
                    .verify()) { 
                // will redirect the user
                flash.error("Cannot verify your OpenID");
                login();
            }
        }
    }
    
    public static String connectedEmail() {
        return session.get("userEmail");
    }
    
    public static long connectedId() {
        return Long.parseLong(session.get("userId"));
    }
    
}