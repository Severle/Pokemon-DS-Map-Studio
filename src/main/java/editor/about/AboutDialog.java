package editor.about;

import java.util.*;
import editor.handler.MapEditorHandler;
import lombok.extern.log4j.Log4j2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Trifindo, JackHack96
 */
@SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
@Log4j2
public class AboutDialog extends JDialog {
    public AboutDialog(Window owner) {
        super(owner);
        initComponents();
        jlVersionName.setText(MapEditorHandler.versionName);

        getRootPane().setDefaultButton(jbOk);
        jbOk.requestFocus();
    }

    private void jlWebsiteClick(MouseEvent ignored) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Trifindo/Pokemon-DS-Map-Studio"));
        } catch (IOException | URISyntaxException e1) {
            log.warn(e1);
        }
    }

    private void jbOkClick(ActionEvent ignored) {
        dispose();
    }

    @SuppressWarnings({"DataFlowIssue", "UnnecessaryUnicodeEscape", "Convert2MethodRef"})
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("lang.appAbout");
        jlVersionName = new JLabel();
        jlAuthor = new JLabel();
        j1 = new JLabel();
        jlWebsite = new JLabel();
        klTrifindo = new JLabel();
        jScrollPane1 = new JScrollPane();
        jtDescription = new JTextArea();
        panel2 = new JPanel();
        jlCredits = new JLabel();
        panel1 = new JPanel();
        jbOk = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("AboutDialog.this.title"));
        setModal(true);
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "insets 5,hidemode 3,gap 5 5",
            // columns
            "[fill]" +
            "[grow,fill]",
            // rows
            "[fill]" +
            "[fill]" +
            "[]" +
            "[]" +
            "[fill]" +
            "[grow,fill]" +
            "[80]" +
            "[49]"));

        //---- jlVersionName ----
        jlVersionName.setFont(new Font("Tahoma", Font.BOLD, 18));
        jlVersionName.setHorizontalAlignment(SwingConstants.CENTER);
        jlVersionName.setText("Pokemon DS Map Studio 2.1");
        contentPane.add(jlVersionName, "cell 0 0 2 1");

        //---- jlAuthor ----
        jlAuthor.setText("<html>-- by <b style=\"color:#06B006\";>Trifindo</b> --</html>");
        jlAuthor.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(jlAuthor, "cell 0 1 2 1");

        //---- j1 ----
        j1.setText(bundle.getString("AboutDialog.cn-author"));
        j1.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(j1, "cell 0 3 2 1");

        //---- jlWebsite ----
        jlWebsite.setText(bundle.getString("AboutDialog.official-website"));
        jlWebsite.setHorizontalAlignment(SwingConstants.CENTER);
        jlWebsite.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jlWebsite.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jlWebsiteClick(e);
            }
        });
        contentPane.add(jlWebsite, "cell 0 4 2 1");

        //---- klTrifindo ----
        klTrifindo.setHorizontalAlignment(SwingConstants.CENTER);
        klTrifindo.setIcon(new ImageIcon(getClass().getResource("/icons/trifindo.gif")));
        contentPane.add(klTrifindo, "cell 0 5");

        //======== jScrollPane1 ========
        {

            //---- jtDescription ----
            jtDescription.setEditable(false);
            jtDescription.setColumns(20);
            jtDescription.setRows(5);
            jtDescription.setText(bundle.getString("AboutDialog.jtDescription.text"));
            jtDescription.setLineWrap(true);
            jtDescription.setFont(UIManager.getFont("TextArea.font"));
            jtDescription.setWrapStyleWord(true);
            jScrollPane1.setViewportView(jtDescription);
        }
        contentPane.add(jScrollPane1, "cell 1 5");

        //======== panel2 ========
        {
            panel2.setLayout(new BorderLayout());

            //---- jlCredits ----
            jlCredits.setForeground(UIManager.getColor("Label.foreground"));
            jlCredits.setText("<html>\n<table>\t\n\t<tr><th><p style=\"color:#F07903\";>JackHack96</p></th><th><p style=\"color:#333FFF\";>Mikelan98</p></th><th><p style=\"color:#898989\";>Jay</p></th><th><p style=\"color:#CE0D0D\";>BagBoy</p></th></tr>\n\t<tr><th><p style=\"color:#F00303\";>Driox</p></th><th><p style=\"color:#AD7F36\";>Jiboule</p></th><th><p style=\"color:#3B41A06\";>Brom</p></th><th><p style=\"color:#FF960B\";>Gonhex</p></th></tr>\n\t<tr><th><p style=\"color:#2F9B0F\";>Nextworld</p></th><th><p style=\"color:#603294\";>AdAstra</p></th><th><p style=\"color:#328D94\";>Monkeyboy0</p></th><th><p style=\"color:#B3AF2E\";>turtleisaac</p></th></tr>\n\t<tr><th><p style=\"color:#7AB006\";>Ren\u00e9</p></th><th><p style=\"color:#7AB0F6\";>Silast</p></th> </tr>\n</table>\n</html>");
            jlCredits.setBorder(new TitledBorder(bundle.getString("AboutDialog.credits")));
            panel2.add(jlCredits, BorderLayout.CENTER);
        }
        contentPane.add(panel2, "cell 1 6");

        //======== panel1 ========
        {
            panel1.setLayout(new FlowLayout());

            //---- jbOk ----
            jbOk.setText("OK");
            jbOk.addActionListener(e -> jbOkClick(e));
            panel1.add(jbOk);
        }
        contentPane.add(panel1, "cell 0 7 2 1");
        setSize(635, 425);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel jlVersionName;
    private JLabel jlAuthor;
    private JLabel j1;
    private JLabel jlWebsite;
    private JLabel klTrifindo;
    private JScrollPane jScrollPane1;
    private JTextArea jtDescription;
    private JPanel panel2;
    private JLabel jlCredits;
    private JPanel panel1;
    private JButton jbOk;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
