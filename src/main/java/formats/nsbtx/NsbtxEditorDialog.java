package formats.nsbtx;

import editor.handler.MapEditorHandler;
import utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Trifindo, JackHack96
 */
@SuppressWarnings({"SpellCheckingInspection", "unused", "FieldCanBeLocal", "DuplicatedCode"})
public class NsbtxEditorDialog extends JDialog {

    private MapEditorHandler handler;
    private NsbtxHandler nsbtxHandler;

    private boolean textureListEnabled = true;
    private boolean paletteListEnabled = true;
    private boolean colorSpinersEnabled = true;

    public NsbtxEditorDialog(Window owner) {
        super(owner);
        initComponents();

        int min = 0;
        int max = 248;
        int step = 8;

        SpinnerNumberModel modelRed = new SpinnerNumberModel(min, min, max, step);
        SpinnerNumberModel modelGreen = new SpinnerNumberModel(min, min, max, step);
        SpinnerNumberModel modelBlue = new SpinnerNumberModel(min, min, max, step);
        jSpinnerRed.setModel(modelRed);
        jSpinnerGreen.setModel(modelGreen);
        jSpinnerBlue.setModel(modelBlue);
    }

    private void jmOpenNsbtxActionPerformed(ActionEvent e) {
        openNsbtxWithDialog();
    }

    private void jmSaveNsbtxActionPerformed(ActionEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            saveNsbtx();
        }
    }

    private void jmSaveNsbtxAsActionPerformed(ActionEvent e) {
        if (nsbtxHandler != null) {
            saveNsbtxWithDialog();
        }
    }

    private void textureNamesListValueChanged(ListSelectionEvent e) {
        if (nsbtxHandler.getNsbtx() != null && textureListEnabled) {
            nsbtxHandler.setTextureIndexSelected(textureNamesList.getSelectedIndex());
            updateViewPaletteNamesUsingTexNames();
            updateView();
        }
    }

    private void paletteNamesListValueChanged(ListSelectionEvent e) {
        if (nsbtxHandler.getNsbtx() != null && paletteListEnabled) {
            nsbtxHandler.setPaletteIndexSelected(paletteNamesList.getSelectedIndex());
            updateView();
        }
    }

    private void jSpinnerRedStateChanged(ChangeEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            if (colorSpinersEnabled) {
                changeColorUsingSpinnerValue(jSpinnerRed);
            }
        }
    }

    private void jSpinnerGreenStateChanged(ChangeEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            if (colorSpinersEnabled) {
                changeColorUsingSpinnerValue(jSpinnerGreen);
            }
        }
    }

    private void jSpinnerBlueStateChanged(ChangeEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            if (colorSpinersEnabled) {
                changeColorUsingSpinnerValue(jSpinnerBlue);
            }
        }
    }

    private void jButton1ActionPerformed(ActionEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            if (nsbtxHandler.getColorIndexSelected() > 0) {
                moveColor(-1);
            }
        }
    }

    private void jButton2ActionPerformed(ActionEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            if (nsbtxHandler.getColorIndexSelected() < nsbtxHandler.getNumColorsInSelectedPalette() - 1) {
                moveColor(1);
            }
        }
    }

    private void jButton3ActionPerformed(ActionEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            importTextureAndPaletteWithDialog();
            nsbtxDisplay.updateImage();
            nsbtxDisplay.repaint();

            paletteDisplay.updatePalette();
            paletteDisplay.repaint();
            updateViewColorValues();
        }
    }

    private void jButton4ActionPerformed(ActionEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            importTextureOnlyWithDialog();
            nsbtxDisplay.updateImage();
            nsbtxDisplay.repaint();
        }
    }

    private void jButton5ActionPerformed(ActionEvent e) {
        if (nsbtxHandler.getNsbtx() != null) {
            saveTextureWithDialog();
        }
    }

    public void init(MapEditorHandler handler) {
        this.handler = handler;
        this.nsbtxHandler = new NsbtxHandler(handler, this);
        this.nsbtxDisplay.init(nsbtxHandler);
        this.paletteDisplay.init(nsbtxHandler);


    }

    public void updateView() {
        nsbtxDisplay.updateImage();
        nsbtxDisplay.repaint();

        paletteDisplay.updatePalette();
        paletteDisplay.repaint();

        nsbtxHandler.setColorIndexSelected(0);

        updateViewColorValues();

    }

    public void updateViewTextureNames() {
        textureListEnabled = false;
        DefaultListModel<String> demoList = new DefaultListModel<>();
        for (int i = 0; i < nsbtxHandler.getNsbtx().textureNames.size(); i++) {
            String name = nsbtxHandler.getNsbtx().textureNames.get(i);
            demoList.addElement(name);
        }
        textureNamesList.setModel(demoList);
        textureNamesList.setSelectedIndex(nsbtxHandler.getTextureIndexSelected());
        textureListEnabled = true;
    }

    public void updateViewPaletteNames() {
        paletteListEnabled = false;
        DefaultListModel<String> demoList = new DefaultListModel<>();
        for (int i = 0; i < nsbtxHandler.getNsbtx().paletteNames.size(); i++) {
            String name = nsbtxHandler.getNsbtx().paletteNames.get(i);
            demoList.addElement(name);
        }
        paletteNamesList.setModel(demoList);
        paletteNamesList.setSelectedIndex(nsbtxHandler.getPaletteIndexSelected());
        paletteListEnabled = true;
    }

    private void updateViewPaletteNamesUsingTexNames() {
        int index = textureNamesList.getSelectedIndex();
        int paletteIndex;
        String paletteName = nsbtxHandler.getNsbtx().textureNames.get(index);
        if ((paletteIndex = nsbtxHandler.getNsbtx().paletteNames.indexOf(paletteName)) != -1
                || (paletteIndex = nsbtxHandler.getNsbtx().paletteNames.indexOf(setElementInString(paletteName, "_pl"))) != -1) {
            paletteNamesList.setSelectedIndex(paletteIndex);
            paletteNamesList.ensureIndexIsVisible(paletteNamesList.getSelectedIndex());
        }
    }

    public void updateViewColorValues() {
        if (paletteDisplay.palette != null) {
            colorSpinersEnabled = false;
            Color c = paletteDisplay.palette.get(nsbtxHandler.getColorIndexSelected());
            jSpinnerRed.setValue(c.getRed());
            jSpinnerGreen.setValue(c.getGreen());
            jSpinnerBlue.setValue(c.getBlue());
            colorSpinersEnabled = true;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private String setElementInString(String src, String newPart) {
        return src.replaceFirst(" ", newPart).substring(0, Math.min(16, src.length()));
    }

    private void moveColor(int increment) {
        int index1 = nsbtxHandler.getColorIndexSelected();
        int index2 = nsbtxHandler.getColorIndexSelected() + increment;
        nsbtxHandler.getNsbtx().swapColors(
                nsbtxHandler.getTextureIndexSelected(),
                nsbtxHandler.getPaletteIndexSelected(),
                index1, index2);
        nsbtxHandler.incrementColorIndexSelected(increment);
        paletteDisplay.updatePaletteColor(index1);
        paletteDisplay.updatePaletteColor(index2);
        updateViewColorValues();
        paletteDisplay.updatePaletteColor(index1);
        paletteDisplay.updatePaletteColor(index2);
        paletteDisplay.repaint();
        nsbtxDisplay.updateImage();
        nsbtxDisplay.repaint();
    }

    private void changeColorUsingSpinnerValue(JSpinner spinner) {
        int value = (int) spinner.getValue();
        spinner.setValue(value - value % 8);
        if (nsbtxHandler.getNsbtx() != null) {
            Color color = new Color(
                    (int) jSpinnerRed.getValue(),
                    (int) jSpinnerGreen.getValue(),
                    (int) jSpinnerBlue.getValue());
            nsbtxHandler.getNsbtx().setColor(
                    nsbtxHandler.getPaletteIndexSelected(),
                    nsbtxHandler.getColorIndexSelected(),
                    color
            );
            paletteDisplay.updateSelectedPaletteColor();
            paletteDisplay.repaint();
            nsbtxDisplay.updateImage();
            nsbtxDisplay.repaint();
        }
    }

    public void saveTextureWithDialog() {
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastNsbtxDirectoryUsed() != null) {
            fc.setCurrentDirectory(new File(handler.getLastNsbtxDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));
        fc.setApproveButtonText("Save");
        fc.setDialogTitle("Save Texture as PNG");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handler.setLastNsbtxDirectoryUsed(fc.getSelectedFile().getParent());
            try {
                String path = fc.getSelectedFile().getPath();
                path = Utils.addExtensionToPath(path, "png");
                ImageIO.write(nsbtxHandler.getSelectedImage(), "png", new File(path));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Can't save file.",
                        "Error saving image", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveNsbtxWithDialog() {
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastNsbtxDirectoryUsed() != null) {
            fc.setCurrentDirectory(new File(handler.getLastNsbtxDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("NSBTX (*.nsbtx)", Nsbtx.fileExtension));
        fc.setApproveButtonText("Save");
        fc.setDialogTitle("Save NSBTX");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handler.setLastNsbtxDirectoryUsed(fc.getSelectedFile().getParent());
            try {
                String path = fc.getSelectedFile().getPath();
                path = Utils.addExtensionToPath(path, Nsbtx.fileExtension);
                NsbtxWriter.saveNsbtx(nsbtxHandler.getNsbtx(), path);
                nsbtxHandler.setNsbtxPath(path);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Can't save file.",
                        "Error saving NSBTX", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveNsbtx() {
        try {
            String path = nsbtxHandler.getNsbtxPath();
            NsbtxWriter.saveNsbtx(nsbtxHandler.getNsbtx(), path);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Can't save file.",
                    "Error saving NSBTX", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void importTextureOnlyWithDialog() {
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastNsbtxDirectoryUsed() != null) {
            fc.setCurrentDirectory(new File(handler.getLastNsbtxDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));
        fc.setApproveButtonText("Open");
        fc.setDialogTitle("Open PNG Image");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handler.setLastNsbtxDirectoryUsed(fc.getSelectedFile().getParent());
            try {
                BufferedImage img = ImageIO.read(fc.getSelectedFile());

                if (img.getWidth() == nsbtxHandler.getSelectedTextureWidth()
                        && img.getHeight() == nsbtxHandler.getSelectedTextureHeight()) {
                    nsbtxHandler.getNsbtx().importTextureOnly(img,
                            nsbtxHandler.getTextureIndexSelected(),
                            nsbtxHandler.getPaletteIndexSelected());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The image imported does not have the same size as the one in the NSBTX",
                            "Error opening image", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Can't open file.",
                        "Error opening image", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void importTextureAndPaletteWithDialog() {
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastNsbtxDirectoryUsed() != null) {
            fc.setCurrentDirectory(new File(handler.getLastNsbtxDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));
        fc.setApproveButtonText("Open");
        fc.setDialogTitle("Open PNG Image");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handler.setLastNsbtxDirectoryUsed(fc.getSelectedFile().getParent());
            try {
                BufferedImage img = ImageIO.read(fc.getSelectedFile());

                if (img.getWidth() == nsbtxHandler.getSelectedTextureWidth()
                        && img.getHeight() == nsbtxHandler.getSelectedTextureHeight()) {
                    nsbtxHandler.getNsbtx().importTextureAndPalette(img,
                            nsbtxHandler.getTextureIndexSelected(),
                            nsbtxHandler.getPaletteIndexSelected());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The image imported does not have the same size as the one in the NSBTX",
                            "Error opening image", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Can't open file.",
                        "Error opening image", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void openNsbtxWithDialog() {
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastNsbtxDirectoryUsed() != null) {
            fc.setCurrentDirectory(new File(handler.getLastNsbtxDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("NSBTX (*.nsbtx)", "nsbtx"));
        fc.setApproveButtonText("Open");
        fc.setDialogTitle("Open NSBTX Image");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handler.setLastBuildDirectoryUsed(fc.getSelectedFile().getParent());
            try {
                nsbtxHandler.loadNsbtx(fc.getSelectedFile().getPath());
                updateView();
                updateViewTextureNames();
                updateViewPaletteNames();
                updateViewColorValues();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Can't open file.",
                        "Error opening image", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings({"Convert2MethodRef", "Convert2Diamond", "DuplicatedCode", "FieldMayBeFinal", "UnnecessaryUnicodeEscape"})
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jmOpenNsbtx = new JMenuItem();
        jmSaveNsbtx = new JMenuItem();
        jmSaveNsbtxAs = new JMenuItem();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        textureNamesList = new JList<>();
        jPanel2 = new JPanel();
        jScrollPane2 = new JScrollPane();
        paletteNamesList = new JList<>();
        jPanel3 = new JPanel();
        nsbtxDisplay = new NsbtxDisplay();
        jPanel4 = new JPanel();
        paletteDisplay = new PaletteDisplay();
        jLabel1 = new JLabel();
        jSpinnerRed = new JSpinner();
        jSpinnerGreen = new JSpinner();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jSpinnerBlue = new JSpinner();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jLabel4 = new JLabel();
        jPanel5 = new JPanel();
        jButton3 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        jPanel6 = new JPanel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NSBTX Editor (Pokemon Map Tilesets ONLY) - Experimental! Use with caution!");
        setModal(true);
        Container contentPane = getContentPane();

        //======== jMenuBar1 ========
        {

            //======== jMenu1 ========
            {
                jMenu1.setText("File");

                //---- jmOpenNsbtx ----
                jmOpenNsbtx.setText("Open NSBTX...");
                jmOpenNsbtx.addActionListener(e -> jmOpenNsbtxActionPerformed(e));
                jMenu1.add(jmOpenNsbtx);

                //---- jmSaveNsbtx ----
                jmSaveNsbtx.setText("Save NSBTX...");
                jmSaveNsbtx.addActionListener(e -> jmSaveNsbtxActionPerformed(e));
                jMenu1.add(jmSaveNsbtx);

                //---- jmSaveNsbtxAs ----
                jmSaveNsbtxAs.setText("Save NSBTX as...");
                jmSaveNsbtxAs.addActionListener(e -> jmSaveNsbtxAsActionPerformed(e));
                jMenu1.add(jmSaveNsbtxAs);
            }
            jMenuBar1.add(jMenu1);
        }
        setJMenuBar(jMenuBar1);

        //======== jPanel1 ========
        {
            jPanel1.setBorder(new TitledBorder("Texture Names"));

            //======== jScrollPane1 ========
            {
                jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                //---- textureNamesList ----
                textureNamesList.setModel(new AbstractListModel<String>() {
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
                textureNamesList.addListSelectionListener(e -> textureNamesListValueChanged(e));
                jScrollPane1.setViewportView(textureNamesList);
            }

            GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup()
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup()
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                                    .addContainerGap())
            );
        }

        //======== jPanel2 ========
        {
            jPanel2.setBorder(new TitledBorder("Palette Names"));

            //======== jScrollPane2 ========
            {
                jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                //---- paletteNamesList ----
                paletteNamesList.setModel(new AbstractListModel<String>() {
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
                paletteNamesList.addListSelectionListener(e -> paletteNamesListValueChanged(e));
                jScrollPane2.setViewportView(paletteNamesList);
            }

            GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup()
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup()
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane2)
                                    .addContainerGap())
            );
        }

        //======== jPanel3 ========
        {
            jPanel3.setBorder(new TitledBorder("Texture"));

            //======== nsbtxDisplay ========
            {
                nsbtxDisplay.setBorder(new LineBorder(new Color(102, 102, 102)));

                GroupLayout nsbtxDisplayLayout = new GroupLayout(nsbtxDisplay);
                nsbtxDisplay.setLayout(nsbtxDisplayLayout);
                nsbtxDisplayLayout.setHorizontalGroup(
                        nsbtxDisplayLayout.createParallelGroup()
                                .addGap(0, 126, Short.MAX_VALUE)
                );
                nsbtxDisplayLayout.setVerticalGroup(
                        nsbtxDisplayLayout.createParallelGroup()
                                .addGap(0, 126, Short.MAX_VALUE)
                );
            }

            GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup()
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(nsbtxDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel3Layout.setVerticalGroup(
                    jPanel3Layout.createParallelGroup()
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(nsbtxDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //======== jPanel4 ========
        {
            jPanel4.setBorder(new TitledBorder("Palette Display"));

            //======== paletteDisplay ========
            {

                GroupLayout paletteDisplayLayout = new GroupLayout(paletteDisplay);
                paletteDisplay.setLayout(paletteDisplayLayout);
                paletteDisplayLayout.setHorizontalGroup(
                        paletteDisplayLayout.createParallelGroup()
                                .addGap(0, 256, Short.MAX_VALUE)
                );
                paletteDisplayLayout.setVerticalGroup(
                        paletteDisplayLayout.createParallelGroup()
                                .addGap(0, 16, Short.MAX_VALUE)
                );
            }

            //---- jLabel1 ----
            jLabel1.setText("R: ");

            //---- jSpinnerRed ----
            jSpinnerRed.addChangeListener(e -> jSpinnerRedStateChanged(e));

            //---- jSpinnerGreen ----
            jSpinnerGreen.addChangeListener(e -> jSpinnerGreenStateChanged(e));

            //---- jLabel2 ----
            jLabel2.setText("G: ");

            //---- jLabel3 ----
            jLabel3.setText("B: ");

            //---- jSpinnerBlue ----
            jSpinnerBlue.addChangeListener(e -> jSpinnerBlueStateChanged(e));

            //---- jButton1 ----
            jButton1.setText("\u25c4");
            jButton1.addActionListener(e -> jButton1ActionPerformed(e));

            //---- jButton2 ----
            jButton2.setText("\u25ba");
            jButton2.addActionListener(e -> jButton2ActionPerformed(e));

            //---- jLabel4 ----
            jLabel4.setText("Move color:");

            GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup()
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel4Layout.createParallelGroup()
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addComponent(jLabel2)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jSpinnerGreen, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addComponent(jLabel3)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jSpinnerBlue, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addComponent(jLabel1)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jSpinnerRed, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel4)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jButton1)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jButton2))
                                            .addComponent(paletteDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(86, Short.MAX_VALUE))
            );
            jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup()
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(paletteDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel1)
                                            .addComponent(jSpinnerRed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton1)
                                            .addComponent(jButton2)
                                            .addComponent(jLabel4))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jSpinnerGreen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jSpinnerBlue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //======== jPanel5 ========
        {
            jPanel5.setBorder(new TitledBorder("Editor"));

            //---- jButton3 ----
            jButton3.setText("Import Texture and Palette");
            jButton3.addActionListener(e -> jButton3ActionPerformed(e));

            //---- jButton4 ----
            jButton4.setText("Import Texture Only");
            jButton4.addActionListener(e -> jButton4ActionPerformed(e));

            //---- jButton5 ----
            jButton5.setText("Export Image");
            jButton5.addActionListener(e -> jButton5ActionPerformed(e));

            GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup()
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jButton4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup()
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jButton3)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5)
                                    .addContainerGap())
            );
        }

        //======== jPanel6 ========
        {
            jPanel6.setBorder(new TitledBorder("Information"));

            GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
            jPanel6.setLayout(jPanel6Layout);
            jPanel6Layout.setHorizontalGroup(
                    jPanel6Layout.createParallelGroup()
                            .addGap(0, 155, Short.MAX_VALUE)
            );
            jPanel6Layout.setVerticalGroup(
                    jPanel6Layout.createParallelGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jmOpenNsbtx;
    private JMenuItem jmSaveNsbtx;
    private JMenuItem jmSaveNsbtxAs;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JList<String> textureNamesList;
    private JPanel jPanel2;
    private JScrollPane jScrollPane2;
    private JList<String> paletteNamesList;
    private JPanel jPanel3;
    private NsbtxDisplay nsbtxDisplay;
    private JPanel jPanel4;
    private PaletteDisplay paletteDisplay;
    private JLabel jLabel1;
    private JSpinner jSpinnerRed;
    private JSpinner jSpinnerGreen;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JSpinner jSpinnerBlue;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel4;
    private JPanel jPanel5;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JPanel jPanel6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
