/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import groovy.lang.Script;
import hudson.Extension;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.plugins.pivot.Measure;
import jenkins.plugins.pivot.impl.GroovyScriptCompiler;
import jenkins.plugins.pivot.impl.GroovyScriptMeasure;
import jenkins.plugins.pivot.impl.GroovyShellScript;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Groovy based measure defintion.
 * @author zijiang
 */
public class PivotMeasureUseGroovy extends PivotMeasureDefinition{
    private final String name;
    private final String description;
    private final GroovyShellScript script;
    private transient Script compiledScript;
    
    @DataBoundConstructor
    public PivotMeasureUseGroovy(String name, String description, GroovyShellScript script){
        this.name = name;
        this.description = description;
        this.script = script;
        compileScript();
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public GroovyShellScript getScript(){
        return this.script;
    }
     
    @Override
    public Measure getMeasure() {
        return new GroovyScriptMeasure(this.compiledScript);
    }
    
    private Object readResolve() {
        compileScript();
        return this;
    }
    private void compileScript(){
        this.compiledScript = GroovyScriptCompiler.parseScript(script.getContent());
    }    
    
    @Extension
    public static class DescriptorImpl extends PivotMeasureDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Groovy based measure";
        }
        
        public Descriptor getScriptDescriptor(){
            return Jenkins.getInstance().getDescriptor(GroovyShellScript.class);
        }
    }
    
}
