/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import jenkins.plugins.pivot.DataItemProvider;
import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import java.util.Collection;
import jenkins.model.Jenkins;

/**
 * Configurable data item poviders
 * @author zijiang
 */
public abstract class PivotDataItemProviderDefinition implements Describable<PivotDataItemProviderDefinition>, ExtensionPoint{
    public abstract DataItemProvider getDataItemProvider(Collection<TopLevelItem> jobs);
    
    @Override
    public Descriptor<PivotDataItemProviderDefinition> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
    
    public static DescriptorExtensionList<PivotDataItemProviderDefinition, Descriptor<PivotDataItemProviderDefinition>> all(){
        return Jenkins.getInstance().<PivotDataItemProviderDefinition, Descriptor<PivotDataItemProviderDefinition>>getDescriptorList(PivotDataItemProviderDefinition.class);
    }
    
    public static abstract class PivotDataItemProviderDefinitionDescriptor extends Descriptor<PivotDataItemProviderDefinition>{
        
    }
}
