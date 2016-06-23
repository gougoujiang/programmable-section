/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot;

import java.util.List;

/**
 * The measure calculation result.
 * @author zijiang
 */
public class PivotResult {
    private Object value;
    private List<Object> items;
    
    public PivotResult(Object value, List<Object> items){
        this.value = value;
        this.items = items;
    }
    
    public Object getValue(){
        return this.value;
    }
    
    public List<Object> getItems(){
        return this.items;
    }
    
    public boolean isEmpty(){
        return this.items.isEmpty();
    }
}
