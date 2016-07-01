/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.controller;

import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import courseman2.DomainConstraint;
import courseman2.NotPossibleException;
import static courseman2.controller.Manager.validate;
import courseman2.model.ElectiveModule;
import courseman2.model.Enrolment;
import courseman2.model.Module;
import courseman2.model.Student;
import courseman2.view.EasyTable;
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
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *@overview EnrolmentManager is a program allowed user can create a new Enrolment
 *@attribute
 *  stu,mo,inter,exam JTextField
 *  mid JPanel
 *  sman StudentManager
 *  mman ModuleManager
 * @author Le Tuan Anh
 */
public class EnrolmentManager extends Manager {
    private JTextField stu,mo,inter,exam; 
    private JPanel mid;
    private StudentManager sman;
    private ModuleManager mman;
    /**
     * constructor method
     * @effect
     *      intialize Enrolmentmanager eman <title, titleText,width,height,x,y,sman,mman>
     * @param title
     * @param titleText
     * @param width
     * @param height
     * @param x
     * @param y
     * @param sman
     * @param mman 
     */
    public EnrolmentManager(String title, String titleText, int width, int height, int x, int y,StudentManager sman,ModuleManager mman) {
        super(title, titleText, width, height, x, y);
        this.sman=sman;
        this.mman=mman;
    }
    @Override
    /**
     * @effect
     *      initialize mid as new JPanel
     *      set gridlayout for mid
     *      initialize label lStu
     *      add lStu to mid
     *      initialize textfield stu
     *      add stu to mid
     * 
     *      initialize label lsemester
     *      add lsemester to mid
     *      initialize textfield semester
     *      add semester to mid
     * 
     *      initialize label lMo
     *      add lMo to mid
     *      initialize textfield mo
     *      add mo to mid
     * 
     *      initialize label lInter
     *      add lInter to mid
     *      initialize textfield inter
     *      add inter to mid
     * 
     *      initialize label lExam 
     *      add lExam to mid
     *      initialize textfield exam
     *      add exam to mid
     */
    protected void createMiddlePanel() {
         if(this.gui!=null){
            mid = new JPanel();
            GridLayout grid = new GridLayout(0,2);
            mid.setLayout(grid);
            //Stu
            JLabel lStu=new JLabel("Student: ");
            mid.add(lStu);
            stu = new JTextField();
            stu.setPreferredSize(new Dimension(200, 50));
            mid.add(stu);
            //Module
            JLabel lMo=new JLabel("Module: ");
            mid.add(lMo);
            mo = new JTextField();
            mo.setPreferredSize(new Dimension(200, 50));
            mid.add(mo);
            //Inter
            JLabel lInter=new JLabel("InternalMark: ");
            mid.add(lInter);
            inter = new JTextField();
            inter.setPreferredSize(new Dimension(200, 50));
            mid.add(inter);
            //Exam
            JLabel lExam=new JLabel("ExaminationMark: ");
            mid.add(lExam);
            exam = new JTextField();
            exam.setPreferredSize(new Dimension(200, 50));
            mid.add(exam); 
            this.gui.add(mid,BorderLayout.CENTER);
        }
    }

    @Override
    /**
     * @effect
     *  get all input data from user
     *  if validate data true
     *      get class dmct of domainconstraint
     *      get class m of Enrolment
     *      get all field  f of student
     *      foreach (Field f1 : f)
     *          get annotation of f1
     *          use method validate to cast f1 to true type
     *      use method newInstance to create new Enrolment
     *  
     */
    public Object createObject() throws NotPossibleException {
        //get data
        String s = stu.getText();
        String mod=mo.getText();
        String it=inter.getText();
        String ex = exam.getText();
        String message="";
        if(s.equals("")||mod.equals("")||it.equals("")||ex.equals("")){
            message="Please enter all information";
            displayErrorMessage(message,"Create enrolment");
        }else{
            boolean validate1=true;
            boolean validate2=true;
            boolean validate3=true;
            //validate data
            validate1=validateStudent(s);
            if(validate1==false){
                message="Cant find studentId: "+s;
            }
            validate2=validateModule(mod);
            if(validate2==false){
                message=message+"\nCan't find moduleId: "+mod+"\n";
            }
            if(validate1==true&&validate2==true){
                validate3=validateEnrolment(s, mod);
                if(validate3==false){
                    message="Duplicate moduleId: "+mod+" and studentId "+s;
                }
            }
            if(!message.equals("")){
                displayErrorMessage(message,"Create enrolment");
            }else{
                Class dmct=DomainConstraint.class;
                Class m = Enrolment.class;
                //get all field in class Module
                Field[] f =m.getDeclaredFields();
                Object[] o = {s,mod,it,ex};
                String [] textField={"studentId","moduleId","internalMark","examinationMark"};
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
                                }

                            }  
                        }
                    }   
                }     
                if(errorMessage!=""){
                        displayErrorMessage(errorMessage,"Create Student");
                        throw new NotPossibleException(errorMessage);
                }else{
                        Enrolment enrol =(Enrolment) newInstance(m,o);
                        //display message to user when object is created
                        displayMessage("Created Enrolment("+enrol.getStudentId()+","+enrol.getModuleId()+","+enrol.getInternalMark()+","
                        +enrol.getExaminationMark()+","+enrol.getFinalGrade()+")", "Create a enrolment");
                        objects.add(enrol);
                        return enrol;
                } 
            }
        }
        return null;
    }        

    @Override
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
    public void save() {
        if(this.objects!=null){
            File f = new File("enrolments.dat");
            try {
                FileOutputStream out = new FileOutputStream(f);
                ObjectOutputStream  op = new ObjectOutputStream(out);
                for(int i=0 ; i<objects.size();i++){
                    Enrolment s = (Enrolment) objects.get(i);
                    op.writeObject(s);
                }
                System.out.println("saved enrolments");
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
     *      use all information in file module.dat to create a object of Class Enrolment
     *      add this object to Vector objects
     */
    public void startUp() {
            if(this.objects!=null){
            File f = new File("enrolments.dat");
            try {
                FileInputStream in = new FileInputStream(f);
                ObjectInputStream ip = new ObjectInputStream(in);
                while(true){
                    Enrolment e = (Enrolment) ip.readObject();
                    objects.add(e);
                }
            } catch (FileNotFoundException ex) {
                System.err.println("File enrolments.dat is not found.If it is the first"
                        + "time you run program, Please ignore it");
            } catch (IOException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }catch (ClassNotFoundException ex) {
                Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Load Enrolment Complete");  
        }
            
    }
    @Override
    /**
     * @effect
     * <link>clearGUI(this.mid)
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
     * @throws NullPointerException 
     */
    public boolean validateStudent(String s) throws NullPointerException{
            for(int i=0;i<sman.objects.size();i++){
                Student student = (Student) sman.objects.get(i);
                if(student.getId().equals(s)){
                    return true;
                }
            }
            return false;
    }
    /**
     * @effect
     *      if mod is valid
     *          return true
     *      else
     *          return false
     * @param mod
     * @return
     * @throws NullPointerException 
     */
    public boolean validateModule(String mod) throws NullPointerException{
            for(int i=0;i<mman.objects.size();i++){
                Module m = (Module) mman.objects.get(i);
                if(m.getCode().equals(mod)){
                   return true;
                }
            }        
            return false;
    }
    /**
     * @effect
     *      if s and mod are valid
     *          return true
     *      else
     *          return false
     * @param s
     * @param mod
     * @return 
     */
    public boolean validateEnrolment(String s, String mod){
        for(int i=0;i<this.objects.size();i++){
                Enrolment en = (Enrolment) this.objects.get(i);
                if(en.getModuleId().equals(mod)&&en.getStudentId().equals(s)){
                    return false;
                }
        
        }
        return true;
    }
    /**
     * @effect
     *      display assessedEnrolment table
     */
    public void reportAssessment(){
        JFrame gui = new JFrame("List of the assessed Enrolments");
        gui.setSize(300, 300);
        String [] head ={"No","Student Id","Module Code","Internal mark","Exam mark","Final Grade"};
        List dataRow = new ArrayList();
        for(int i=0;i<this.objects.size();i++){
            List data = new ArrayList();
            Enrolment e = (Enrolment) objects.get(i);
            int num=i+1;
            String sId=e.getStudentId();
            String mCode=e.getModuleId();
            float inter = e.getInternalMark();
            float exam =e.getExaminationMark();
            String finalGrade =e.getFinalGrade();
            String no= Integer.toString(num);
            data.add(no);
            data.add(sId);
            data.add(mCode);
            data.add(inter);
            data.add(exam);
            data.add(finalGrade);
            dataRow.add(data);
        }
        EasyTable table = new EasyTable(dataRow, head);
        JScrollPane scroll = new JScrollPane(table);
        gui.add(scroll);
        gui.setVisible(true);
    }
   /**
    * @effect
    *   display initialEnrolment table
    */
    public void report(){
        JFrame gui = new JFrame("List of the initial Enrolments");
        gui.setSize(300, 300);
        Vector head= new Vector();
        head.add("No");
        head.add("StudentId");
        head.add("StudentName");
        head.add("Module code");
        head.add("Module name");
        Vector dataRow = new Vector();
        for(int i=0;i<this.objects.size();i++){
            Vector data= new Vector();
            Enrolment e = (Enrolment) objects.get(i);
            int num=i+1;
            String sId=e.getStudentId();
            String sName="";
            for(int j=0;j<sman.objects.size();j++){
                Student s = (Student) sman.objects.get(j);
                if(s.getId().equals(sId)){
                    sName=s.getName();
                }
            }
            String mCode=e.getModuleId();
            String mName="";
            for(int j=0;j<mman.objects.size();j++){
                Module m = (Module) mman.objects.get(j);
                if(m.getCode().equals(mCode)){
                    mName=m.getName();
                }
            }
            String no= Integer.toString(num);
            data.add(no);
            data.add(sId);
            data.add(sName);
            data.add(mCode);
            data.add(mName);
            dataRow.add(data);
        }
        JTable table = new JTable(dataRow,head);
        JScrollPane scroll = new JScrollPane(table);
        gui.add(scroll);
        gui.setVisible(true);
    }
    
     
}
