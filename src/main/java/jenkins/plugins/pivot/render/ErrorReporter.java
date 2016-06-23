/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render;

import jenkins.plugins.pivot.ResultTable;
import jenkins.plugins.pivot.ResultTableRender;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author zijiang
 */
public class ErrorReporter extends ResultTableRender{
    private String errMsg;
    private final Throwable ex;
    private String stacktrace
;    
    public ErrorReporter(String errMsg, Throwable ex){
        this(errMsg, ex, null);
    }
    
    public ErrorReporter(String errMsg, Throwable ex, ResultTable rs){
        super(rs);
        this.errMsg = errMsg;
        this.ex = ex;
    }
    
    public String getMessage(){
        return this.errMsg;
    }
    
    public Throwable getException(){
        return this.ex;
    }
    
    public String getStacktrace(){
        return this.stacktrace;
    }

    @Override
    public void render() {
        if(this.errMsg == null && this.ex != null){
            this.errMsg = this.ex.getMessage();
        }
        fillStackTrace();
    }

    @Override
    public String getPage() {
        return "error.jelly";
    }

    private void fillStackTrace() {
        if(this.ex != null){
            StringWriter sw = new StringWriter();
            this.ex.printStackTrace(new PrintWriter(sw));
            this.stacktrace = sw.toString();
        }
    }
}
