package courseman2.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import courseman2.DomainConstraint;
import courseman2.NotPossibleException;
import java.util.Locale;

/**
 * @overview Represents the abstract super-type of all the GUI-based controllers.
 *           It is sub-type of <tt>ActionListener</tt>, which requires that the
 *           sub-classes handle the user actions on the GUI.
 * 
 * @attributes
 *  gui       JFrame
 *  title     String
 *  titleText String
 *  dim       Dimension
 *  location  Point
 *  objectListeners List<ObjectListener>
 * 
 * @abstract_properties
 *  <pre>
 *  optional(title)=false /\ optional(titleText) = false /\ optional(dim) = false /\ 
 *  optional(location) = false /\ optional(objectListeners) = false /\
 *  option(gui) = false /\  
 *  gui.title = title /\ gui.size = dim /\ gui.location = location /\
 *  (A): 
 *  gui.menuBar.components = [ menu<"File"> ] /\  
 *  gui.contentPane.components =  
 *    [ top:panel<>, middle:panel<>, bottom:panel<> ] /\ 
 *  top.components= [ label<titleText> ]
 *  size(middle.components) > 0 /\ 
 *  bottom.components = [ ok:button<"OK">, cancel:button<"Cancel"> ] /\
 *  ok.actionListener = this /\ cancel.actionListener = this
 *  </pre>
 *  
 *  <p>i.e. 
 *  <br>(A):
 *  <br><tt>gui</tt>'s menu bar contains one <tt>"File"</tt> menu /\ 
 *  <br><tt>gui</tt> has three panel components named <tt>top</tt>, <tt>middle</tt>, and <tt>bottom</tt> /\
 *  <br><tt>top</tt> panel has a label whose content is <tt>titleText</tt> /\ 
 *  <br><tt>middle</tt> panel has at least one component /\ 
 *  <br><tt>bottom<tt> panel has two buttons "OK" (named <tt>ok</tt>) and "Cancel" (named <tt>cancel</tt>) /\ 
 *  <br>action listener of buttons <tt>ok</tt> and <tt>cancel</tt> are <tt>this</tt>
 *   
 * @author dmle
 */
public abstract class AppController implements ActionListener{
  /**the data management GUI window*/
  @DomainConstraint(type = DomainConstraint.Type.Object,optional=false)
  protected JFrame gui;

  @DomainConstraint(type = DomainConstraint.Type.String,optional=false,mutable=false)
  private String title;
  
  @DomainConstraint(type = DomainConstraint.Type.String,optional=false,mutable=false)
  private String titleText;
  
  @DomainConstraint(type = DomainConstraint.Type.Object,optional=false,mutable=false)
  private Dimension dim;
  
  @DomainConstraint(type = DomainConstraint.Type.Object,optional=false,mutable=false)
  private Point location;

  @DomainConstraint(type = DomainConstraint.Type.Collection,optional=false)
  private List objectListeners;
  // constructor
  /**
   * @effects 
   *  initialise this with a <tt>gui</tt> using the specified settings
   *  <br>initialise <tt>objectListeners</tt> as empty 
   */
  public AppController(String title, String titleText, int width, int height, int x, int y) {
    this.title = title;
    this.titleText = titleText;
    dim = new Dimension(width, height);
    location = new Point(x,y);
    createGUI();
    objectListeners = new LinkedList();
  }

  /**
   * @modifies this
   * @effects {@link #initGUI()}: create <tt>gui</tt> to be a JFrame-typed window that has:
   * 
   *          <ol>
   *          <li>{@link #createFileMenu()}: a File menu with a single Exit menu item. 
   *          <li>{@link #createTopPanel()}: a top panel for displaying title text,
   *          <li>{@link #createMiddlePanel()}: a (middle) data panel for displaying GUI-related components specific 
   *          for the current application function, 
   *          <li>{@link #createBottomPanel()}: a (bottom) button panel for displaying two command buttons: OK
   *          and Cancel, and 
   *          </ol>
   */
  private void createGUI() {
    // initialise gui
    initGUI();

    // a File menu with a single Exit menu item.
    createFileMenu();
    
    // top panel
    createTopPanel();
    
    // middle panel
    createMiddlePanel();
    
    // bottom panel
    createBottomPanel();
  }

  /**
   * @requires gui != null
   * @modifies this
   * @effects
   *  initialise <tt>gui</tt> to be a JFrame-typed window whose title is <tt>title</tt>, 
   *  size is <tt>dim</tt> and location is <tt>location</tt>.
   */
  private void initGUI() {
    // TODO: complete this code
          this.gui=new JFrame(this.title);
          int width=this.dim.width;
          int height=this.dim.height;
          this.gui.setSize(dim);
          this.gui.setLocation(location);
  }
  
  /**
   * @requires gui != null
   * @modifies this
   * @effects
   *  create a File menu with a single Exit item, which hides <tt>gui</tt>.
   *  
   *  <p>The action listener of this item is <tt>this</tt>.
   */
  private void createFileMenu() {
    // TODO: complete this code
        if(this.gui!=null){
            JMenuBar bar = new JMenuBar();
            this.gui.setJMenuBar(bar);
            JMenu menu = new JMenu("File");
            JMenuItem exit = new JMenuItem("Exit");
            exit.addActionListener(this);
            menu.add(exit);
            bar.add(menu);
        }
  }
  
  /**
   * @requires gui != null
   * @modifies this
   * @effects
   *  a top panel of <tt>gui</tt> for displaying <tt>titleText</tt>
   */
  private void createTopPanel() {
    // TODO: complete this code
      if(gui!=null){
          JPanel top = new JPanel();
          JLabel label = new JLabel(titleText);
          top.add(label);
          this.gui.add(top,BorderLayout.NORTH);     
      }
  }
  
  /**
   * @requires gui != null
   * @modifies this
   * @effects
   *  create a middle panel of <tt>gui</tt> for displaying GUI-related components specific 
   *          for the current application function
   */
  protected abstract void createMiddlePanel();
  
  /**
   * @requires gui != null
   * @modifies this
   * @effects create a (bottom) button panel of <tt>gui</tt> for displaying two command buttons: OK
   *          and Cancel. 
   *          
   *          <p>The OK button performs the application function using the data entered on 
   *          <tt>gui</tt>. The Cancel button reset the data panel by clearing the GUI components.
   * 
   *          <p>The action listeners of these buttons are <tt>this</tt>. 
   */
  private void createBottomPanel() {
    // TODO: complete this code    
      if(this.gui!=null){
          JPanel bottom = new JPanel();
          JButton ok = new JButton("OK");
          ok.addActionListener(this);
          JButton cancel = new JButton("Cancel");
          cancel.addActionListener(this);
          bottom.add(ok);
          bottom.add(cancel);
          this.gui.add(bottom,BorderLayout.SOUTH);
      }
  }
  
  /**
   * @requires <tt>gui != null</tt>
   * @effects show <tt>gui</tt>
   */
  public void display() {
      if(this.gui!=null){
          gui.setVisible(true);
      }
    
  }

  /**
   * @effects 
   *  return title
   */
  public String getTitle() {
    return title;
  }
  
  /**
   * @requires <tt>gui != null</tt>
   * @effects display on <tt>gui</tt> an informational message dialog whose
   *          title is <tt>title</tt>,
   *          and whose message is <tt>mesg</tt>
   */
  protected void displayMessage(String mesg, String title) {
    JOptionPane.showMessageDialog(gui, mesg, title,
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * @requires <tt>gui != null</tt>
   * @effects display on <tt>gui</tt> an error message dialog whose title is
   *          <tt>title</tt>,
   *          and whose message is <tt>mesg</tt>
   */
  protected void displayErrorMessage(String mesg, String title) {
    JOptionPane.showMessageDialog(gui, mesg, title, JOptionPane.ERROR_MESSAGE);
  }

  /**
   * @effects 
   *  add <tt>listener</tt> to <tt>this</tt>
   */
  public void addObjectListener(ObjectListener listener) {
    objectListeners.add(listener);
  }
  
  /**
   * @effects 
   *  inform each <tt>ObjectListener</tt> in <tt>this</tt> that 
   *  <tt>obj</tt> has been created. 
   */
  protected void fireObjectCreated(Object obj) {
    for (Object o : objectListeners) {
      ((ObjectListener)o).objectCreated(obj);
    }
  }
  
  /**
   * @effects handles user action performed on <tt>gui</tt>:
   * 
   * <pre>
   *      if e.commandAction is File/Exit
   *        {@link #exit()}
   *      else if e.commandAction is OK
   *        {@link #doTask()}
   *        if exception is thrown        
   *          {@link #displayErrorMessage} : display the exception message on the GUI
   *
   *      else if e.commandAction is Cancel
   *        {@link #clearGUI()}</pre>
   */
  public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Exit")) {
          exit();
        } else if (cmd.equals("OK")) {
          doTask();
        } else if (cmd.equals("Cancel")) {
          // clear the GUI to enter again
          clearGUI();
        }
  }

  /**
   * @effects perform the current application function using the data in the data panel of
   *          <tt>gui</tt> as input
   *          
   *          <p>Throws NotPossibleException if failed to perform the function.
   */
  public abstract void doTask() throws NotPossibleException;
  
  /**
   * @effects clear the GUI components on <tt>gui</tt> 
   */
  public abstract void clearGUI();
  
  /**
   * A helper method for {@link #clearGUI()}.
   * 
   * @effects
   *  recursively clear the enabled text fields in <tt>panel</tt> and in all of its sub-panels. 
   */
  protected void clearGUI(JPanel panel) {
    Component[] comps = panel.getComponents();
    Component comp;
    JTextField tf;

    for (int i = 0; i < comps.length; i++) {
      comp = comps[i];
      if (comp instanceof JTextField) {
        // found a text field
        tf = (JTextField) comp;

        // only clear the enabled text fields
        if (tf.isEnabled())
          tf.setText("");
      } else if (comp instanceof JPanel) {
        clearGUI((JPanel) comp);
      }
    }
  }
  
  /**
   * @modifies this
   * @effects initialise any application resources associated to the attributes of <tt>this</tt>.
   *          
   *          <pre>if succeeds 
   *            display a console message 
   *          else 
   *            display a console error message</pre>
   */
  public abstract void startUp();

  /**
   * @requires <tt>gui != null</tt>
   * @effects 
   *   {@link #clearGUI()}: reset <tt>gui</tt> 
   *   hide <tt>gui</tt>
   */
  public void exit() {
    clearGUI();
    gui.setVisible(false);
  }

  /**
   * @requires <tt>gui != null</tt>
   * @modifies this
   * @effects 
   *  dispose <tt>gui</tt> and clear any resources associated to attributes of <tt>this</tt>
   */
  public void shutDown() {
    gui.dispose();
    objectListeners.clear();
    objectListeners = null;
  }
    
}
