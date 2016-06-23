/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import jenkins.plugins.pivot.Measure;
import groovy.lang.Script;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zijiang
 */
public class GroovyScriptMeasure extends GroovyScriptResolver implements Measure{

    public GroovyScriptMeasure(Script script) {
        super(script);
    }

    @Override
    public Object getValue(List<Object> items) {
        Map<String, Object> vars = new HashMap();
        vars.put("items", items);
        return this.evaluate(vars);
    }
    
}
