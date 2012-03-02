/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import play.mvc.*;
/**
 *
 * @author Christian
 */
@Check("admin")
@With(Secure.class)
@CRUD.For(models.BetaUser.class)
public class BetaUsersCRUD extends CRUD {

}
