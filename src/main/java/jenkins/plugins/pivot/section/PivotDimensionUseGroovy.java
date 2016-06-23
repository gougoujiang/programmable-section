/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import groovy.lang.Script;
import hudson.Extension;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.plugins.pivot.Dimension;
import jenkins.plugins.pivot.impl.GroovyScriptCompiler;
import jenkins.plugins.pivot.impl.GroovyScriptDimension;
import jenkins.plugins.pivot.impl.GroovyShellScript;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Groovy based pivot dimension definition which uses groovy 
 * script to do calculation.
 * @author zijiang
 */
public class PivotDimensionUseGroovy extends PivotDimensionDefinition{
    private final String name;
    private final GroovyShellScript script;
    private final GroovyShellScript sorter;
    
    private transient Script compiledScript;
    private transient Script compiledSorter;

    @DataBoundConstructor
    public PivotDimensionUseGroovy(String name, GroovyShellScript script, GroovyShellScript sorter){
        this.name = name;
        this.script = script;
        this.sorter = sorter;    
        
        compileScript();
    }
    
    public String getName(){
        return this.name;
    }
    
    public GroovyShellScript getScript(){
        return this.script;
    }
    
    public GroovyShellScript getSorter(){
        return this.sorter;
    }
    
    private Object readResolve() {
        compileScript();
        return this;
    }
    
    private void compileScript(){
        this.compiledScript = GroovyScriptCompiler.parseScript(script.getContent());
        this.compiledSorter = GroovyScriptCompiler.parseScript(sorter.getContent());
    }
    
    @Override
    public Dimension getDimension() {
        return new GroovyScriptDimension(this.name, this.compiledScript, this.compiledSorter);
    }
    
    @Extension
    public static class DescriptorImpl extends PivotDimensionDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Groovy based dimension";
        }
        
        public Descriptor getScriptDescriptor(){
            return Jenkins.getInstance().getDescriptor(GroovyShellScript.class);
        }
    }
}
