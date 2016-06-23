/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zijiang
 */
public class ResultTable {
    private final List<DimValueList> rowDimValues;
    private final List<DimValueList> colDimValues;
    
    private final List<int[]> rowIndexList;
    private final List<int[]> colIndexList;
    
    private List<ResultRow> rows;
    
    public ResultTable(List<DimValueList> rowDimValues, List<int[]> rowIndexList,
                         List<DimValueList> colDimValues, List<int[]> colIndexList){
        this.rowDimValues = rowDimValues;
        this.rowIndexList = rowIndexList;
        
        this.colDimValues = colDimValues;
        this.colIndexList = colIndexList;
        
        this.rows = new ArrayList();
    }
    
    public List<DimValueList> getRowDimValues(){
        return this.rowDimValues;
    }
    
    public List<DimValueList> getColumnDimValues(){
        return this.colDimValues;
    }
    
    public List<int[]> getRowIndexList(){
        return this.rowIndexList;
    }
    
    public List<int[]> getColumnIndexList(){
        return this.colIndexList;
    }
    
    public List<ResultRow> getRows(){
        return Collections.unmodifiableList(this.rows);
    }
    
    public void addRow(ResultRow row){
        this.rows.add(row);
    }
    
    public ResultRow newRow(int[] rowDimValueIndexes){
        return new ResultRow(rowDimValueIndexes);
    }
    
    public void addCoumn(ResultRow row, ResultColumn column){
        row.columns.add(column);
    }
    
    public ResultColumn newColumn(ResultRow row, int[] colDimValueIndexes){
        return new ResultColumn(row, colDimValueIndexes);
    }
    
    public int getRowCount(){
        return this.rowIndexList.size();
    }
    
    public int getColumnCount(){
        return this.colIndexList.size();
    }
    
    public boolean isEmpty(){
        return this.rows.isEmpty();
    }
    
    public boolean isEmptyRow(int rowIndex){
        ResultRow row = this.rows.get(rowIndex);
        for(ResultColumn col : row.columns){
            if(!col.isEmpty()){
                return false;
            }
        }
        return true;
    }
    
    public boolean isEmptyColumn(int columnIndex){
        for(ResultRow row : this.rows){
            if(!row.columns.get(columnIndex).isEmpty()){
                return false;
            }
        }
        return true;
    }
    
    protected ResultTable _this(){
        return this;
    }

    public class ResultRow{
        private final int[] rowDimValueIndexes;
        private List<ResultColumn> columns;
        
        public ResultRow(int[] rowDimValueIndexes){
            this.rowDimValueIndexes = rowDimValueIndexes;
            this.columns = new ArrayList();
        }
        
        public List<Object> getRowDimValues(){
            assert this.rowDimValueIndexes.length == _this().rowDimValues.size();
            
            List<Object> result = new ArrayList();
            for(int i = 0; i < this.rowDimValueIndexes.length; i++){
                int index = this.rowDimValueIndexes[i];
                result.add(_this().rowDimValues.get(i).get(index));
            }
            return result;
        }
        
        public void addColumn(ResultColumn col){
            this.columns.add(col);
        }
        
        public List<ResultColumn> getColumns(){
            return Collections.unmodifiableList(this.columns);
        }
    }
    
    public class ResultColumn{
        private final ResultRow row;
        private final int[] colDimValueIndexes;
        private PivotResult result;
        
        public ResultColumn(ResultRow row, int[] colDimValueIndexes){
            this.row = row;
            this.colDimValueIndexes = colDimValueIndexes;
        }
        
        public List<Object> getColumnDimValues(){
            assert this.colDimValueIndexes.length == _this().colDimValues.size();
            
            List<Object> values = new ArrayList();
            for(int i = 0; i < this.colDimValueIndexes.length; i++){
                int index = this.colDimValueIndexes[i];
                values.add(_this().colDimValues.get(i).get(index));
            }
            return values;
        }
        
        public void setResult(PivotResult result){
            this.result = result;
        }
        
        public PivotResult getResult(){
            return this.result;
        }
        
        public boolean isEmpty(){
            return this.result == null || this.result.isEmpty();
        }
        
        public ResultRow getRow(){
            return this.row;
        }
    }
}
