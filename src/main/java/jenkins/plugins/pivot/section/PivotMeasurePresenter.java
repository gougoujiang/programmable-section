/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import jenkins.plugins.pivot.render.Presenter;
import jenkins.plugins.pivot.render.PresenterBuilder;
import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

/**
 * Styles
 * @author zijiang
 */
public abstract class PivotMeasurePresenter implements PresenterBuilder, Describable<PivotMeasurePresenter>, ExtensionPoint{

    public Descriptor<PivotMeasurePresenter> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
    
    public static DescriptorExtensionList<PivotMeasurePresenter, Descriptor<PivotMeasurePresenter>> all(){
        return Jenkins.getInstance().<PivotMeasurePresenter, Descriptor<PivotMeasurePresenter>>getDescriptorList(PivotMeasurePresenter.class);
    }
    
    public static abstract class PivotMeasureStyleDefinitionDescriptor extends Descriptor<PivotMeasurePresenter> {
    }
}
