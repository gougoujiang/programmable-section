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
public class SimpleText implements Presenter{
    private final String text;
    
    public SimpleText(String text){
        this.text = text;
    }
    
    public String getText(){
        return this.text;
    }
}
