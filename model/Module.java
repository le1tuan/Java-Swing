/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.model;

import courseman2.DomainConstraint;
import static courseman2.DomainConstraint.Type.Integer;
import static courseman2.DomainConstraint.Type.String;
import courseman2.NotPossibleException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Le Tuan Anh
 * @overview
 * Module is course in university
 * @attributes
 *  code String
 *  name String
 *  semester Integer
 *  credis Integer
 * @object
 *  A typical Modules is s=<c,n,s,r> where code(c),name(n),semester(s),credis(r)
 * @abstract_properties
 *  mutable(code)=false/\optional(code)=false
 *  mutable(name)=true/\optional(name)=false
 *  mutable(semester)=true/\optional(dob)=false
 *  mutable(credis)=true/\optional(address)=false
 */
public class Module implements Serializable{
    @DomainConstraint(type=String, optional=false,mutable=false)
    protected String code;
    @DomainConstraint(type=String, optional=false,mutable=true,length = 50)
    private String name;
    @DomainConstraint(type=Integer, optional=false,mutable=true,min=1,max=9)
    private int semester;
    @DomainConstraint(type=Integer, optional=false,mutable=true,min=5,max=20)
    private int credis;
    private static int [] storage = new int[1000];
    /**
     * @effect
     *      if name,semester,credis are valid
     *          initialise this as Modules<code,name,semester,credis>
     *      else
     *          throw new NullPointerException
     * @throws NullPointerException 
     */
    public Module(String name,Integer semester,Integer credis) throws NotPossibleException{
        if(validate(name,semester,credis)){
            this.name=name;
            this.semester=semester;
            this.credis=credis;
            int a=storage[semester]+1;
            storage[semester]=a;
            this.code="M"+((this.semester*100)+a);  
        }else{
            throw new NotPossibleException("invalid value");
        }
    }  
    /**
     * @effect
     *  return this.code;
     * @return 
     */
    public String getCode() {
        return this.code;
    }
    /**
     * @effect
     *  return this.name;
     * @return 
     */
    public String getName() {
        return this.name;
    }
    /**
     * @effect
     *  if name is valid
     *      this.name=name
     *  else
     *      throw new NullPointerException
     * @param name
     * @throws NullPointerException 
     */
    public void setName(String name) throws NotPossibleException{
        if(validateName(name)){
            this.name = name;
        }else{
            throw new NotPossibleException("invalid Name");
        }
        
    }
    /**
     * @effect
     *  return this.semester;
     * @return 
     */
    public int getSemester() {
        return this.semester;
    }
    /**
     * @effect
     *  if semester is valid
     *      this.semester=semester
     *  else
     *      throw new NullPointerException
     * @param semester
     * @throws NullPointerException 
     */
    public void setSemester(int semester) throws NotPossibleException{
        if(validateSemester(semester)){
            this.semester = semester;
        }else{
            throw new NotPossibleException("invalid Semester");
        }
    }
    /**
     * @effect
     *  return this.credis;
     * @return 
     */
    public int getCredis() {
        return this.credis;
    }
    /**
     * @effect
     *  if credis is valid
     *      this.credis=credis
     *  else
     *      throw new NullPointerException
     * @param credis
     * @throws NullPointerException 
     */
    public void setCredis(int credis)throws NotPossibleException {
        if(validateCredis(credis)){
            this.credis = credis;
        }else{
            throw new NotPossibleException("invalid Credis");
        }
        
    }
    @Override
    public String toString() {
        return "Modules{" + "code=" + code + ", name=" + name + ", semester=" + semester + ", credis=" + credis + '}';
    }
     /**
     * @effect
     *  if name is valid
     *      return true
     *  else
     *      return false
     * @param n
     * @return 
     */
    private boolean validateName(String n){
        if(n==null||n.length()==0)
            return false;
        else
            return true;
    }
   /**
    * if s is valid
    *   return true
    * else 
    *   return false
    * @param s
    * @return 
    */
    private boolean validateSemester(int s){
        if(s<=0||s>9)
            return false;
        else
            return true;
    }
    /**
     * if c is address
     *      return true
     * else
     *      return false
     * @param c
     * @return 
     */
    private boolean validateCredis(int c){
        if(c<=0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * @effect
     * if(n,d,a,e) are valid
     *  return true
     * else 
     *  return false
     * @param n
     * @param d
     * @param a
     * @param e
     * @return 
     */
    private boolean validate(String n,int s,int c){
        return validateName(n)&&validateSemester(s)&&validateCredis(c);
    }
}
