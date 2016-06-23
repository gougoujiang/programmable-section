/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.section;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Saveable;
import hudson.model.TopLevelItem;
import hudson.plugins.sectioned_view.SectionedViewSection;
import hudson.plugins.sectioned_view.SectionedViewSectionDescriptor;
import hudson.util.DescribableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.plugins.pivot.DataItemProvider;
import jenkins.plugins.pivot.Dimension;
import jenkins.plugins.pivot.Measure;
import jenkins.plugins.pivot.PivotContext;
import jenkins.plugins.pivot.PivotEngine;
import jenkins.plugins.pivot.ResultTable;
import jenkins.plugins.pivot.ResultTableRender;
import jenkins.plugins.pivot.impl.PivotEngineImpl;
import jenkins.plugins.pivot.render.ErrorReporter;
import jenkins.plugins.pivot.render.PivotTableRender;
import jenkins.plugins.pivot.render.PresenterBuilder;
import jenkins.plugins.pivot.render.jelly.SimpleTextBuilder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Groovy script based pivot view section.
 * @author zijiang
 */
public class ProgrammablePivotViewSection extends SectionedViewSection{
    private PivotDataItemProviderDefinition dataItemProviderDef;
    private DescribableList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>> rowDimDefs;
    private DescribableList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>> colDimDefs;
    
    private PivotMeasureDefinition measureDef;
    private PivotMeasurePresenter presenter;
    
    private boolean useFolding;
    private boolean folding;
    private Boolean statusFilter;
    
    @DataBoundConstructor
    public ProgrammablePivotViewSection(String name, SectionedViewSection.Width width, SectionedViewSection.Positioning alignment,
                                      boolean useFolding, boolean folding, String filter,
                                      PivotDataItemProviderDefinition dataItemProviderDef,
                                      PivotMeasureDefinition measureDef, 
                                      PivotMeasurePresenter presenter){
        super(name, width, alignment);
        this.dataItemProviderDef = dataItemProviderDef;
        this.measureDef = measureDef;
        this.presenter = presenter;
        this.useFolding = useFolding;
        this.folding = folding;
        this.statusFilter = filter != null ? "1".equals(filter) : null;
    }
    
    public boolean getUseFolding() {
        return useFolding;
    }

    public boolean getFolding() {
        return folding;
    }

    public Boolean getStatusFilter() {
        return statusFilter;
    }
    
    public PivotDataItemProviderDefinition getDataItemProviderDef(){
        return this.dataItemProviderDef;
    }
    
    public DescribableList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>> getRowDimDefs() {
        return this.rowDimDefs;
    }
    
    public DescribableList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>> getColDimDefs() {
        return this.colDimDefs;
    }
    
    public PivotMeasureDefinition getMeasureDef(){
        return this.measureDef;
    }
    
    public PivotMeasurePresenter getPresenter(){
        return this.presenter;
    }
    
    public ResultTableRender doPivot(PivotContext context){
        try{
            ResultTable rs = runPivot(context);
            
            // By default, use the simple plain text style
            PresenterBuilder sb = new SimpleTextBuilder();
            if(this.presenter != null){
                sb = this.presenter;
            }
            PivotTableRender render = new PivotTableRender(rs, sb);
            
            render.render();
            return render;
        }catch(Throwable ex){
            ResultTableRender r = new ErrorReporter(ex.getMessage(), ex);
            r.render();
            return r;
        }
    }
    
    public PivotContext preparePivotContext(Collection<TopLevelItem> jobs){
        // Provider
        DataItemProvider provider = this.dataItemProviderDef.getDataItemProvider(jobs);
        
        // Row dim
        List<Dimension> rowDims = new ArrayList();
        for(PivotDimensionDefinition def : this.rowDimDefs){
            rowDims.add(def.getDimension());
        }
        
        // Column dim
        List<Dimension> colDims = new ArrayList();
        for(PivotDimensionDefinition def : this.colDimDefs){
            colDims.add(def.getDimension());
        }
        
        // Measures
        Measure m = this.measureDef.getMeasure();
        
        return new PivotContext(provider, rowDims, colDims, m);
    }
    
    private ResultTable runPivot(PivotContext context){
        PivotEngine pivotEngine = new PivotEngineImpl();
        return pivotEngine.process(context);
    }
    
    @Extension
    public static class DescriptorImpl extends SectionedViewSectionDescriptor{
        
        @Override
        public String getDisplayName(){
            return "Programmable Pivot Section ";
        }
        
        @Override
        public ProgrammablePivotViewSection newInstance(StaplerRequest req, JSONObject formData) throws Descriptor.FormException {
            ProgrammablePivotViewSection section = (ProgrammablePivotViewSection) super.newInstance(req, formData);
            setOwnership(section);
            rebuildRowDimDefs(section, req, formData);
            rebuildColDimDefs(section, req, formData);
            return section;
        }
        
        private void setOwnership(ProgrammablePivotViewSection section){
            if(section.dataItemProviderDef != null && section.dataItemProviderDef instanceof PivotDataItemProviderUseGroovy){
                ((PivotDataItemProviderUseGroovy)section.dataItemProviderDef).setOwner(section);
            }
        }
        
        private void rebuildRowDimDefs(ProgrammablePivotViewSection section, StaplerRequest req, JSONObject formData) throws Descriptor.FormException{
            if(section.rowDimDefs == null){
                section.rowDimDefs = new DescribableList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>>(Saveable.NOOP);
            }
            
            // rebuild column/row list
            try{
                section.rowDimDefs.rebuildHetero(req, formData, this.getDimensionDefDescriptors(), "rowDimDefs");
            }catch (IOException e) {
                throw new Descriptor.FormException("Error rebuilding list of row dimensions.", e, "rowDimDefs");
            }
        }
        
        private void rebuildColDimDefs(ProgrammablePivotViewSection section, StaplerRequest req, JSONObject formData) throws Descriptor.FormException{
            if(section.colDimDefs == null){
                section.colDimDefs = new DescribableList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>>(Saveable.NOOP);
            }
            
            try{
                section.colDimDefs.rebuildHetero(req, formData, this.getDimensionDefDescriptors(), "colDimDefs");
            }catch (IOException e) {
                throw new Descriptor.FormException("Error rebuilding list of column dimensions.", e, "colDimDefs");
            }
        }
        
        public DescriptorExtensionList<PivotDataItemProviderDefinition, Descriptor<PivotDataItemProviderDefinition>> getDataItemProviderDefDescriptors(){
            return PivotDataItemProviderDefinition.all();
        }
        
        public DescriptorExtensionList<PivotDimensionDefinition, Descriptor<PivotDimensionDefinition>> getDimensionDefDescriptors(){
            return PivotDimensionDefinition.all();
        }
        
        public DescriptorExtensionList<PivotMeasureDefinition, Descriptor<PivotMeasureDefinition>> getMeasureDefDescriptors(){
            return PivotMeasureDefinition.all();
        }
        
        public DescriptorExtensionList<PivotMeasurePresenter, Descriptor<PivotMeasurePresenter>> getPresenterDescriptors(){
            return PivotMeasurePresenter.all();
        }

        public Collection<TopLevelItem> getJobs() {
            return Jenkins.getInstance().getItems();
        }
        
        public ProgrammablePivotViewSection getDefaultInstance(){
            return new ProgrammablePivotViewSection("", SectionedViewSection.Width.TWO_THIRDS, SectionedViewSection.Positioning.LEFT, 
                                                false, false, null, null, null, new PivotMeasurePresenterSimpleText());
        }
    }
}
