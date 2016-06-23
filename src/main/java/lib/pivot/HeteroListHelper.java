/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.pivot;

import hudson.DescriptorExtensionList;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author yihua
 */
public class HeteroListHelper {
    private String name;
    private Boolean hasHeader;
    private DescriptorExtensionList descriptors;
    private String deleteCaption;
    private String addCaption;
    private Boolean collapseAll;

    public Boolean getCollapseAll() {
        return collapseAll;
    }

    public void setCollapseAll(Boolean collapseAll) {
        this.collapseAll = collapseAll;
    }

    public Boolean getExpandUnique() {
        return expandUnique;
    }

    public void setExpandUnique(Boolean expandUnique) {
        this.expandUnique = expandUnique;
    }
    private Boolean expandUnique;

    public HeteroListHelper() {}
    public HeteroListHelper(StaplerRequest req) {
        setName(req.getParameter("name"));
        setHasHeader(Boolean.parseBoolean(req.getParameter("hasHeader")));
        setAddCaption(req.getParameter("addCaption"));
        setDeleteCaption(req.getParameter("deleteCaption"));
        setExpandUnique(Boolean.parseBoolean(req.getParameter("expandUnique")));
        setCollapseAll(Boolean.parseBoolean(req.getParameter("collapseAll")));
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public DescriptorExtensionList getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(DescriptorExtensionList descriptors) {
        this.descriptors = descriptors;
    }

    public String getDeleteCaption() {
        return deleteCaption;
    }

    public void setDeleteCaption(String deleteCaption) {
        this.deleteCaption = deleteCaption;
    }

    public String getAddCaption() {
        return addCaption;
    }

    public void setAddCaption(String addCaption) {
        this.addCaption = addCaption;
    }
}
