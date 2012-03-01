/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import controllers.CRUD;
import play.mvc.Controller;
import play.mvc.*;
/**
 *
 * @author Christian
 */
@Check("admin")
@With(Secure.class)
public class BetaUsers extends CRUD {
    
}
