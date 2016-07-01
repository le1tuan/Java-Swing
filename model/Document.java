package courseman2.model;

/**
 * @overview 
 *  Represents a document that is used in the keyword search engine.
 * 
 * @author dmle
 */
public interface Document {
  
  /**
   * @effects 
   *   return an HTML document whose title and body are derived from this
   */
  public String toHtmlDoc();
}
