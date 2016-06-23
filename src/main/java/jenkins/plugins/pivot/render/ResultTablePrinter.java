/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render;

import jenkins.plugins.pivot.DimValueList;
import jenkins.plugins.pivot.ResultTable;
import java.util.List;

/**
 *
 * @author zijiang
 */
public class ResultTablePrinter {
    private final ResultTable rs;
    
    public ResultTablePrinter(ResultTable rs){
        this.rs = rs;
    }
    
    public ResultTable getResultTable(){
        return this.rs;
    }
    
    public void render(){
        // Print headers
        // Column dimension size, row of header
        List<DimValueList> columnDimValues = this.rs.getColumnDimValues();
        List<DimValueList> rowDimValues = this.rs.getRowDimValues();
        for(int i =0; i < columnDimValues.size(); i++){
            // Print row dimension names
            for(int j = 0; j < rowDimValues.size(); j++){
                if(i == 0){
                    print(rowDimValues.get(j).getDim().getName());
                    print(',');
                }else{
                    print("<SAMEUP> ,");
                }
            }
            
            // Print column dimension values
            List<int[]> columnIndexList = this.rs.getColumnIndexList();
            int[] indexes = new int[columnIndexList.size()];
            for(int k = 0; k < columnIndexList.size(); k++){
                int[] colIndexes = columnIndexList.get(k);
                indexes[k] = colIndexes[i];
            }
            
            DimValueList dimValueList = columnDimValues.get(i);
            for(int index : indexes){
                print(dimValueList.get(index));
                print(",");
            }
            
            println();
        }
        // Print data rows, with row headers
        List<ResultTable.ResultRow> rows = this.rs.getRows();
        for(ResultTable.ResultRow row : rows){
            // Print head
            for(Object o : row.getRowDimValues()){
                print(o);
                print(',');
            }
            // Print measure data
            List<ResultTable.ResultColumn> columns = row.getColumns();
            for(ResultTable.ResultColumn col : columns){
                String txt = "";
                if(col.getResult() != null && col.getResult().getValue() != null){
                    txt = col.getResult().getValue().toString();
                }
                print(txt);
                print(',');                
            }
            
            println();
        }
    }
    
    private void print(Object s){
        System.out.print(s);
    }
    
    private void println(){
        System.out.println();
    }
    
}
