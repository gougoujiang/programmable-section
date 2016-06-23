/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render.jelly;

import jenkins.plugins.pivot.render.Presenter;

/**
 *
 * @author zijiang
 */
public class LinkableColoredText extends SimpleText{
    
    private final String color;
    private final String url;
    
    public LinkableColoredText(String text, String color, String url){
        super(text);
        this.color = color;
        this.url = url;
    }
    
    public String getColor(){
        return this.color;
    }
    
    public String getUrl(){
        return this.url;
    }
}
