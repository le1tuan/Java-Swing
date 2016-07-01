/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.controller;

import courseman2.DomainConstraint;
import courseman2.NotPossibleException;
import static courseman2.controller.Manager.validate;
import courseman2.model.CompulsoryModule;
import courseman2.model.ElectiveModule;
import courseman2.model.Module;
import courseman2.model.Student;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *@overview ModuleManager is a program allowed user can create a new Module
 * @attributes
 *  name,semester,credis,departmentName JTextField
 *  jc JComboBox
 *  mid JPnel
 *  dpm JLabel
 * 
 * @author Le Tuan Anh
 */
public class ModuleManager extends Manager{
    @DomainConstraint(type=DomainConstraint.Type.UserDefined)
    private JTextField name,semester,credis,departmentName; 
    @DomainConstraint(type=DomainConstraint.Type.UserDefined)
    private JComboBox jc;
    @DomainConstraint(type=DomainConstraint.Type.UserDefined)
    private JPanel mid;
    @DomainConstraint(type=DomainConstraint.Type.UserDefined)
    private JLabel dpm;/**
     * constructor method
     * @effect
     *   initialize ModuleManager m<title,titleText,width,height,x,y>
     * @param title
     * @param titleText
     * @param width
     * @param height
     * @param x
     * @param y 
     */
    public ModuleManager(String title, String titleText, int width, int height, int x, int y) {
        super(title, titleText, width, height, x, y);    
    }

    @Override
    /**
     * @effect
     *      initialize mid as new JPanel
     *      set gridlayout for mid
     *      initialize label op
     *      initialize jc as new JComboBox
     *      add event to jc
     *      add jc and op to mid
     *      initialize label lname
     *      add lname to mid
     *      initialize textfield name
     *      add name to mid
     * 
     *      initialize label lsemester
     *      add lsemester to mid
     *      initialize textfield semester
     *      add semester to mid
     * 
     *      initialize label lcredis
     *      add lcredis to mid
     *      initialize textfield credis
     *      add credis to mid
     * 
     *      initialize label lname
     *      add lname to mid
     *      initialize textfield name
     *      add name to mid
     *      initialize label lname
     *      add lname to mid
     *      initialize textfield name
     *      add name to mid
     */
    protected void createMiddlePanel() {
        if(this.gui!=null){
            mid = new JPanel();
            GridLayout grid = new GridLayout(0,2);
            mid.setLayout(grid);
            //Option
            JLabel op = new JLabel("Option");
            Object[] v ={"CompulsoryModule","ElectiveModule"};
            jc = new JComboBox(v);
            jc.setSelectedIndex(0);
            mid.add(op);
            mid.add(jc);
            
            //Name
            JLabel lname=new JLabel("Name: ");
            mid.add(lname);
            name = new JTextField();
            name.setPreferredSize(new Dimension(200, 50));
            mid.add(name);
            //DOb
            JLabel lsemester=new JLabel("Semester: ");
            mid.add(lsemester);
            semester = new JTextField();
            semester.setPreferredSize(new Dimension(200, 50));
            mid.add(semester);
            //Address
            JLabel lcredis=new JLabel("Credis: ");
            mid.add(lcredis);
            credis = new JTextField();
            credis.setPreferredSize(new Dimension(200, 50));
            mid.add(credis); 
            //Department
            jc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String t=(String) jc.getSelectedItem();
                    if(t.equals("ElectiveModule")){
                        departmentName=null;
                        if(departmentName==null){  
                            dpm=new JLabel("Department:");
                            departmentName = new JTextField();
                            mid.add(dpm);
                            mid.add(departmentName);
                            mid.validate();
                            //todo
                        }                       
                    }else{ 
                        if(departmentName!=null){
                            Component[] comps = mid.getComponents();
                            Component c;
                            for(int i=0;i<comps.length;i++){
                                c=comps[i];
                                if(c instanceof JLabel){
                                    mid.remove(dpm);
                                    mid.remove(departmentName);
                                    mid.validate();
                                }
                            }
                        }   
                    }
                }
            });
            this.gui.add(mid,BorderLayout.CENTER);
            gui.validate();
        }
    }

    @Override
    /**
     * @effect
     *  get all input data from user
     *  if validate data true
     *      get class dmct of domainconstraint
     *      get class m of Module
     *      get all field  f of student
     *      foreach (Field f1 : f)
     *          get annotation of f1
     *          use method validate to cast f1 to true type
     *      use method newInstance to create new Module
     *  
     */
    public Object createObject() throws NotPossibleException {
        //get data from user
        String n=name.getText();
        String s=semester.getText();
        String c= credis.getText();
        String type=(String) jc.getSelectedItem();
        String department=""; 
        String message="";
        if(n.equals("")||s.equals("")||c.equals("")){
            message="Please enter all information";
            displayErrorMessage(message,"Create Module");
        }else{
            if(type.equals("ElectiveModule")){  
                department=departmentName.getText();
                if(department.equals("")){          
                    message="Please enter all information\n";
                }else{
                    if(validateDepartment(department)==false){
                        message="Wrong input value : department = "+department;
                    }
                }
            }
            boolean validate1=true;
            validate1=validateSemester(s);
            if(validate1==false){
                message=message+"Wrong input value : semester = "+s;
            }
            boolean validate2=true;
            validate2=validateCredis(c);
            if(validate2==false){
                message=message+"\nWrong input value : credis = "+c;
            }
            if(message!=""){
                displayErrorMessage(message,"Create Module");
            }else{
                String[] textField;
                Object[] o;
                Class cl;
                cl=ElectiveModule.class;
                Class dmct=DomainConstraint.class;
                Class m=Module.class;
                if(type.equals("ElectiveModule")){  
                    department=departmentName.getText();
                    String [] t={"name","semester","credis","departmentName"};
                    textField=t;
                    Object[]oj= {n,s,c,department};
                    o=oj;
                }else{ 
                    String [] t={"name","semester","credis"};
                    textField=t;
                    Object[] oj= {n,s,c};
                    o=oj;
                } 
                //get all field in class Module
                Field[] f =m.getDeclaredFields(); 
                boolean check=true;
                int i=0;
                String errorMessage="";
                for (Field f1 : f) {  
                    String temp=f1.getName();              
                    for(int j=0;j<textField.length;j++){                        
                        if(temp.equals(textField[j])){
                            //get annotation of this field
                            DomainConstraint dc= (DomainConstraint) f1.getAnnotation(dmct);
                            if(dc!=null&&i<o.length){
                                try{
                                    Object ob =validate(dc,o[j]);
                                    o[j]=ob;
                                }catch(NotPossibleException ne){
                                    System.err.println("Attribute: " + temp + "\n\tvalue: " + o[j]
                                  + " -> " + ne.getMessage());                                    
                                    errorMessage=errorMessage+"\nWrong input "+temp+"="+ne.getMessage();
                                    check=false;
                                }
                                
                            }  
                        }
                    }   
                }   
                if(errorMessage!=""){
                    displayErrorMessage(errorMessage,"Create Student");
                }
                if(check){ 
                    if(type.equals("ElectiveModule")){
                        for(int k=0;k<o.length;k++){
                            System.out.println(o[k]);
                        }
                        ElectiveModule mo =(ElectiveModule) (Module) newInstance(cl,o);
                        objects.add(mo);
                        displayMessage("Created ElectiveModule ("+mo.getCode()+","+mo.getName()+","+mo.getCredis()+","
                        +mo.getSemester()+","+mo.getDepartmentName()+")", "Create a module");
                        return mo;
                    }else{
                        Module mo =(Module) newInstance(m,o);
                        objects.add(mo);
                        displayMessage("Created CompulsoryModule ("+mo.getCode()+","+mo.getName()+","+mo.getCredis()+","
                        +mo.getSemester()+")", "Create a module");
                        return mo;
                    } 
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
     *  Save all necessary information of Module which in Vector objects to file module.dat
     */
    public void save() {
        if(this.objects!=null){
            File f = new File("modules.dat");
            try {
                FileOutputStream out = new FileOutputStream(f);
                ObjectOutputStream  op = new ObjectOutputStream(out);
                for(int i=0 ; i<objects.size();i++){
                    Module s = (Module) objects.get(i);
                    op.writeObject(s);
                }
                System.out.println("saved modules");
                op.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    /**
     * @effect
     *      use all information in file module.dat to create a object of Class Module
     *      add this object to Vector objects
     */
    public void startUp() {
        if(this.objects!=null){
            File f = new File("modules.dat");
            try {
                FileInputStream in = new FileInputStream(f);
                ObjectInputStream ip = new ObjectInputStream(in);
                while(true){
                    Module m = (Module) ip.readObject();
                    objects.add(m);
                }
            } catch (FileNotFoundException ex) {
                System.err.println("File modules.dat is not found.If it is the first"
                        + "time you run program, Please ignore it");
            } catch (IOException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }catch (ClassNotFoundException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Load Module Complete");  
        }
            
    }
    @Override
    /**
     * @effect
     *  <link> clearGUI(this.mid)
     */
    public void clearGUI() {
        clearGUI(this.mid);
    }
    /**
     * @effect
     *  if s is valid
     *      return true
     *  else
     *      return false
     * @param s
     * @return 
     */
    public boolean validateSemester(String s){
        if(s.matches("^[0-9]+$")){
            return true;
        }
        return false;
    }
    /**
     * @effect
     *  if c is valid
     *      return true
     *  else
     *      return false
     * @param c
     * @return 
     */
    public boolean validateCredis(String c){
        if(c.matches("^[0-9]+$")){
            return true;
        }
        return false;
    }
    /**
     * @effect
     *  if department is valid
     *      return true
     *  else
     *      return false
     * @param department
     * @return 
     */
    public boolean validateDepartment(String department){
        if(department.matches("^([a-z]|[A-Z])+$")){
            return true;
        }
        return false;
    }
}
