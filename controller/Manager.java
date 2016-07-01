package courseman2.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import courseman2.DomainConstraint;
import courseman2.DomainConstraint.Type;
import static courseman2.DomainConstraint.Type.Char;
import courseman2.NotPossibleException;
import courseman2.model.CompulsoryModule;
import courseman2.model.ElectiveModule;
import courseman2.model.Enrolment;
import courseman2.model.Module;
import courseman2.model.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @overview A sub-type of AppController that represents the abstract super-class of all the data manager
 *           classes.
 * 
 * @attributes
 *  objects   Vector
 * 
 * @abstract_properties 
 * <pre>
 *  P_AppController/\ 
 *  optional(objects) = false /\  
 *  objects.size > 0 -> (for all o in objects:
 *      o.data is obtained from the data panel of gui)
 *  </pre>
 *  
 * @author dmle
 */
public abstract class Manager extends AppController {
  /**keep the objects that are managed by this manager*/
  @DomainConstraint(type = DomainConstraint.Type.Object, optional = false)
  protected Vector objects;

  // constructor
  /**
   * @effects 
   *  initialise <tt>this</tt> with a <tt>gui</tt> using the specified settings 
   *  and an empty set of objects
   */
  public Manager(String title, String titleText, int width, int height, int x, int y) {
    super(title, titleText, width, height, x, y);
    objects = new Vector();
  }
  
  /**
   * @requires gui != null
   * @modifies this
   * @effects create a (middle) data panel for displaying the <b>input labels and
   *          fields</b> appropriate for the type of <b>objects</b> that are managed by
   *          <tt>this</tt>
   */
  @Override
  protected abstract void createMiddlePanel();

  /**
   * @effects 
   *   {@link #createObject()}: create a new object
   *   {@link #fireObjectCreated(Object)}: inform object listeners about the new object
   *   
   *   <p>Throws NotPossibleException if failed to create object.
   */
  @Override
  public void doTask() throws NotPossibleException {
    // TODO: complete this code
          try{
              Object o=createObject();
              fireObjectCreated(o);  
          }catch(NotPossibleException e){
              throw new NotPossibleException(e.getMessage());
          }  
  }

  /**
   * @effects 
   * <pre>
   *  create a new data object from the data in the data panel of
   *     gui and add it to objects
   *     
   *  if succeeded 
   *     return the new object
   *  else 
   *     throws NotPossibleException
   *  </pre>
   */
  public abstract Object createObject() throws NotPossibleException;
  
  /**
   * @effects <pre>
   *    if exists field f in class cls whose name is fieldName
   *      return f
   *    else if exists field f in the super-class of cls whose name is fieldName
   *      return f
   *    else
   *      return null
   *  </pre>
   */
  protected static Field getDeclaredField(Class cls, String fieldName) {
    Field f = null;
    try {
      f = cls.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      // get field from the super class
      try {
        f = cls.getSuperclass().getDeclaredField(fieldName);
      } catch (NoSuchFieldException e1) {
        // should not happen
      }
    }
    return f;
  }
  /**
   * @effects <pre>
   *  check and convert value to a value val of the type specified in cons.type
   *  based on the specified domain constraint.
   *
   *  if succeeds 
   *    return val
   *  else
   *    return null 
   * </pre>
   */
protected static Object validate(DomainConstraint cons, Object value) {
      // TODO: complete this code
    Object val=null;
    Type t = cons.type();
    if(t.equals(Type.String)){
        val=(String)value;
        if(((String) val).length()>cons.length()){
            throw new NotPossibleException("Wrong length input value: length of value :"+cons.length());
        }
    }else if(t.equals(Type.Integer)){
        val = Integer.parseInt(value.toString());
        if(((Integer)val)<cons.min()){
            throw new NotPossibleException(""+(Integer)val);
        }
        if(((Integer)val)>cons.max()){
            throw new NotPossibleException(""+(Integer)val);            
        }
    }else if(t.equals(Type.Double)){
        val =Double.parseDouble(value.toString());
        if(((Double)val)<cons.min()){
            throw new NotPossibleException(""+(Double)val);
        }
        if(((Double)val)>cons.max()){
            throw new NotPossibleException(""+(Double)val);            
        }
    }else if(t.equals(Type.Long)){
        val =Long.parseLong(value.toString()); 
        if(((Long)val)<cons.min()){
            throw new NotPossibleException(""+(Long)val);
        }
        if(((Long)val)>cons.max()){
            throw new NotPossibleException(""+(Long)val);            
        }
    }else if(t.equals(Type.Float)){
        val = Float.parseFloat(value.toString());
        if(((Float)val)<cons.min()){
            throw new NotPossibleException(""+(Float)val);
        }
        if(((Float)val)>cons.max()){
            throw new NotPossibleException(""+(Float)val);            
        }
    }else{
         return null;
    }
    return val;
 }
     
            
  
  /**
   * @effects <pre>
   *    if a constructor cons of c is found matching attributeVals
   *    and cons is correctly defined 
   *      create a new object of c by invoking cons using attributeVals as arguments 
   *    else 
   *      throws NotPossibleException 
   *  </pre>
   */                     
  protected static Object newInstance(Class c, Object[] attributeVals)
      throws NotPossibleException {
    Object o = null;
    try {

      // create a new object using the default constructor method
      Constructor[] cons = c.getDeclaredConstructors();
      // find the constructor that has the same signature as the attributes
      // specified in values
      Constructor co = null;
      Class[] paramTypes;
      OUTER: for (int i = 0; i < cons.length; i++) {
        co = cons[i];
        paramTypes = co.getParameterTypes();
        if (paramTypes.length == attributeVals.length) {    
          boolean match = true;
          CONS: for (int k = 0; k < paramTypes.length; k++) {
            Class type = paramTypes[k];
            
            Object obj = attributeVals[k];
            
            // compare the object type with the parameter type
            if (obj == null)
              continue; // consider a match

            Class oc = obj.getClass();
            
            if (!type.equals(oc) && !isDecendant(oc, type)) {
              match = false;
              break CONS;
            }
          } // end CONS loop
          if (match) {
            // found the constructor
            break OUTER;
          }
        }
        co = null;
      } // end OUTER loop

      if (co == null) {
        throw new NotPossibleException(
            "Manager.newInstance: could not find constructor matching the data values");
      }
      // System.out.println("constructor: " + co);
      // create a new object
      o = co.newInstance(attributeVals);
    } catch (InvocationTargetException e) {
      throw new NotPossibleException(
          "Manager.newInstance: failed to create a new instance for class: "
              + c.getName() + ": " + e.getTargetException().getMessage());
    } catch (Exception e) {
      throw new NotPossibleException(
          "Manager.newInstance: failed to create a new instance for class: "
              + c.getName() + ": " + e.getMessage());
    }

    return o;
  }

  /**
   * @requires <tt>c1 != null && c2 != null</tt>
   * @effects <pre> 
   *  if class c1 is a descendant of class c2
   *  i.e. c1 is a sub-class of c2 or a sub-class of a decendant of c2
   *    return true
   *  else
   *    return false
   *  </pre>
   */
  private static boolean isDecendant(Class c1, Class c2) {
    Class sup = c1.getSuperclass();
    if (sup == null)
      return false;
    if (sup == c2) {
      return true;
    } else if (sup != Object.class) {
      return isDecendant(sup, c2);
    } else {
      return false;
    }
  }

  /**
   * @requires <tt>objects != null</tt>
   * @effects save <tt>objects</tt> to file <tt>X.dat</tt>, where X is named
   *          after the object type,
   *          e.g. if the object type is Student then X is <tt>students</tt>.
   *          
   *          <pre>if succeeds in saving objects
   *            display a console message
   *          else 
   *            display a console error message</pre>
   */
  public abstract void save();

  /**
   * @requires <tt>objects != null</tt>
   * @modifies this
   * @effects load into <tt>objects</tt> the data objects in the storage file
   *          <tt>X.dat</tt> that was used by
   *          method <tt>save</tt> to store objects.
   *          
   *          <br>inform object listeners of each loaded object.
   *           
   *          <pre>if succeeds 
   *            display a console message 
   *          else 
   *            display a console error message</pre>
   */
  @Override
  public abstract void startUp();

  /**
   * @requires <tt>gui != null</tt>
   * @modifies this
   * @effects dispose <tt>gui</tt> and clear <tt>objects</tt>
   */
  @Override
  public void shutDown() {
    super.shutDown();
    objects.clear();
    objects = null;
  }
}
