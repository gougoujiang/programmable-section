/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import groovy.lang.Binding;
import groovy.lang.Script;
import java.util.HashMap;
import java.util.Map;
import jenkins.plugins.pivot.PivotResult;

/**
 *
 * @author zijiang
 */
public abstract class PivotMeasurePresenterGroovySupport extends PivotMeasurePresenter{
    
    protected Object eval(Script script, PivotResult result){
        Map<String, Object> var = new HashMap();
        var.put("result", result);

        if(script == null){
            return null;
        }
        script.setBinding(new Binding(var));
        return script.run();
    }
    
}
