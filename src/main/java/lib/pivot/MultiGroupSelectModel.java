package lib.pivot;

import hudson.model.ModelObject;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Flavor;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Model object of dynamically filled multiple check box.
 *
 */
@ExportedBean
public class MultiGroupSelectModel extends ArrayList<MultiGroupSelectModel.Item> implements HttpResponse {
    public static final class SelectedItem
    {
        private String group;
        private String value;

        public SelectedItem()
        {}
        
        public SelectedItem(String group, String value) {
            this.group = group;
            this.value = value;
        }
        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
        
    }
    @ExportedBean(defaultVisibility=999)
    public static final class Item {
        @Exported
        public String group;
        /**
         * Text to be displayed to user.
         */
        @Exported
        public String name;
        /**
         * The value that gets sent to the server when the form is submitted.
         */
        @Exported
        public String value;

        public Item(String group, String name) {
            this(group, name, name);
        }

        public Item(String group, String name, String value) {
            this.group = group;
            this.name = name;
            this.value = value;
        }
    }

    public MultiGroupSelectModel(int initialCapacity) {
        super(initialCapacity);
    }

    public MultiGroupSelectModel() {
    }

    public MultiGroupSelectModel(Collection<MultiGroupSelectModel.Item> c) {
        super(c);
    }

    public MultiGroupSelectModel(MultiGroupSelectModel.Item... data) {
        super(Arrays.asList(data));
    }

    public void add(String group, String displayName, String value) {
        add(new MultiGroupSelectModel.Item(group, displayName, value));
    }

    public void add(ModelObject usedForDisplayName, String value) {
        add(usedForDisplayName.getDisplayName(), value);
    }

    /**
     * A version of the {@link #add(String, String)} method where the display name and the value are the same. 
     */
    public MultiGroupSelectModel add(String group, String nameAndValue) {
        add(group, nameAndValue,nameAndValue);
        return this;
    }

    public void writeTo(StaplerRequest req,StaplerResponse rsp) throws IOException, ServletException {
        rsp.serveExposedBean(req,this,Flavor.JSON);
    }

    public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
        writeTo(req,rsp);
    }

    /**
     * @deprecated
     *      Exposed for stapler. Not meant for programatic consumption.
     */
    @Exported
    public MultiGroupSelectModel.Item[] values() {
        return toArray(new MultiGroupSelectModel.Item[size()]);
    }
}
