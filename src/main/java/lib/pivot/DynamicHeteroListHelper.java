/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.pivot;

import hudson.DescriptorExtensionList;
import java.util.List;
import org.kohsuke.stapler.ForwardToView;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author yihua
 */
public class DynamicHeteroListHelper {

    private String name;
    private Boolean hasHeader;
    private List descriptors;
    private String deleteCaption;
    private String addCaption;
    private Boolean collapseAll;
    private Boolean showMore;
    
    public Boolean getShowMore() {
        return showMore;
    }
    
    public void setShowMore(Boolean showMore) {
        this.showMore = showMore;
    }
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

    public DynamicHeteroListHelper() {
    }

    public DynamicHeteroListHelper(StaplerRequest req) {
        setName(req.getParameter("name"));
        setHasHeader(Boolean.parseBoolean(req.getParameter("hasHeader")));
        setAddCaption(req.getParameter("addCaption"));
        setDeleteCaption(req.getParameter("deleteCaption"));       
        setCollapseAll(Boolean.parseBoolean(req.getParameter("collapseAll")));
        setExpandUnique(Boolean.parseBoolean(req.getParameter("expandUnique")));
        setShowMore(Boolean.parseBoolean(req.getParameter("showMore")));
    }

    public DynamicHeteroListHelper(StaplerRequest req, List list) {
        this(req);
        setDescriptors(list);
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

    public List getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List descriptors) {
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

    public ForwardToView forwardToView() {
        return HttpResponses.forwardToView(this, "index.jelly");
    }
}
