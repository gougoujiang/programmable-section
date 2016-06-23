/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot;

/**
 *
 * @author zijiang
 */
public abstract class ResultTableRender {
    private final ResultTable rs;
    
    protected ResultTableRender(ResultTable rs){
        this.rs = rs;
    }
    
    public ResultTable getResultTable(){
        return this.rs;
    }
    
    public abstract void render();    
    
    public abstract String getPage();
}
