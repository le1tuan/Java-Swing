package courseman2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import courseman2.controller.EnrolmentManager;
import courseman2.controller.ModuleManager;
import courseman2.controller.StudentManager;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
//import courseman2.controller.search.SearchManager;

/**
 * @overview Represents the main class of the CourseMan program. 
 *   
 * @attributes
 *  initial       String
 *  sman          StudentManager
 *  mman          ModuleManager
 *  emain         EnrolmentManager
 *  srman         SearchManager
 *  lhelper       CourseManDemo.LogoHelper
 *  mainGUI       JFrame
 *    
 * @abstract_properties
 * <pre>
 *   optional(initial) = false /\ 
 *   optional(sman) = false /\
 *   optional(mman) = false /\
 *   optional(eman) = false /\
 *   optional(srman) = false /\
 *   optional(lhelper) = false /\  
 *   optional(mainGUI) = false /\ 
 * </pre>  
 * 
 * @author dmle
 */
public class CourseManDemo implements ActionListener {
  /** the initial used as logo */
  @DomainConstraint(type=DomainConstraint.Type.String,optional=false)
  private String initial;
  
  /**the student manager*/
  @DomainConstraint(type=DomainConstraint.Type.UserDefined,optional=false)
  private StudentManager sman;
  
  /**the module manager*/
  @DomainConstraint(type=DomainConstraint.Type.UserDefined,optional=false)
  private ModuleManager mman;
  
  /**the enrolment manager*/
  @DomainConstraint(type=DomainConstraint.Type.UserDefined,optional=false)
  private EnrolmentManager eman;

  /** the search GUI used for searching objects */
  //@DomainConstraint(type=DomainConstraint.Type.UserDefined,optional=false)
//  private SearchManager srman;
  
  /**the logo helper used for creating a visual effect on the logo */
  //@DomainConstraint(type=DomainConstraint.Type.UserDefined,optional=false)
  //private LogoHelper lhelper;
  
  /**the main GUI window*/
  @DomainConstraint(type=DomainConstraint.Type.Object,optional=false)
  private JFrame mainGUI;

  // constructor method
  /**
     * @param initial
   * @effects 
   *  initialise this with initial
   *  {@link #createGUI()}: create mainGUI
   *  initialise sman, mman, eman, srman such that their (x,y) locations are each 50 pixels higher
   *    those of mainGUI
   *  initialise lhelper  
   *  add srman as object listener of sman, mman, eman
   */
  public CourseManDemo(String initial){
      this.initial=initial;
      createGUI();
      sman=new StudentManager("Manage Student","Enter student details" , 500, 300, 100, 100);
      mman=new ModuleManager("Manage Module", "Enter module details", 500, 300, 100, 100);
      eman=new EnrolmentManager("Manager Enrolment","Enrolment detail", 500, 300, 100, 100,sman,mman);
  }
  /**
   * @modifies this
   * @effects create mainGUI that has a menu bar with:
   *   
   *   File menu has two items: Save and Exit
   *   Tools has three menu items for the three data management functions,
   *        and a search menu item 
   *   Reports: has the two reporting functions
   *   {@link #createLogoPanel(JMenuBar, String)}: a logo panel containing a 
   *          logo label at the far end of the menu bar
   *   
   *     
   *   The action listener of the menu items is this.
   */
  protected void createGUI(){
      //Create JFrame
      mainGUI = new JFrame("Program CourseManDemo");
      mainGUI.setSize(300,400);
      //Create menuBar
      JMenuBar bar = new JMenuBar();
      mainGUI.setJMenuBar(bar);
      //Create menu
      JMenu file = new JMenu("File");
      //Create menuItem
      JMenuItem save = new JMenuItem("Save");
      JMenuItem exit = new JMenuItem("Exit");
      //Add actionListener
      exit.addActionListener(this);
      save.addActionListener(this);
      //add to menu
      file.add(save);
      file.add(exit);
      //add to menuBar
      bar.add(file );
      //Create menu
      JMenu tool = new JMenu("Tools");
      //Create menuItem
      JMenuItem stu = new JMenuItem("Manage Student");
      JMenuItem mod = new JMenuItem("Manage Module");
      JMenuItem enrol = new JMenuItem("Manage Enrolment");
      //Add actionListener
      stu.addActionListener(this);
      mod.addActionListener(this);
      enrol.addActionListener(this);
      //add to menu
      tool.add(stu);
      tool.add(mod);
      tool.add(enrol);
      //add to menuBar
      bar.add(tool);
      //Create menu
      JMenu report = new JMenu("Report");
      //Create menuItem
      JMenuItem ini = new JMenuItem("Initial Enrolment");
      //Add actionListener
      ini.addActionListener(this);
      //Create menuItem
      JMenuItem as = new JMenuItem("Assessed Enrolment");
      //Add actionListener
      as.addActionListener(this);
      //add to menu
      report.add(ini);
      report.add(as);
      //add to menuBar
      bar.add(report);
      createLogoPanel(bar,this.initial);
  }

  /**
   * @effects 
   *  create a label panel containing a decorated JLabel whose text is 
   *  initial. The decoration must use the following settings:
   *  
   *    background colour: orange
   *    foreground colour: blue
   *    font: Serif, bold, 18 points
   *    size: height=20, wide enough to fit the text
   *    alignment: center
   *    focusable: false 
   *  
   *  
   *  add the label panel to the menu bar mb so that it appears at the far end.
   *
   *  The logo text must have the "appearing" effect.
   */
  private void createLogoPanel(JMenuBar mb, String initial){
      JLabel label = new JLabel(initial,SwingConstants.CENTER);
      label.setForeground(Color.blue);
      label.setBackground(Color.orange);
      Font f = new Font("Serif",Font.BOLD,18);
      label.setFont(f);
      label.setSize(100,20);
      label.setFocusable(false);
      label.setOpaque(true);
      mb.add(Box.createHorizontalGlue());
      mb.add(label);
  }
  
  /**
   * @effects save data objects managed by sman, mman and eman to files 
   */
  public void save(){
      sman.save();
      mman.save();
      eman.save();
  }

  /**
   * @effects 
   *  start up sman, mman, eman, srman 
   *  start lhelper
   */
  public void startUp(){
      sman.startUp();
      mman.startUp();
      eman.startUp();
  }

  /**
   * @effects shut down sman, mman, eman, srman 
   *          dispose mainGUI and end the program. 
   */
  public void shutDown(){
      sman.shutDown();
      mman.shutDown();
      eman.shutDown();
      System.exit(0);
  }

  /**
   * @effects show mainGUI 
   */
  public void display(){
      mainGUI.setVisible(true);
  }
  /**
   * @effects handles user actions on the menu items
   *          <pre>
   *          if menu item is Tools/Manage students
   *            {@link #sman}.display()}
   *          else if menu item is Tools/Manage modules  
   *            {@link #mman}.display()
   *          else if menu item is Tools/Manage enrolments
   *            {@link #eman}.display()
   *          else if menu item is Tools/Search...
   *            {@link #srman}.display()
   *          else if menu item is Reports/Initial enrolment report
   *            {@link #eman}.report()
   *          else if menu item is Reports/Assessment report
   *            {@link #eman}.reportAssessment()
   *          else if menu item is File/Save
   *            {@link #save()}
   *          else if menu item is File/Exit 
   *            {@link #shutDown()}
   *          </pre>
   */
  public void actionPerformed(ActionEvent e){
      String cmd=e.getActionCommand();
      if(cmd.equals("Exit")){
          shutDown();
      }else if(cmd.equals("Manage Student")){   
          sman.display();
      }else if(cmd.equals("Manage Module")){
          mman.display();
      }else if(cmd.equals("Manage Enrolment")){ 
          eman.display();
      }else if(cmd.equals("Save")){
          save();
      }else if(cmd.equals("Initial Enrolment")){
          eman.report();
      }else if(cmd.equals("Assessed Enrolment")){
          eman.reportAssessment();
      }
  }

  /**
   * The run method
   * @effects 
   *  initialise an initial 
   *  create an instance of CourseManDemo from the initial 
   *  {@link #startUp()}: start up the CourseManDemo instance
   *  {@link #display()}: display the main gui of CourseManDemo instance 
   */
  public static void main(String[] args) {
    final String initial = "LTA";
    CourseManDemo app = new CourseManDemo(initial);
    app.startUp();
    app.display();
  }
}
