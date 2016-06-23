/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import groovy.lang.Script;
import hudson.Extension;
import hudson.model.BallColor;
import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.impl.GroovyScriptCompiler;
import jenkins.plugins.pivot.render.Presenter;
import jenkins.plugins.pivot.render.jelly.StatusBall;
import jenkins.plugins.pivot.util.StringUtil;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author zijiang
 */
public class PivotMeasurePresenterStatusBall extends PivotMeasurePresenterGroovySupport{
    
    private final String outdateScript;
    private final String statusScript;
    private final String urlScript;
    private final String infoScript;
    private final String tooltipScript;
    
    private transient Script compiledOutdateScript;
    private transient Script compiledStatusScript;
    private transient Script compiledUrlScript;
    private transient Script compiledInfoScript;
    private transient Script compiledTooltipScript;
    
    @DataBoundConstructor
    public PivotMeasurePresenterStatusBall(String outdateScript, String infoScript, String statusScript, String tooltipScript, String urlScript){
        this.outdateScript = outdateScript;
        this.infoScript = infoScript;
        this.statusScript = statusScript;
        this.tooltipScript = tooltipScript;
        this.urlScript = urlScript;
        
        compileScript();
    }
    
    public String getOutdateScript(){
        return this.outdateScript;
    }
    
    public String getStatusScript(){
        return this.statusScript;
    }
    
    public String getUrlScript(){
        return this.urlScript;
    }
    
    public String getInfoScript(){
        return this.infoScript;
    }
    
    public String getTooltipScript(){
        return this.tooltipScript;
    }
    
    private void compileScript(){
        this.compiledOutdateScript = GroovyScriptCompiler.parseScript(this.outdateScript);
        this.compiledInfoScript = GroovyScriptCompiler.parseScript(this.infoScript);
        this.compiledStatusScript = GroovyScriptCompiler.parseScript(this.statusScript);
        this.compiledUrlScript = GroovyScriptCompiler.parseScript(this.urlScript);
        this.compiledTooltipScript = GroovyScriptCompiler.parseScript(this.tooltipScript);
    }
    
    private Object readResolve() {
        compileScript();
        return this;
    }
    
    @Override
    public Presenter createStyle(PivotResult result) {
        return new StatusBall(getOutdated(result).booleanValue(), getBallColor(result), getUrl(result), getInfo(result), getTooltip(result));
    }
    
    private Boolean getOutdated(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.outdateScript)){
            return Boolean.FALSE;
        }
        Object o = eval(this.compiledOutdateScript, result);
        return (Boolean)o;     
    }
    
    private BallColor getBallColor(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.statusScript)){
            return null;
        }
        Object o= eval(this.compiledStatusScript, result);
        if(o == null){
            return null;
        }
        return (BallColor)o;
    }
    
    private String getInfo(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.infoScript)){
            return null;
        }
        Object o = eval(this.compiledInfoScript, result);
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    private String getUrl(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.urlScript)){
            return null;
        }
        Object o = eval(this.compiledUrlScript, result);
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    private String getTooltip(PivotResult result){
        if(StringUtil.isNullOrEmpty(this.tooltipScript)){
            return null;
        }
        
        if(this.compiledTooltipScript == null){
            return null;
        }
        Object o = eval(this.compiledTooltipScript, result);
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    @Extension
    public static class DescriptorImpl extends PivotMeasureStyleDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Status Ball";
        }
        
    }
}
