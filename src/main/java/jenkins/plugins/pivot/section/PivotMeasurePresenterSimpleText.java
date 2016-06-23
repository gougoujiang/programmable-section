/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import hudson.Extension;
import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.render.Presenter;
import jenkins.plugins.pivot.render.jelly.SimpleTextBuilder;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author zijiang
 */
public class PivotMeasurePresenterSimpleText extends PivotMeasurePresenter{
    
    @DataBoundConstructor
    public PivotMeasurePresenterSimpleText(){
    }
    
    @Override
    public Presenter createStyle(PivotResult result) {
        return new SimpleTextBuilder().createStyle(result);
    }
    
    @Extension
    public static class DescriptorImpl extends PivotMeasureStyleDefinitionDescriptor{

        @Override
        public String getDisplayName() {
            return "Simple Text";
        }
        
    }
}
