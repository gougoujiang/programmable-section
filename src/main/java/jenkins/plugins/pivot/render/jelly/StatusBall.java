/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render.jelly;

import jenkins.plugins.pivot.render.Presenter;
import hudson.model.BallColor;

/**
 *
 * @author zijiang
 */
public class StatusBall implements Presenter{
    
    private boolean outdate;    
    private BallColor icon;
    private String url;
    private String info;
    private String tooltip;
    
    public StatusBall(){}
    
    public StatusBall(boolean outdate, BallColor r, String url, String info, String tooltip){
        this.outdate = outdate;
        this.icon = r;
        this.url = url;
        this.info = info;
        this.tooltip = tooltip;
    }
    
    public boolean isOutdate(){
        return this.outdate;
    }
    
    public void setOutdate(boolean outdate){
        this.outdate = outdate;
    }
    
    public String getUrl(){
        return this.url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public BallColor getIconColor(){
        return this.icon;
    }
    
    public void setIconColor(BallColor icon){
        this.icon = icon;
    }
    
    public String getInfo(){
        return this.info;
    }
    
    public void setInfo(String info){
        this.info = info;
    }
    
    public String getTooltip(){
        return this.tooltip;
    }
    
    public void setTooltip(String tooltip){
        this.tooltip = tooltip;
    }
}
