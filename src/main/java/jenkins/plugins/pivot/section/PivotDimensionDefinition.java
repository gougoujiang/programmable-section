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
import jenkins.plugins.pivot.Dimension;

/**
 * Pivot dimension configurations with UI.
 * @author zijiang
 */
public abstract class PivotDimensionDefinition implements Describable<PivotDimensionDefinition>, ExtensionPoint{
    
    public abstract Dimension getDimension();
    
    @Override
    public Descriptor<PivotDimensionDefinition> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
    
    public static DescriptorExtensionList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>> all(){
        return Jenkins.getInstance().<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>>getDescriptorList(PivotDimensionDefinition.class);
    }
    
    public static abstract class PivotDimensionDefinitionDescriptor extends Descriptor<PivotDimensionDefinition>{
    }
    
}
