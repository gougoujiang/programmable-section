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
public interface Dimension {
    String getName();
    Object getValue(Object item);
    List<Object> sortValues(List<Object> dimValues);
}
