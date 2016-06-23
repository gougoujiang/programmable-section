/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render;

import jenkins.plugins.pivot.DimValueList;
import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.ResultTable;
import jenkins.plugins.pivot.ResultTableRender;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Render pivot result as a table.
 * @author zijiang
 */
public class PivotTableRender extends ResultTableRender{
    
    private List<Tr> rows;
    private final PresenterBuilder styleBuilder;
    
    public PivotTableRender(ResultTable rs, PresenterBuilder sbuilder){
        super(rs);
        this.styleBuilder = sbuilder;
    }
    
    /**
     * Get the file name.
     * @return 
     */
    @Override
    public String getPage(){
        return "table.jelly";
    }
    
    public List<Tr> getRows(){
        return this.rows;
    }
    
    @Override
    public void render() {
        this.rows = new ArrayList();
        ResultTable resultTable = this.getResultTable();
        if(resultTable == null || resultTable.isEmpty()){
            return;
        }
        
        // Print headers
        // Column dimension size, row of header
        List<DimValueList> columnDimValues = resultTable.getColumnDimValues();
        List<DimValueList> rowDimValues = resultTable.getRowDimValues();
        
        // Calculate column spans
        List<int[]> columnIndexList = resultTable.getColumnIndexList();
        Matrix2D colIndexRef = new Matrix2D(columnIndexList).transpose();
        Matrix2D mcolspan = calcColspan(columnIndexList);
        
        for(int row =0; row < columnDimValues.size(); row++){
            Tr tr = new Tr();
            // Print row dimension names
            if(row == 0){
                int colspan = resultTable.getColumnDimValues().size();
                for(int j = 0; j < rowDimValues.size(); j++){
                    tr.addElement(new Th(rowDimValues.get(j).getDim().getName(), colspan, 1));
                }
            }
            
            // Print column dimension values
            DimValueList dimValueList = columnDimValues.get(row);
            int colsize = resultTable.getColumnIndexList().size();
            for(int col = 0; col < colsize; col++){
                int span = mcolspan.get(row, col);
                if(span > 0){
                    int index = colIndexRef.get(row, col);
                    Object o = dimValueList.get(index);
                    tr.addElement(new Th(o.toString(), 1, span));
                }
            }
            this.rows.add(tr);
        }
        
        // Print data rows, with row headers
        List<int[]> rowIndexList = resultTable.getRowIndexList();
        // debug
        Matrix2D mrowspan = calcRowspan(rowIndexList);
        
        List<ResultTable.ResultRow> pivotRows = resultTable.getRows();
        for(int i = 0; i < pivotRows.size(); i++){
            Tr tr = new Tr();
            ResultTable.ResultRow row = pivotRows.get(i);
            
            // Print head
            for(int j = 0; j < row.getRowDimValues().size(); j++){
                int span = mrowspan.get(i, j);
                if( span > 0){
                    Object o = row.getRowDimValues().get(j);
                    tr.addElement(new Th(o.toString(), span, 1));
                }
            }
            
            // Print measure data
            List<ResultTable.ResultColumn> columns = row.getColumns();
            for(ResultTable.ResultColumn col : columns){
                PivotResult result = col.getResult();
                Presenter style = this.styleBuilder.createStyle(result);
                
                Td td = new Td(style);
                //applyStyle(col.getResult(), td);
                tr.addElement(td);
            }
            
            this.rows.add(tr);
        }
    }
    
    private Matrix2D calcRowspan(List<int[]> indexList){
        Matrix2D m = new Matrix2D(indexList);
        
        // build a reference matrix
        Matrix2D r = new Matrix2D(m.getRowSize(), m.getColumnSize()+1);
        r.unifiy(1);
        
        for(int row = 0; row < r.getRowSize(); row++){
            r.set(row, 0, 0);
        }
        
        // Calculate the spans.
        // from bottom up, count the values
        for(int colIndex = 0; colIndex < m.getColumnSize(); colIndex++){
            int refColIndex = colIndex + 1;
            int rowIndex = m.getRowSize() - 1; // from bottom up
            while(rowIndex > 0){
                if(m.get(rowIndex, colIndex) == m.get(rowIndex - 1, colIndex) && r.get(rowIndex, refColIndex - 1) == 0){
                    int sum = r.get(rowIndex, refColIndex) + r.get(rowIndex - 1, refColIndex);
                    r.set(rowIndex - 1, refColIndex, sum);
                    r.set(rowIndex, refColIndex, 0);
                }
                
                rowIndex--;
            }
        }
        
        Matrix2D result = new Matrix2D(m.getRowSize(), m.getColumnSize());
        for(int row = 0; row < r.getRowSize(); row++){
            for(int col = 1; col < r.getColumnSize(); col++){
                result.set(row, col - 1, r.get(row, col));
            }
        }
        return result;
    }
    
    private Matrix2D calcColspan(List<int[]> indexList){
        Matrix2D m = this.calcRowspan(indexList);
        return m.transpose();
    }
    
    public static class Tr{
        private List<Cell> elements;
        
        public Tr(){
            this.elements = new ArrayList();
        }
        
        public void addElement(Cell c){
            this.elements.add(c);
        }
        
        public List<Cell> getElements(){
            return this.elements;
        }
    }
    
    public static abstract class Cell{
        private String content;
        private int rowspan;
        private int colspan;
        private final ArrayList styles;
        
        protected Cell(String content){
            this(content, 1, 1);
        }
        
        protected Cell(String content, int rowspan, int colspan){
            this.content = content;
            this.rowspan = rowspan;
            this.colspan = colspan;
            this.styles = new ArrayList();
        }
        
        public String getContent(){
            return this.content;
        }
        
        public void setRowspan(int rowspan){
            this.rowspan = rowspan;
        }
        
        public int getRowspan(){
            return this.rowspan;
        }
        
        public void setColspan(int colspan){
            this.colspan = colspan;
        }
        
        public int getColspan(){
            return this.colspan;
        }
        
        public void addStyle(String style){
            this.styles.add(style);
        }
        
        public String getStyle(){
            return StringUtils.join(this.styles, ";");
        }
    }
    
    public static class Th extends Cell{
        public Th(String content){
            super(content);
        }
        
        public Th(String content, int rowspan, int colspan){
            super(content, rowspan, colspan);
        }
    }
    
    public static class Td extends Cell{
        Presenter presenter;
        
        public Td(String content){
            super(content);
        }
        
        public Td(Presenter p){
            super("");
            this.presenter = p;
        }
        
        public Presenter getPresenter(){
            return this.presenter;
        }
        
        public Td(String content, int rowspan, int colspan){
            super(content, rowspan, colspan);
        }
    }
}
