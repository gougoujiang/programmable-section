/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;


import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author tadu
 */
public class GroovyShellScript implements Describable<GroovyShellScript> {
    private final String content;
    
    @DataBoundConstructor
    public GroovyShellScript(String content) {
        this.content = content;
    }
    
    public String getContent(){
        return this.content;
    }

    @Override
    public Descriptor<GroovyShellScript> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }


    @Extension
    public static class DescriptorImpl extends Descriptor<GroovyShellScript> {

        @Override
        public String getDisplayName() {
            return "Groovy Shell Script";
        }
    }
}
