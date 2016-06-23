/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zijiang
 */
public class DimValueList extends ArrayList{
    private final Dimension dim;
    
    public DimValueList(Dimension dim){
        this.dim = dim;
    }
    
    public Dimension getDim(){
        return this.dim;
    }
}
