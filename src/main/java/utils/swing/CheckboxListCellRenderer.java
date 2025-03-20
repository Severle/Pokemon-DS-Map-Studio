
package utils.swing;

import javax.swing.*;
import java.awt.*;

/**
 * @author Trifindo
 */
@SuppressWarnings("unused")
public class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer<Object> {

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setSelected(isSelected);
        setEnabled(list.isEnabled());

        setText(value == null ? "" : value.toString());

        return this;
    }
}
