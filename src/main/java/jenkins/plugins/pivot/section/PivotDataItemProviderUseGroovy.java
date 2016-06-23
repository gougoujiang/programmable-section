/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import groovy.lang.Script;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import java.util.Collection;
import jenkins.model.Jenkins;
import jenkins.plugins.pivot.DataItemProvider;
import jenkins.plugins.pivot.impl.GroovyScriptCompiler;
import jenkins.plugins.pivot.impl.GroovyScriptDataItemProvider;
import jenkins.plugins.pivot.impl.GroovyShellScript;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author zijiang
 */
public class PivotDataItemProviderUseGroovy extends PivotDataItemProviderDefinition{
    private final GroovyShellScript script;
    private transient ProgrammablePivotViewSection owner;
    private transient Script compiledScript;
    
    @DataBoundConstructor
    public PivotDataItemProviderUseGroovy(GroovyShellScript script){
        this.script = script;
        compileScript();
    }
    
    public GroovyShellScript getScript(){
        return this.script;
    }
    
    public void setOwner(ProgrammablePivotViewSection owner){
        this.owner = owner;
    }
    
    private Object readResolve() {
        compileScript();
        return this;
    }
    
    private void compileScript(){
        this.compiledScript = GroovyScriptCompiler.parseScript(script.getContent());
    }
    
    @Override
    public DataItemProvider getDataItemProvider(Collection<TopLevelItem> jobs) {
        return new GroovyScriptDataItemProvider(this.compiledScript, jobs);
    }
    
    @Extension
    public static class DescriptorImpl extends PivotDataItemProviderDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Groovy based provider";
        }
        
        public Descriptor getScriptDescriptor(){
            return Jenkins.getInstance().getDescriptor(GroovyShellScript.class);
        }
        
    }
    
}
