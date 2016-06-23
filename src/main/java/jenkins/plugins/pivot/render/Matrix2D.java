/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.render;

import java.util.ArrayList;
import java.util.List;

/**
 * 2-dimension matrix utility
 * @author zijiang
 */
public final class Matrix2D {
    private int[][] data;
    private int rowSize;
    private int colSize;
    
    public Matrix2D(int rowsize, int colsize){
        init(rowsize, colsize);
    }
    
    public Matrix2D(int[][] datasource){
        throw new UnsupportedOperationException();
    }
    
    public Matrix2D(List<int[]> datasource){
        if(datasource.isEmpty()){
            this.init(0, 0);
            return;
        }
        
        int rowsize = datasource.size();
        int colsize = datasource.get(0).length;
        init(rowsize, colsize);
        
        // fill data
        for(int i = 0 ; i < rowsize; i++){
            int[] row = datasource.get(i);
            for(int j = 0; j < row.length; j++){
                set(i, j, row[j]);
            }
        }
    }
    
    public Matrix2D transpose(){
        Matrix2D m = new Matrix2D(this.colSize, this.rowSize);
        for(int i = 0; i < m.rowSize; i++){
            for(int j = 0; j < m.colSize; j++){
                m.set(i, j, this.get(j, i));
            }
        }
        return m;
    }
    
    // Fill all cells with single value
    public void unifiy(int value){
        for(int i = 0; i < this.rowSize; i++){
            for(int j = 0; j < this.colSize; j++){
                this.set(i, j, value);
            }
        }
    }
    
    public int getRowSize(){
        return this.rowSize;
    }
    
    public int getColumnSize(){
        return this.colSize;
    }
    
    private void init(int row, int col){
        this.rowSize = row;
        this.colSize = col;
        this.data = new int[row][col];
    }

    public void set(int rowIndex, int colIndex, int value) {
        this.data[rowIndex][colIndex] = value;
    }
    
    public int get(int rowIndex, int colIndex){
        return this.data[rowIndex][colIndex];
    }
    
    public void dump(){
        for(int i = 0; i < this.getRowSize(); i++){
            for(int j = 0; j < this.getColumnSize(); j++){
                System.out.print(this.get(i, j));
                System.out.print("  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

