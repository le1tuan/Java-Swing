/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @overview
 * Student is people study at university
 * @attributes
 *  id String
 *  name String
 *  dob String
 *  address String
 *  email String
 * @object
 *  A typical Students is s=<i,n,d,a,e> where id(i),name(n),dob(d),address(a),email(e)
 * @abstract_properties
 *  mutable(id)=false/\optional(id)=false
 *  mutable(name)=true/\optional(name)=false
 *  mutable(dob)=true/\optional(dob)=false
 *  mutable(address)=true/\optional(address)=false
 *  mutable(email)=true/\optional(email)=false
 */
package courseman2.model;

import courseman2.DomainConstraint;
import courseman2.DomainConstraint.Type;
import static courseman2.DomainConstraint.Type.String;
import courseman2.NotPossibleException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Le Tuan Anh
 */
public class Student implements Comparable<Student>,Serializable{
    @DomainConstraint(type=String, optional=false,mutable=false,length =20)
    private String id;
    @DomainConstraint(type=String, optional=false,mutable=true,length =50)
    private String name;
    @DomainConstraint(type=String, optional=false,mutable=true,length =50)
    private String dob;
    @DomainConstraint(type=String, optional=false,mutable=true,length =50)
    private String address;
    @DomainConstraint(type=String, optional=false,mutable=true,length =50)
    private String email; 
    private static int year=Calendar.getInstance().get(Calendar.YEAR);
    /**
     * @effect
     *  year++;
     *  return year;
     * @return 
     */ 
    public int getYear(){
        return this.year++;
    }
    /**
     * @effect
     *      if name,dob,address,email are valid
     *          initialise this as Students<name,dob,address,email>
     *      else
     *          throw new NotPossibleException
     * 
     * @param name
     * @param dob
     * @param address
     * @param email
     * @throws NotPossibleException
     */
    public Student(String name,String dob,String address,String email) throws NotPossibleException{
        if(validate(name,dob,address,email)){
            if(id==null){
              this.id="S"+getYear();
              this.name=name;
              this.dob=dob;
              this.address=address;
              this.email=email;  
            }
            
        }else{
            throw new NotPossibleException("cant initialise");
        }
        
    }
    
    /**
     * @effect
     *  return this.id;
     * @return 
     */
    public String getId(){
        return this.id;
    }
    /**
     * @effect
     *  return this.name;
     * @return 
     */
    public String getName() {
        return name;
    }
    /**
     * @effect
     *  if name is valid
     *      this.name=name
     *  else
     *      throw new NotPossibleException
     * @param name
     * @throws NotPossibleException
     */
    public void setName(String name)throws NotPossibleException {
        if(validateName(name)){
             this.name = name;
        }else{
            throw new NotPossibleException("cant set name");
        }
       
    }
    /**
     * @effect
     *  return this.dob
     * @return 
     */
    public String getDob() {
        return dob;
    }
    /**
     * if dob is valid
     *  return this.dob;
     * else
     *  throw new NotPossibleException
     * @param dob
     * @throws NotPossibleException 
     */
    public void setDob(String dob)throws NotPossibleException{
        if(validateDob(dob)){
             this.name = dob;
        }else{
            throw new NotPossibleException("cant set DOB");
        }
    }
    /**
     * @effect
     *  return this.address
     * @return 
     */
    public String getAddress() {
        return address;
    }
    /**
     * @effect
     *  if address is valid
     *      this.address=address
     *  else
     *      throw new NotPossibleException
     * @param address
     * @throws NotPossibleException
     */
    public void setAddress(String address)throws NotPossibleException {
        if(validateAddr(address)){
             this.address = address;
        }else{
            throw new NotPossibleException("cant set Address");
        }
    }
    /**
     * @effect
     *  return this.email
     * @return 
     */
    public String getEmail() {
        return email;
    }
    /**
     * @effect
     *  if email is valid
     *      this.email=email
     *  else
     *      throw new NotPossibleException
     * @param email
     * @throws NotPossibleException
     */
    public void setEmail(String email)throws NotPossibleException {
        if(validateEmail(email)){
             this.email = email;
        }else{
            throw new NotPossibleException("cant set email");
        }
    }
    @Override
    public int compareTo(Student o){
        if(this.id.compareTo(o.id)>0){
            return 1;
        }else{
            return 0;
        }
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
    * if d is valid
    *   return true
    * else 
    *   return false
    * @param d
    * @return 
    */
    private boolean validateDob(String d){
        if(d==null||d.length()==0)
            return false;
        else
            return true;
    }
    /**
     * if a is address
     *      return true
     * else
     *      return false
     * @param a
     * @return 
     */
    private boolean validateAddr(String a){
        if(a==null||a.length()==0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * @effect
     *  if e is valid
     *      return true
     *  else 
     *      return false
     * @param e
     * @return 
     */
    private boolean validateEmail(String e){
        if(e==null||e.length()==0)
            return false;
        else
            return true;
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
    private boolean validate(String n,String d,String a,String e){
        return validateName(n)&&validateDob(d)&&validateAddr(a)&&validateEmail(e);
    }
    @Override
    public String toString() {
        return "Students{" + "id=" + id + ", name=" + name + ", dob=" + dob + ", address=" + address + ", email=" + email + '}';
    }
}
