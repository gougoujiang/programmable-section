/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import groovy.lang.Script;
import hudson.Extension;
import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.impl.GroovyScriptCompiler;
import jenkins.plugins.pivot.render.Presenter;
import jenkins.plugins.pivot.render.jelly.LinkableColoredText;
import jenkins.plugins.pivot.util.StringUtil;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author zijiang
 */
public class PivotMeasurePresenterColoredText extends PivotMeasurePresenterGroovySupport{
    private final String textScript;
    private final String colorScript;
    private final String urlScript;
    
    private transient Script compiledTextScript;
    private transient Script compiledColorScript;
    private transient Script compiledUrlScript;
    
    @DataBoundConstructor
    public PivotMeasurePresenterColoredText(String textScript, String colorScript, String urlScript){
        this.textScript = textScript;
        this.colorScript = colorScript;
        this.urlScript = urlScript;
        
        compileScript();
    }
    
    public String getTextScript(){
        return this.textScript;
    }
    
    public String getColorScript(){
        return this.colorScript;
    }
    
    public String getUrlScript(){
        return this.urlScript;
    }
    
    private void compileScript(){
        this.compiledTextScript = GroovyScriptCompiler.parseScript(this.textScript);
        this.compiledColorScript = GroovyScriptCompiler.parseScript(this.colorScript);
        this.compiledUrlScript = GroovyScriptCompiler.parseScript(this.urlScript);
    }

    @Override
    public Presenter createStyle(PivotResult result) {
        return new LinkableColoredText(getText(result), getColor(result), getUrl(result));
    }
    
    private String getUrl(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.urlScript)){
            return  null;
        }
        Object o = eval(this.compiledUrlScript, result);
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    private String getColor(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.colorScript)){
            return  null;
        }
        Object o = eval(this.compiledColorScript, result);
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    private String getText(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.textScript)){
            return  null;
        }
        Object o = eval(this.compiledTextScript, result);
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    private Object readResolve() {
        compileScript();
        return this;
    }
    
    @Extension
    public static class DescriptorImpl extends PivotMeasureStyleDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Colored Text";
        }
        
    }
}
