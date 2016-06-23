/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import jenkins.plugins.pivot.impl.GroovyScriptCompiler;
import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.render.Presenter;
import jenkins.plugins.pivot.render.jelly.StatusBall;
import jenkins.plugins.pivot.render.jelly.StatusBallList;
import jenkins.plugins.pivot.util.StringUtil;
import groovy.lang.Script;
import hudson.Extension;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author zijiang
 */
public class PivotMeasurePresenterStatusBallList extends PivotMeasurePresenterGroovySupport{
    
    private final String script;
    private transient Script compiledScript;

    
    @DataBoundConstructor
    public PivotMeasurePresenterStatusBallList(String script){
        this.script = script;
        compileScript();
    }
    
    public String getScript(){
        return this.script;
    }
    
    private void compileScript(){
        this.compiledScript = GroovyScriptCompiler.parseScript(this.script);
    }
    
    private Object readResolve() {
        compileScript();
        return this;
    }
    
    @Override
    public Presenter createStyle(PivotResult result) {
        return new StatusBallList(getStatus(result));
    }
    
    private List<StatusBall> getStatus(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.script)){
            return null;
        }
        Object o= eval(this.compiledScript, result);
        if(o == null){
            return null;
        }
        if(o instanceof StatusBall){
            List<StatusBall> list = new ArrayList();
            list.add((StatusBall)o);
            return list;
        }
        return (List<StatusBall>)o;
    }
    
    @Extension
    public static class DescriptorImpl extends PivotMeasureStyleDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Status Ball List";
        }
        
    }
}
