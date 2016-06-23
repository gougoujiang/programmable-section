/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import jenkins.plugins.pivot.Dimension;
import jenkins.plugins.pivot.DimValueList;
import jenkins.plugins.pivot.PivotContext;
import jenkins.plugins.pivot.PivotEngine;
import jenkins.plugins.pivot.PivotResult;
import jenkins.plugins.pivot.ResultTable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zijiang
 */
public class PivotEngineImpl implements PivotEngine{
    private PivotContext context;
    private ResultTable rs;
    
    private List<Object> dataItems;
    
    private List<DimValueList> rowDimValues;
    private List<DimValueList> colDimValues;
    

    @Override
    public ResultTable process(PivotContext context) {
        this.context = context;
        
        // Prepare raw data items
        this.dataItems = this.context.getDataItemProvider().getDataItems();
        
        // Calculate dimension values
        this.rowDimValues = this.getDimValues(this.context.getRowDims());
        this.colDimValues = this.getDimValues(this.context.getColumnDims());
        
        // Build the result table
        List<int[]> rowIndexList = combine(this.rowDimValues);
        List<int[]> filteredRowIndexList = filterInvalidRowCombination(rowIndexList);
        
        List<int[]> colIndexList = combine(this.colDimValues);
        List<int[]> filteredColIndexList = filterInvalidColCombination(colIndexList);
        
        ResultTable table = new ResultTable(this.rowDimValues, filteredRowIndexList, 
                                            this.colDimValues, filteredColIndexList);
        
        for(int[] rowIndexes : filteredRowIndexList){
            ResultTable.ResultRow newRow = table.newRow(rowIndexes);
            for(int[] colIndexes : filteredColIndexList){
                ResultTable.ResultColumn newColumn = table.newColumn(newRow, colIndexes);
                
                List<Object> items = this.doFilter(rowIndexes, colIndexes);
                Object value = this.doMeasure(items);
                
                newColumn.setResult(new PivotResult(value, items));
                newRow.addColumn(newColumn);
            }
            table.addRow(newRow);
        }
        
        return table;
    }
    
    private List<Object> doFilter(int[] rowIndexes, int[] colIndexes){
        List<Object> result = new ArrayList();
        OUTER: for(Object item : this.dataItems){
                    for(int i = 0; i < rowIndexes.length; i++){
                        Dimension dim = this.rowDimValues.get(i).getDim();
                        Object dimValue = this.rowDimValues.get(i).get(rowIndexes[i]);
                        if(!dimValue.equals(dim.getValue(item))){
                            continue OUTER;
                        }
                    }

                    for(int j = 0; j < colIndexes.length; j++){
                        Dimension dim = this.colDimValues.get(j).getDim();
                        Object dimValue = this.colDimValues.get(j).get(colIndexes[j]);
                        if(!dimValue.equals(dim.getValue(item))){
                            continue OUTER;
                        }
                    }

                    result.add(item);
                }
        
        return result;
    }
    
    private Object doMeasure(List<Object> items){
        return this.context.getMeasure().getValue(items);
    }
    
    private List<int[]> combine(List<DimValueList> ll){
        QuickCombiner qc = new QuickCombiner(ll);
        return qc.run();
    }
    
    private List<DimValueList> getDimValues(List<Dimension> dims){
        List<DimValueList> dimValues = new ArrayList();
        for(Dimension d : dims){
            dimValues.add(listDimValues(d));
        }
        return dimValues;
    }
    
    private DimValueList listDimValues(Dimension dim){
        List<Object> values = new ArrayList();
        for(Object o : this.dataItems){
            Object v = dim.getValue(o);
            if(!values.contains(v)){
                values.add(v);
            }
        }
        
        // Sort values
        List<Object> sortedValues = dim.sortValues(values);
        DimValueList dvl = new DimValueList(dim);
        for(Object o : sortedValues){
            dvl.add(o);
        }
        return dvl;
    } 

    private List<int[]> filterInvalidRowCombination(List<int[]> rowIndexList) {
        List<int[]> res = new ArrayList();
        for(int[] arr : rowIndexList){
            List<Object> items = this.doFilter(arr, new int[]{});
            if(!items.isEmpty()){
                res.add(arr);
            }
        }
        return res;
    }
    private List<int[]> filterInvalidColCombination(List<int[]> colIndexList) {
        List<int[]> res = new ArrayList();
        for(int[] arr : colIndexList){
            List<Object> items = this.doFilter(new int[]{}, arr);
            if(!items.isEmpty()){
                res.add(arr);
            }
        }
        return res;
    }
}
