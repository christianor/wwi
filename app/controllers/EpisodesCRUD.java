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
@CRUD.For(models.Episode.class)
public class EpisodesCRUD extends CRUD {
}
