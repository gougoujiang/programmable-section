/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import groovy.lang.Binding;
import groovy.lang.Script;
import java.util.Map;

/**
 *
 * @author zijiang
 */
public class GroovyScriptResolver {
    
    private final Script script;
    
    public GroovyScriptResolver(Script script){
        this.script = script;
    }
    
    public Script getScript(){
        return this.script;
    }
    
    public Object evaluate(Map<String, Object> variables){
        return this.doEval(this.script, variables);
    }
    
    protected Object doEval(Script script, Map<String, Object> variables){
        if(script == null){
            return null;
        }
        script.setBinding(new Binding(variables));
        return script.run();
    }
}
