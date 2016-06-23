/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render.jelly;

import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.render.Presenter;
import jenkins.plugins.pivot.render.PresenterBuilder;

/**
 *
 * @author zijiang
 */
public class SimpleTextBuilder implements PresenterBuilder{

    public Presenter createStyle(PivotResult result) {
        String text  ="";
        if(result != null && result.getValue() != null){
            text = result.getValue().toString();
        }
        return new SimpleText(text);
    }
    
}
