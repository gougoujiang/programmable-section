/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import jenkins.plugins.pivot.Dimension;
import groovy.lang.Script;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zijiang
 */
public class GroovyScriptDimension extends GroovyScriptResolver implements Dimension{
    private final String name;
    private final Script sorter;
    
    public GroovyScriptDimension(String name, Script script, Script sorter){
        super(script);
        this.name = name;
        this.sorter = sorter;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getValue(Object item) {
        Map<String, Object> vars = new HashMap();
        vars.put("item", item);
        return this.evaluate(vars);
    }

    @Override
    public List<Object> sortValues(List<Object> dimValues) {
        // Leave as natural order
        if(this.sorter == null){
            return dimValues;
        }
        
        //Use user defined script to sort
        Map<String, Object> vars = new HashMap();
        vars.put("dimValues", dimValues);
        Object sorted = this.doEval(this.sorter, vars);
        return (List<Object>)sorted;
    }

}
