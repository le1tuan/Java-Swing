/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseman2.model;

import courseman2.NotPossibleException;

/**
 *@overview
 *CompulsoryModules are sub class of Modules.It has the same attributes with
 *Modules class
 *@attributes
 *  
 *@abstract_properties
 *  Modules
 * @author Le Tuan Anh
 */
public class CompulsoryModule extends Module{
    /**
     * if name, semester , credis are valid
     *      initialise this as CompulsoryModules:<name,semester,credis>
     * @param name
     * @param semester
     * @param credis 
     */
    public CompulsoryModule(String name,int semester,int credis) throws NotPossibleException{
        super(name,semester,credis);
              
    }
    @Override
    public String toString() {
        return "CompulsoryModules{" + "code=" + this.getCode() + ", name=" + this.getName() + ", semester=" + this.getSemester() + ", credis=" + this.getCredis() + '}';
    }
}
