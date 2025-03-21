package editor.buildingeditor2.animations;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.miginfocom.swing.MigLayout;
import renderer.NitroDisplayGL;
import renderer.ObjectGL;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Trifindo, JackHack96
 */
@Log4j2
@SuppressWarnings({"SpellCheckingInspection", "unused", "FieldCanBeLocal"})
public class AddBuildAnimationDialog extends JDialog {

    public static final int ACEPTED = 0, CANCELED = 1;
    @Getter
    private int returnValue   = CANCELED;
    @Getter
    private int indexSelected = 0;

    private byte[] buildModelData;
    private ArrayList<Integer> buildingAnimationIDs;
    private BuildAnimations buildAnimations;

    private       ArrayList<Integer>   animIconIndices;
    private final ArrayList<ImageIcon> animIcons;

    public AddBuildAnimationDialog(Window owner) {
        super(owner);
        initComponents();

        animIcons = new ArrayList<>(4);
        animIcons.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/NsbcaIcon.png"))));
        animIcons.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/NsbtaIcon.png"))));
        animIcons.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/NsbtpIcon.png"))));
        animIcons.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/NsbmaIcon.png"))));

        animIconIndices = new ArrayList<>();

        jlAnimationsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (index >= 0 && index < animIconIndices.size()) {
                    int animIndex = animIconIndices.get(index);
                    if (animIndex >= 0 && animIndex < animIcons.size()) {
                        label.setIcon(animIcons.get(animIndex));
                    }
                }
                return label;
            }
        });

        nitroDisplayGL.getObjectsGL().add(new ObjectGL());
    }

    private void jlAnimationsListValueChanged(ListSelectionEvent e) {
        indexSelected = jlAnimationsList.getSelectedIndex();

        try {
            byte[] animData = buildAnimations.getAnimations().get(indexSelected).getData();
            ObjectGL object = nitroDisplayGL.getObjectGL(0);
            object.setNsbmdData(buildModelData);
            object.setNsbca(null);
            object.setNsbta(null);
            object.setNsbtp(null);
            object.setNsbva(null);
            switch (buildAnimations.getAnimationType(indexSelected)) {
                case ModelAnimation.TYPE_NSBCA:
                    object.setNsbcaData(animData);
                    break;
                case ModelAnimation.TYPE_NSBTA:
                    object.setNsbtaData(animData);
                    break;
                case ModelAnimation.TYPE_NSBTP:
                    object.setNsbtpData(animData);
                    break;
                case ModelAnimation.TYPE_NSBMA:
                    //object.setNsbData(animData);
                    break;
                case ModelAnimation.TYPE_NSBVA:
                    object.setNsbvaData(animData);
                    break;
            }

            nitroDisplayGL.requestUpdate();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void jbAcceptActionPerformed(ActionEvent e) {
        if (!buildingAnimationIDs.contains(indexSelected)) {
            returnValue = ACEPTED;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "The animation selected is already used by the building.",
                    "Can't add animation", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jbCancelActionPerformed(ActionEvent e) {
        returnValue = CANCELED;
        dispose();
    }

    public void init(byte[] buildModelData, BuildAnimations animations, ArrayList<Integer> buildingAnimationIDs) {
        this.buildModelData = buildModelData;
        this.buildAnimations = animations;
        this.buildingAnimationIDs = buildingAnimationIDs;

        updateViewAnimationsList(0);
    }

    @SuppressWarnings("SameParameterValue")
    private void updateViewAnimationsList(int indexSelected) {
        if (buildAnimations != null) {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<ModelAnimation> animations = buildAnimations.getAnimations();
            animIconIndices = new ArrayList<>(animations.size());
            for (int i = 0; i < animations.size(); i++) {
                names.add(i + ": "
                        + animations.get(i).getName() + " ["
                        + animations.get(i).getAnimationTypeName() + "]");
                animIconIndices.add(animations.get(i).getAnimationType());
            }
            addElementsToList(jlAnimationsList, names, indexSelected);
        }
    }

    private static void addElementsToList(JList<String> list, ArrayList<String> elements, int indexSelected) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String element : elements) {
            listModel.addElement(element);
        }
        list.setModel(listModel);

        indexSelected = Math.max(Math.min(list.getModel().getSize() - 1, indexSelected), 0);
        list.setSelectedIndex(indexSelected);
    }

    @SuppressWarnings({"DataFlowIssue", "DuplicatedCode", "Convert2Diamond", "Convert2MethodRef", "FieldMayBeFinal"})
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        jPanel12 = new JPanel();
        jLabel11 = new JLabel();
        jScrollPane5 = new JScrollPane();
        jlAnimationsList = new JList<>();
        nitroDisplayGL = new NitroDisplayGL();
        jbAccept = new JButton();
        jbCancel = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Animation");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
                // columns
                "[grow,fill]" +
                        "[grow,fill]",
                // rows
                "[grow,fill]" +
                        "[fill]"));

        //======== jPanel12 ========
        {
            jPanel12.setBorder(new TitledBorder("Building Animations (bm_anime.narc)"));
            jPanel12.setLayout(new MigLayout(
                    "insets 0,hidemode 3,gap 5 5",
                    // columns
                    "[fill]" +
                            "[grow,fill]",
                    // rows
                    "[fill]" +
                            "[grow,fill]"));

            //---- jLabel11 ----
            jLabel11.setIcon(new ImageIcon(getClass().getResource("/icons/AnimationIcon.png")));
            jLabel11.setText("Animations:");
            jLabel11.setToolTipText("");
            jPanel12.add(jLabel11, "cell 0 0");

            //======== jScrollPane5 ========
            {
                jScrollPane5.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                //---- jlAnimationsList ----
                jlAnimationsList.setModel(new AbstractListModel<String>() {
                    String[] values = {

                    };

                    @Override
                    public int getSize() {
                        return values.length;
                    }

                    @Override
                    public String getElementAt(int i) {
                        return values[i];
                    }
                });
                jlAnimationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jlAnimationsList.addListSelectionListener(e -> jlAnimationsListValueChanged(e));
                jScrollPane5.setViewportView(jlAnimationsList);
            }
            jPanel12.add(jScrollPane5, "cell 0 1");

            //======== nitroDisplayGL ========
            {
                nitroDisplayGL.setBorder(new LineBorder(new Color(102, 102, 102)));

                GroupLayout nitroDisplayGLLayout = new GroupLayout(nitroDisplayGL);
                nitroDisplayGL.setLayout(nitroDisplayGLLayout);
                nitroDisplayGLLayout.setHorizontalGroup(
                        nitroDisplayGLLayout.createParallelGroup()
                                .addGap(0, 144, Short.MAX_VALUE)
                );
                nitroDisplayGLLayout.setVerticalGroup(
                        nitroDisplayGLLayout.createParallelGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                );
            }
            jPanel12.add(nitroDisplayGL, "cell 1 1");
        }
        contentPane.add(jPanel12, "cell 0 0 2 1");

        //---- jbAccept ----
        jbAccept.setText("OK");
        jbAccept.addActionListener(e -> jbAcceptActionPerformed(e));
        contentPane.add(jbAccept, "cell 0 1");

        //---- jbCancel ----
        jbCancel.setText("Cancel");
        jbCancel.addActionListener(e -> jbCancelActionPerformed(e));
        contentPane.add(jbCancel, "cell 1 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel jPanel12;
    private JLabel jLabel11;
    private JScrollPane jScrollPane5;
    private JList<String> jlAnimationsList;
    private NitroDisplayGL nitroDisplayGL;
    private JButton jbAccept;
    private JButton jbCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
