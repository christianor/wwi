/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Christian
 */
public class SeriesNotFoundException extends Exception {
    public SeriesNotFoundException() {
        super("Series not found");
    }
    
    public SeriesNotFoundException(String message) {
        super(message);
    }
}
