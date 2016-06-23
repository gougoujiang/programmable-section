/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.plugins.pivot.Measure;

/**
 *
 * @author zijiang
 */
public abstract class PivotMeasureDefinition implements Describable<PivotMeasureDefinition>, ExtensionPoint{
    
    public abstract Measure getMeasure();
    
    @Override
    public Descriptor<PivotMeasureDefinition> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
    
    public static DescriptorExtensionList<PivotMeasureDefinition, Descriptor<PivotMeasureDefinition>> all(){
        return Jenkins.getInstance().<PivotMeasureDefinition, Descriptor<PivotMeasureDefinition>>getDescriptorList(PivotMeasureDefinition.class);
    }
    
    public static abstract class PivotMeasureDefinitionDescriptor extends Descriptor<PivotMeasureDefinition> {
    }
    
}
