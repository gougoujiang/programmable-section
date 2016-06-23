/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import jenkins.plugins.pivot.DataItemProvider;
import groovy.lang.Script;
import hudson.model.TopLevelItem;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zijiang
 */
public class GroovyScriptDataItemProvider extends GroovyScriptResolver implements DataItemProvider{
    private final Collection<TopLevelItem> jobs;
    
    public GroovyScriptDataItemProvider(Script script, Collection<TopLevelItem> jobs){
        super(script);
        this.jobs = jobs;
    }
    
    public Collection<TopLevelItem> getBuilds(){
        return this.jobs;
    }

    @Override
    public List<Object> getDataItems() {
        Map<String, Object> vars = new HashMap();
        vars.put("jobs", this.jobs);
        Object res = this.evaluate(vars);
        return (List<Object>) res;
    }
    
}
