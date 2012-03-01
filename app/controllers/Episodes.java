/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import play.mvc.With;

/**
 *
 * @author Christian
 */
@Check("admin")
@With(Secure.class)
public class Episodes extends CRUD {
    
}
