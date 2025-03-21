package editor.settings;

import editor.MainFrame;
import net.miginfocom.swing.MigLayout;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Trifindo, JackHack96
 */
@SuppressWarnings({"SpellCheckingInspection", "FieldCanBeLocal", "unused"})
public class SettingsDialog extends JDialog {
    public SettingsDialog(Window owner) {
        super(owner);
        initComponents();
        String currentThemeName = MainFrame.preferences.get(ThemeUtil.KEY, ThemeUtil.defaultTheme().name());
        jcmbTheme.setSelectedItem(ThemeUtil.getOrDefault(currentThemeName));
        rootPane.setDefaultButton(okButton);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void okButtonActionPerformed(ActionEvent e) {
        int index = jcmbTheme.getSelectedIndex();
        if (index == -1) return;

        // get selected theme
        var themeEntry = jcmbTheme.getItemAt(index);
        MainFrame.preferences.put(ThemeUtil.KEY, themeEntry.name());
        // JOptionPane.showMessageDialog(this, "Please restart PDSMS!");
        // update theme
        themeEntry.install();

        dispose();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        jcmbTheme = new JComboBox<>();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Settings");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 0 0",
                // columns
                "[grow,fill]",
                // rows
                "[grow,fill]" +
                "[fill]"));

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[grow,fill]",
                    // rows
                    "[]"));

                //---- label1 ----
                label1.setText("Theme:");
                contentPanel.add(label1, "cell 0 0");

                //---- jcmbTheme ----
                jcmbTheme.setModel(new DefaultComboBoxModel<>(ThemeUtil.themeVector()));
                contentPanel.add(jcmbTheme, "cell 1 0");
            }
            dialogPane.add(contentPanel, "cell 0 0");

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    "[fill]"));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, "cell 0 1");
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(220, 140);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel                          label1;
    private JComboBox<ThemeUtil.ThemeEntry> jcmbTheme;
    private JPanel                          buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
