/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render.jelly;

import jenkins.plugins.pivot.render.Presenter;
import java.util.List;

/**
 * Display list of status balls, with the first one as summary
 * @author zijiang
 */
public class StatusBallList implements Presenter{
    private final List<StatusBall> status;
    
    public StatusBallList(List<StatusBall> status){
        this.status = status;
    }
    
    public boolean isValid(){
        return this.status != null && !this.status.isEmpty();
    }
    
    public List<StatusBall> getStatus(){
        return this.status;
    }
}
