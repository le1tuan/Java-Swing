package courseman2.controller;

import courseman2.NotPossibleException;

/**
 * @overview 
 *  Represents a listener for object manipulation events (e.g. when a object has been created).
 *  
 * @author dmle
 */
public interface ObjectListener {
  
  /**
   * @requires obj != null
   * @effects
   *   handle the event that object <tt>obj</tt> has been created.
   *   
   *   <br>Throws NotPossibleException if fails to handle the event.
   */
  public void objectCreated(Object obj) throws NotPossibleException;
}
