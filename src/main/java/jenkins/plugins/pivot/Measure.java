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
public interface Measure {
    Object getValue(List<Object> items);
}
