/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.pivot;

import java.util.Map;

/**
 * Customized control to display properties
 * @author zijiang
 */
public class PropertyTable{
    private String title;
    private String backUrl;
    private Map<String, String> props;
    
    public PropertyTable(String title, String backUrl, Map<String, String> props){
        this.title = title;
        this.backUrl = backUrl;
        this.props = props;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getBackUrl(){
        return this.backUrl;
    }
    
    public Map<String, String> getProperties(){
        return this.props;
    }
}
