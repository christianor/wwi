/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
@CRUD.For(models.Series.class)
public class SeriesCRUD extends CRUD {
    
}
