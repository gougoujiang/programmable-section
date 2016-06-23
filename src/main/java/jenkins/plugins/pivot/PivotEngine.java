/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot;

/**
 *
 * @author zijiang
 */
public interface PivotEngine {
    ResultTable process(PivotContext context);
}
