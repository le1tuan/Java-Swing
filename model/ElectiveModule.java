/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.model;

import courseman2.DomainConstraint;
import courseman2.DomainConstraint.Type;
import static courseman2.DomainConstraint.Type.String;
import courseman2.NotPossibleException;

/**
 * @overview
 *  ElectiveModules are sub class of Modules which module has one more attribute. It 
 *  is department
 * @attributes
 *  departmentName String
 * @abstract_properties
 *  Modules/\
 *  mutable(departmentName)=true/\optional(departmentName)=false
 */
public class ElectiveModule extends Module{
    @DomainConstraint(type=Type.String, optional=false,mutable=true,length = 30)
    private String departmentName; 
/**
   * @effects <pre>
   *            if name, semester,credis,departmentName are valid
   *              initialise this as ElectiveModules:<name,semester,credis,departmentName>
   *            else
   *              throw NullPointer
   *          </pre>
   */    
    public ElectiveModule(String name, Integer semester, Integer credis,String departmentName) throws NotPossibleException {
        
        super(name, semester, credis);
        if(validaDepartment(departmentName)){
            this.departmentName=departmentName;
        }else{
            throw new NotPossibleException("invalid departmentname");
        }
    }
/**
* @effects 
*  return a departmentName
*/    
    public String getDepartmentName() {
        return departmentName;
    }
/**
 * @effects
 * if dapartmentName is valid
 *      this.departmentName=departmentName;
 * else
 *      throw new NotPossibleException("invalid departmentname");
 * @param departmentName
 * @throws NotPossibleException 
 */
    public void setDepartmentName(String departmentName) throws NotPossibleException {
        if(validaDepartment(departmentName)){
            this.departmentName = departmentName;
        }else{
            throw new NotPossibleException("invalid departmentname");
        }
        
    }
    /**
     * @requirements
     *  String departmentName
     * @effects
     *  if departmentName is valid
     *      return true
     * else 
     *      return false
     * @param departmentName
     * @return 
     */
    public boolean validaDepartment(String departmentName){
        if(departmentName==null||departmentName.length()<=0){
            return false;
        }else{
            return true;
        }
    }
   @Override
    public String toString() {
        return "ElectiveModules{" + "code=" + this.getCode() + ", name=" + this.getName() + ", semester=" + this.getSemester() + ", credis=" + this.getCredis() + ", department="+this.getDepartmentName()+'}';
    }
    
    
}
