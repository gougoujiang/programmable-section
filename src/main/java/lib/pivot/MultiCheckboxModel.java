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
public class MultiCheckboxModel extends ArrayList<MultiCheckboxModel.Item> implements HttpResponse {

    @ExportedBean(defaultVisibility=999)
    public static final class Item {
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

        /**
         * True to make this item selected.
         */
        @Exported
        public boolean checked;

        public Item(String name, String value) {
            this(name,value,false);
        }

        public Item(String name) {
            this(name,name,false);
        }

        public Item(String name, String value, boolean checked) {
            this.name = name;
            this.value = value;
            this.checked = checked;
        }
    }

    public MultiCheckboxModel(int initialCapacity) {
        super(initialCapacity);
    }

    public MultiCheckboxModel() {
    }

    public MultiCheckboxModel(Collection<MultiCheckboxModel.Item> c) {
        super(c);
    }

    public MultiCheckboxModel(MultiCheckboxModel.Item... data) {
        super(Arrays.asList(data));
    }

    public void add(String displayName, String value) {
        add(new MultiCheckboxModel.Item(displayName,value));
    }

    public void add(ModelObject usedForDisplayName, String value) {
        add(usedForDisplayName.getDisplayName(), value);
    }

    /**
     * A version of the {@link #add(String, String)} method where the display name and the value are the same. 
     */
    public MultiCheckboxModel add(String nameAndValue) {
        add(nameAndValue,nameAndValue);
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
    public MultiCheckboxModel.Item[] values() {
        return toArray(new MultiCheckboxModel.Item[size()]);
    }
}
