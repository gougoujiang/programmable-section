/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot;

import java.util.List;

/**
 *
 * @author zijiang
 */
public class PivotContext {
    private final DataItemProvider itemProvider;
    
    private final List<Dimension> rowDims;
    private final List<Dimension> columnDims;
    private final Measure measure;
    
    private String errorMessage;
    
    public PivotContext(DataItemProvider itemProvider, List<Dimension> rowDims, List<Dimension> colDims, Measure measure){
        this.itemProvider = itemProvider;
        this.rowDims = rowDims;
        this.columnDims = colDims;
        this.measure = measure;        
    }
    
    public DataItemProvider getDataItemProvider(){
        return this.itemProvider;
    }
    
    public List<Dimension> getRowDims(){
        return this.rowDims;
    }
    
    public List<Dimension> getColumnDims(){
        return this.columnDims;
    }
    
    public Measure getMeasure(){
        return this.measure;
    }
    
    public boolean verify(){
        if(this.itemProvider == null){
            this.errorMessage = "No data item provider";
            return false;
        }
        
        if(this.rowDims == null || this.rowDims.isEmpty()){
            this.errorMessage = "Row dimensions not specified";
            return false;
        }
        
        if(this.columnDims == null || this.columnDims.isEmpty()){
            this.errorMessage = "Column dimensions not specified";
            return false;
        }
        
        if(this.measure == null){
            this.errorMessage = "Measure not specified";
            return false;
        }
        
        return true;            
    }
    
    public String getMessage(){
        return this.errorMessage;
    }
    
}
