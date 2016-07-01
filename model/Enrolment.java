/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.model;

import courseman2.DomainConstraint;
import static courseman2.DomainConstraint.Type.Float;
import static courseman2.DomainConstraint.Type.Object;
import static courseman2.DomainConstraint.Type.String;
import courseman2.NotPossibleException;
import courseman2.model.Module;
import courseman2.model.Student;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Le Tuan Anh
 * @overview
 * @attributes
 *  student Students
 *  module Modules
 *  internalMark float
 *  examinationMark float
 *  finalGrade String
 * @object
 *  A typical Enrolments is e=<s,m,i,e> where student(s),module(m),internalMark(i),examinationMark(e)
 * @abstract_properties
 *  mutable(student)=true/\optional(student)=false
 *  mutable(module)=true/\optional(module)=false
 *  mutable(internalMark)=true/\optional(internalMark)=false
 *  mutable(examinationMark)=true/\optional(address)=false
 *  mutable(finalGrade)=false/\optional(finalGrade)=false
 */
public class Enrolment implements Serializable {
    @DomainConstraint(type=String, optional=false,mutable=true,length=30)
    private String studentId;
    @DomainConstraint(type=String, optional=false,mutable=true,length=30)
    private String moduleId;
    @DomainConstraint(type=Float, optional=false,mutable=true,min=0,max=10)
    private float internalMark;
    @DomainConstraint(type=Float, optional=false,mutable=true,min=0,max=10)
    private float examinationMark;
    @DomainConstraint(type=String, optional=false,mutable=true)
    private String finalGrade;   
    /**
     * 
     * @param student
     * @param modules
     * @param inter
     * @param exam 
     * @effects
     * if student,module,inter,exam are valid
     *      initialise this as Enrolment<student,module,inter,exam>
     * else
     *      throw NotPossibleException("invalid value")
     */
    public Enrolment(String studentId,String moduleId,Float inter,Float exam) throws NotPossibleException{
        if(validate(studentId, moduleId, inter, exam)){
            this.studentId=studentId;
            this.moduleId=moduleId;
            this.internalMark=inter;
            this.examinationMark=exam;
            calcFinalGrade();
        }else{
            throw new NotPossibleException("invalid value");
        }
        
    }
    /**
     * @effect
     * int result= 0.4*this.internalMark+0.6*this.examinationMark
     * if(result<5)
     *     this.finalGrade= F
     *  else if(result>=5&&result<7)
     *     this.finalGrade= P
     *  else if(result==7||result==8)
     *     this.finalGrade=G
     *  else if(result>=9)
     *     this.finalGrade= E
     * 
     */
    public void calcFinalGrade(){
        int result= (int) (0.4*this.internalMark+0.6*this.examinationMark);
        if(result<5){
            this.finalGrade= "F";
        }else if(result>=5&&result<7){
            this.finalGrade= "P";
        }else if(result==7||result==8){
            this.finalGrade="G";
        }else if(result>=9){
            this.finalGrade= "E";
        }
    }

    @Override
    public String toString() {
        return "Enrolments{" + "student=" + studentId + ", module=" + moduleId + ", internalMark=" + internalMark + ", examinationMark=" + examinationMark + ", finalGrade=" + finalGrade + '}';
    }
    /**
     * @effect
     *  return this.student
     * @return 
     */
    public String getStudentId() {
        return this.studentId;
    }
    /**
     * @effect
     *  if student is valid
     *      this.student=student
     *  else
     *      throw new NotPossibleException("invalid student");
     * @param student
     * @throws NotPossibleException 
     */
    public void setStudentId(String studentId) throws NotPossibleException {
        if(validateStudentId(studentId)){
            this.studentId = studentId;
        }else{
            throw new NotPossibleException("invalid student");
        }
        
    }
    /**
     * @effect
     *  return this.module
     * @return 
     */
    public String getModuleId() {
        return this.moduleId;
    }
    /**
     * @effect
     *  if module is valid
     *      this.module=module
     *  else
     *      throw new NotPossibleException("invalid module");
     * @param module
     * @throws NotPossibleException 
     */
    public void setModuleId(String module) throws NotPossibleException {
        if(validateModuleId(module)){
            this.moduleId = module;
        }
        
    }
    /**
     * @effect
     *  return this.internalMark
     * @return 
     */
    public float getInternalMark() {
        return internalMark;
    }
    /**
     * @effect
     *  if internalMark is valid
     *      this.internalMark=internalMark
     *  else
     *      throw new NotPossibleException("invalid internalMark");
     * @param internalMark
     * @throws NotPossibleException 
     */
    public void setInternalMark(float internalMark) throws NotPossibleException {
        if(validateInternal(internalMark)){
            this.internalMark = internalMark;
            calcFinalGrade();
        }else{
            throw new NotPossibleException("invalid internalMark");
        }
        
    }
    /**
     * @effect
     *  return this.examinationMark
     * @return 
     */
    public float getExaminationMark() {
        return examinationMark;
    }
     /**
     * @effect
     *  if examinationMark is valid
     *      this.examinationMark=examinationMark
     *  else
     *      throw new NotPossibleException("invalid examinationMark");
     * @param examinationMark
     * @throws NotPossibleException 
     */
    public void setExaminationMark(float examinationMark) throws NotPossibleException {
        if(validateExam(examinationMark)){
            this.examinationMark = examinationMark;
            calcFinalGrade();
        }else{
            throw new NotPossibleException("invalid examinationMark");
        }
        
    }
    /**
     * @effect
     *  return this.finalGrade
     * @return 
     */
    public String getFinalGrade() {
        return finalGrade;
    }
    /**
     * @effects
     *  if s is valid
     *      return true
     *  else
     *      return false
     * @param s
     * @return 
     */
    public boolean validateStudentId(String id){
        if(id==null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * @effects
     *  if m is valid
     *      return true
     *  else
     *      return false
     * @param m
     * @return 
     */
    public boolean validateModuleId(String m){
       if(m==null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * @effects
     *  if i is valid
     *      return true
     *  else
     *      return false
     * @param i
     * @return 
     */
    public boolean validateInternal(float i){
        if(i<0||i>10){
            return false;
        }else{
            return true;
        }
    }
    /**
     * @effects
     *  if e is valid
     *      return true
     *  else
     *      return false
     * @param e
     * @return 
     */
    public boolean validateExam(float e){
        if(e<0||e>10){
            return false;
        }else{
            return true;
        }
    }
    /**
     * @effects
     *  if s,m,i,e arevalid
     *      return true
     *  else
     *      return false
     * @param s,m,i,e
     * @return 
     */
    public boolean validate(String s,String m, float i,float e){
        return validateStudentId(s)&&validateModuleId(m)&&validateInternal(i)&&validateExam(e);
    }   
    
}
