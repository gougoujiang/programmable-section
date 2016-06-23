/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.impl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zijiang
 */
class QuickCombiner {
    private final List<? extends List> ll;
    int[] counter;
    int counterIndex;
    public QuickCombiner(List<? extends List> ll){
        this.ll = ll;

        this.counter = new int[ll.size()];
        for(int i = 0; i < ll.size(); i++){
            counter[i] = 0;
        }
        this.counterIndex = ll.size() - 1;
    }

    public List<int[]> run(){
        List<int[]> result = new ArrayList();

        long rowCount = 1;
        for(int i = 0; i < ll.size(); i++){
            rowCount *= ll.get(i).size();
        }

        for(int i = 0; i < rowCount; i++){
            int[] combineRow = new int[ll.size()];
            for(int j = 0; j < ll.size(); j++){
                combineRow[j] = counter[j];
            }

            handle();
            result.add(combineRow);
        }
        return result;
    }

    private void handle() {  
        counter[counterIndex]++;  
        if (counter[counterIndex] >= ll.get(counterIndex).size()) {  
            counter[counterIndex] = 0;  
            counterIndex--;  
            if (counterIndex >= 0) {  
                handle();  
            }  
            counterIndex = ll.size() - 1;  
        }  
    }
}
