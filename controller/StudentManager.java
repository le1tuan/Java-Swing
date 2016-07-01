/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.controller;

import courseman2.DomainConstraint;
import static courseman2.DomainConstraint.Type.String;
import courseman2.NotPossibleException;
import courseman2.model.Student;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Le Tuan Anh
 */
/**
 * @overview StudentManager is a program allowed user can create a new Student
 * @attributes 
 * name,dob,addr,email JTexField
 * mid JPanel
 * @abstract_properties
 * @author Le Tuan Anh
 */
public class StudentManager extends Manager{
    @DomainConstraint(type=DomainConstraint.Type.UserDefined)
    private JTextField name,dob,addr,email;
    @DomainConstraint(type=DomainConstraint.Type.UserDefined)
    private JPanel mid ;
    /**
     * @effects
     *  initialise this as StudentManager <title,titleText,width,height,x,y>
     * @param title
     * @param titleText
     * @param width
     * @param height
     * @param x
     * @param y 
     */
    public StudentManager(String title, String titleText, int width, int height, int x, int y) {
        super(title, titleText, width, height, x, y);
    }
    @Override
    /**
     * @effect
     *      initial JPanel mid
     *      initial JLabel lname
     *      add lname to mid
     *      initial JLabel ldob
     *      add ldob to mid
     *      initial JLabel laddr
     *      add laddr to mid
     *      initial JLabel lemail
     *      add lemail to mid
     *      add mid to this.gui
     */
    protected void createMiddlePanel() {
        if(this.gui!=null){
            mid= new JPanel();
            GridLayout grid = new GridLayout(0,2);
            mid.setLayout(grid);
            //Name
            JLabel lname=new JLabel("Name: ");
            mid.add(lname);
            name = new JTextField();
            name.setPreferredSize(new Dimension(200, 50));
            mid.add(name);
            //DOb
            JLabel ldob=new JLabel("Dob: ");
            mid.add(ldob);
            dob = new JTextField();
            dob.setPreferredSize(new Dimension(200, 50));
            mid.add(dob);
            //Address
            JLabel laddr=new JLabel("Address: ");
            mid.add(laddr);
            addr = new JTextField();
            addr.setPreferredSize(new Dimension(200, 50));
            mid.add(addr);
            //Email
            JLabel lemail=new JLabel("Email: ");
            mid.add(lemail);
            email = new JTextField();
            email.setPreferredSize(new Dimension(200, 50));
            mid.add(email);
            //add this Panel to main gui.
            this.gui.add(mid,BorderLayout.CENTER);
        }
        
    }
    @Override
    /**
     * @effect
     *  get all input data from user
     *  if validate data true
     *      get class dmct of domainconstraint
     *      get class s of Student
     *      get all field  f of student
     *      foreach (Field f1 : f)
     *          get annotation of f1
     *          use method validate to cast f1 to true type
     *      use method newInstance to create new Student 
     *  
     */
    public Object createObject() throws NotPossibleException {
        //get all input data
        String n=name.getText();
        String d=dob.getText();
        String a= addr.getText();
        String e=email.getText();
        String message="";
        if(n.equals("")||d.equals("")||a.equals("")||e.equals("")){
            message="Please enter data";
            displayErrorMessage(message,"Create Student");
        }else{
            boolean validate1=true;
            validate1=validateDate(d);
            if(!validate1){
                message="Wrong format dateofbirth "+d;
            }
            boolean validate2=true;
            validate2=validateEmail(e);
            if(!validate2){
                message=message+"\nWrong format email "+e;
            } 
            if(message!=""){
                displayErrorMessage(message,"Create Student");
            }else{
                    Class dmct=DomainConstraint.class;
                    Class s = Student.class;
                    Field[] f =s.getDeclaredFields();
                    Object[] o = {n,d,a,e};
                    String [] textField={"name","dob","address","email"};
                    boolean check=true;
                    int i=0;
                    for (Field f1 : f) {  
                        String temp=f1.getName();
                        for(int j=0;j<textField.length;j++){
                            if(temp.equals(textField[j])){
                                DomainConstraint dc= (DomainConstraint) f1.getAnnotation(dmct);
                                if(dc!=null&&i<o.length){
                                    try{
                                        Object ob =validate(dc, o[j]);
                                    }catch(NotPossibleException ne){
                                        System.err.println("Attribute: " + temp + "\n\tvalue: " + o[j]
                                      + " -> " + ne.getMessage());
                                        displayErrorMessage(ne.getMessage(),"Create Student");
                                        check=false;
                                    }

                                }  
                            }
                        }   
                    }
                    if(check){
                        Student stu =(Student) newInstance(s,o);
                        displayMessage("Created Student("+stu.getId()+","+stu.getName()+","+stu.getDob()+","
                        +stu.getAddress()+","+stu.getEmail()+")", "Create a student");
                        objects.add(stu);
                        return stu;
                    }else{
                        throw new NotPossibleException("error");
                    } 
            }
        }
        
        
        return null;
    }

    @Override
    /**
     * @effect
     *  Save all necessary information of Student which in Vector objects to file student.dat
     */
    public void save(){
        if(this.objects!=null){
            File f = new File("students.dat");
            try {
                FileOutputStream out = new FileOutputStream(f);
                ObjectOutputStream  op = new ObjectOutputStream(out);
                for(int i=0 ; i<objects.size();i++){
                    Student s = (Student) objects.get(i);
                    op.writeObject(s);
                }
                System.out.println("saved students");
                op.close();
            } catch (FileNotFoundException ex) {
                System.err.println("File students.dat is not found.If it is the first"
                        + "time you run program, Please ignore it");
            } catch (IOException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    @Override
    /**
     * @effect
     *      use all information in file students.dat to create a object of Class Student
     *      add this object to Vector objects
     */
    public void startUp() {
        if(this.objects!=null){
            File f = new File("students.dat");
            try {
                FileInputStream in = new FileInputStream(f);
                ObjectInputStream ip = new ObjectInputStream(in);
                while(true){
                    Student s = (Student) ip.readObject();
                    objects.add(s);
                }
            } catch (FileNotFoundException ex) {
                System.err.println("File students.dat is not found.If it is the first"
                        + "time you run program, Please ignore it");
            } catch (IOException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }catch (ClassNotFoundException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Load Student Complete");  
        }
            
     
    }

    @Override
    /**
     * @effect
     *  link : clearGUI(this.mid)
     */
    public void clearGUI() {
       clearGUI(this.mid);
    }
     /**
     * @require: String d
     * @effect
     * if d is not mathces regex
     *      return false
     * else
     *      return true
     * @param d
     * @return 
     */
    public boolean validateDate(String d){
        if(!d.matches("^(([0][0-9]|[0-9])|([0-2][0-9])|([3][0-1]))(\\-|\\\\|\\.|\\/)([0][1-9]|[1-9]|[1][0-2])(\\-|\\\\|\\.|\\/)\\d{4}$")){
            //
            return false;
        }
        return true;
    }
    /**
     * @require: String e
     * @effect
     * if e is not mathces regex
     *      return false
     * else
     *      return true
     * @param e
     * @return 
     */
    public boolean validateEmail(String e){
        if(!e.matches("^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$")){
            //
            return false;
        }
        return true;
    }
}
