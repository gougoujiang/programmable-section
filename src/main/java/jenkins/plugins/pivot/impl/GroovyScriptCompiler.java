/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import hudson.PluginWrapper;
import jenkins.model.Jenkins;

/**
 *
 * @author zijiang
 */
public class GroovyScriptCompiler {
    private static GroovyShell shell;
    static {
        PluginWrapper plugin = Jenkins.getInstance().getPluginManager().getPlugin("programmable-section");
        shell = new GroovyShell(plugin.classLoader);
    }
    
    public static Script parseScript(String scriptText){
        if(scriptText == null || scriptText.isEmpty()){
            return null;
        }
        
        return shell.parse(scriptText);
    }
}
