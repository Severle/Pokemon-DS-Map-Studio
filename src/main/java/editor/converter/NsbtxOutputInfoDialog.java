
package editor.converter;

import editor.handler.MapData;
import editor.handler.MapEditorHandler;
import formats.nsbtx2.Nsbtx2;
import formats.nsbtx2.NsbtxImd;
import formats.nsbtx2.NsbtxPanel;
import lombok.extern.log4j.Log4j2;
import tileset.TilesetMaterial;
import utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode", "FieldCanBeLocal", "unused"})
public class NsbtxOutputInfoDialog extends javax.swing.JDialog {

    private MapEditorHandler handler;

    private ArrayList<Integer> areaIndices;
    private String nsbtxFolderPath;

    private ArrayList<Nsbtx2> nsbtxData;
    private ArrayList<String> errorMsgs;

    private Thread convertingThread;

    private static final Color GREEN = new Color(6, 176, 37);
    private static final Color ORANGE = new Color(255, 106, 0);
    private static final Color RED = Color.red;

    private enum ConvertStatus {
        SUCCESS_STATUS("SUCCESSFULLY CONVERTED", GREEN),
        PALETTE_MISSED_STATUS("CONVERTED (SOME PALETTES NOT CONVERTED)", ORANGE),
        TEXTURE_MISSED_STATUS("CONVERTED (SOME TEXTURES NOT CONVERTED", ORANGE),
        CONVERTER_NOT_FOUND_STATUS("NOT CONVERTED (CONVERTER NOT FOUND)", RED),
        CONVERSION_ERROR_STATUS("NOT CONVERTED (CONVERSION ERROR)", RED),
        IMD_NOT_FOUND_ERROR_STATUS("NOT CONVERTED (IMD NOT FOUND)", RED),
        UNKNOWN_ERROR_STATUS("NOT CONVERTED (UNKNOWN ERROR)", RED),
        MOVE_FILE_ERROR_STATUS("NOT CONVERTED (ERROR MOVING FILE)", RED),
        INTERRUPT_ERROR_STATUS("NOT CONVERTED (ERROR CONVERTING THE MODEL)", RED);

        public final String msg;
        public final Color color;

        ConvertStatus(String msg, Color color) {
            this.msg = msg;
            this.color = color;
        }
    }

    /**
     * Creates new form ImdOutputInfoDialog
     */
    public NsbtxOutputInfoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        getRootPane().setDefaultButton(jbAccept);
        jbAccept.requestFocus();

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(350);

        jTable1.getColumnModel().getColumn(1).setCellRenderer(new StatusColumnCellRenderer());

        jTable1.getSelectionModel().addListSelectionListener(event -> {
            int index = jTable1.getSelectedRow();
            updateView(index);
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({"FieldMayBeFinal", "Convert2MethodRef", "UnnecessaryUnicodeEscape", "DuplicatedCode"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new JPanel();
        jbAccept = new JButton();
        jSplitPane1 = new JSplitPane();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jLabel1 = new JLabel();
        jProgressBar1 = new JProgressBar();
        jLabel2 = new JLabel();
        jlFilesProcessed = new JLabel();
        jLabel4 = new JLabel();
        jlFilesConverted = new JLabel();
        jLabel8 = new JLabel();
        jlFilesNotConverted = new JLabel();
        jLabel3 = new JLabel();
        jlStatus = new JLabel();
        jLabel5 = new JLabel();
        jlResult = new JLabel();
        jpCard = new JPanel();
        jpDisplay = new JPanel();
        nsbtxPanel1 = new NsbtxPanel();
        jpErrorInfo = new JPanel();
        jScrollPane2 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jLabel6 = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resulting NSBTX files info");
        setModal(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                formWindowActivated(e);
            }
            @Override
            public void windowClosed(WindowEvent e) {
                formWindowClosed(e);
            }
        });
        Container contentPane = getContentPane();

        //======== jPanel1 ========
        {
            jPanel1.setLayout(new FlowLayout());

            //---- jbAccept ----
            jbAccept.setText("OK");
            jbAccept.setEnabled(false);
            jbAccept.addActionListener(e -> jbAcceptActionPerformed(e));
            jPanel1.add(jbAccept);
        }

        //======== jSplitPane1 ========
        {
            jSplitPane1.setDividerLocation(460);
            jSplitPane1.setResizeWeight(0.5);

            //======== jPanel2 ========
            {

                //======== jScrollPane1 ========
                {

                    //---- jTable1 ----
                    jTable1.setModel(new DefaultTableModel(
                        new Object[][] {
                        },
                        new String[] {
                            "Name", "Status"
                        }
                    ) {
                        boolean[] columnEditable = new boolean[] {
                            false, false
                        };
                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return columnEditable[columnIndex];
                        }
                    });
                    jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    jScrollPane1.setViewportView(jTable1);
                }

                //---- jLabel1 ----
                jLabel1.setText("NSBMD exporting progress:");

                //---- jLabel2 ----
                jLabel2.setText("Files processed:");

                //---- jlFilesProcessed ----
                jlFilesProcessed.setFont(new Font("Tahoma", Font.BOLD, 11));
                jlFilesProcessed.setText("N/N");

                //---- jLabel4 ----
                jLabel4.setText("Files converted into IMD:");

                //---- jlFilesConverted ----
                jlFilesConverted.setFont(new Font("Tahoma", Font.BOLD, 11));
                jlFilesConverted.setText("N");

                //---- jLabel8 ----
                jLabel8.setText("Files not converted:");

                //---- jlFilesNotConverted ----
                jlFilesNotConverted.setFont(new Font("Tahoma", Font.BOLD, 11));
                jlFilesNotConverted.setText("N");

                //---- jLabel3 ----
                jLabel3.setText("Status:");

                //---- jlStatus ----
                jlStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
                jlStatus.setText("Converting...");

                //---- jLabel5 ----
                jLabel5.setText("Result:");

                //---- jlResult ----
                jlResult.setFont(new Font("Tahoma", Font.BOLD, 12));
                jlResult.setText(" ");

                GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup()
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jlResult, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup()
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jlFilesConverted, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jlFilesNotConverted, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jlStatus, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jlFilesProcessed, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jProgressBar1, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup()
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addGap(21, 21, 21)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jProgressBar1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jlStatus))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jlFilesProcessed))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlFilesConverted))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlFilesNotConverted))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jlResult))
                            .addGap(9, 9, 9))
                );
            }
            jSplitPane1.setLeftComponent(jPanel2);

            //======== jpCard ========
            {
                jpCard.setLayout(new CardLayout());

                //======== jpDisplay ========
                {

                    GroupLayout jpDisplayLayout = new GroupLayout(jpDisplay);
                    jpDisplay.setLayout(jpDisplayLayout);
                    jpDisplayLayout.setHorizontalGroup(
                        jpDisplayLayout.createParallelGroup()
                            .addGroup(jpDisplayLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nsbtxPanel1, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    jpDisplayLayout.setVerticalGroup(
                        jpDisplayLayout.createParallelGroup()
                            .addGroup(jpDisplayLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nsbtxPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }
                jpCard.add(jpDisplay, "CardDisplay");

                //======== jpErrorInfo ========
                {

                    //======== jScrollPane2 ========
                    {

                        //---- jTextArea1 ----
                        jTextArea1.setEditable(false);
                        jTextArea1.setColumns(20);
                        jTextArea1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                        jTextArea1.setRows(5);
                        jTextArea1.setTabSize(3);
                        jScrollPane2.setViewportView(jTextArea1);
                    }

                    //---- jLabel6 ----
                    jLabel6.setText("Error info:");

                    GroupLayout jpErrorInfoLayout = new GroupLayout(jpErrorInfo);
                    jpErrorInfo.setLayout(jpErrorInfoLayout);
                    jpErrorInfoLayout.setHorizontalGroup(
                        jpErrorInfoLayout.createParallelGroup()
                            .addGroup(jpErrorInfoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jpErrorInfoLayout.createParallelGroup()
                                    .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                                    .addGroup(jpErrorInfoLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                    );
                    jpErrorInfoLayout.setVerticalGroup(
                        jpErrorInfoLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, jpErrorInfoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                                .addContainerGap())
                    );
                }
                jpCard.add(jpErrorInfo, "CardErrorInfo");
            }
            jSplitPane1.setRightComponent(jpCard);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSplitPane1))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSplitPane1)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6))
        );
        setSize(910, 490);
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if (convertingThread == null) {
            convertingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    saveAllNsbtx();
                }
            });
            convertingThread.start();
        }
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        convertingThread.interrupt();
    }//GEN-LAST:event_formWindowClosed

    private void jbAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAcceptActionPerformed
        dispose();
    }//GEN-LAST:event_jbAcceptActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel1;
    private JButton jbAccept;
    private JSplitPane jSplitPane1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JLabel jLabel1;
    private JProgressBar jProgressBar1;
    private JLabel jLabel2;
    private JLabel jlFilesProcessed;
    private JLabel jLabel4;
    private JLabel jlFilesConverted;
    private JLabel jLabel8;
    private JLabel jlFilesNotConverted;
    private JLabel jLabel3;
    private JLabel jlStatus;
    private JLabel jLabel5;
    private JLabel jlResult;
    private JPanel jpCard;
    private JPanel jpDisplay;
    private NsbtxPanel nsbtxPanel1;
    private JPanel jpErrorInfo;
    private JScrollPane jScrollPane2;
    private JTextArea jTextArea1;
    private JLabel jLabel6;
    // End of variables declaration//GEN-END:variables

    public void init(MapEditorHandler handler, ArrayList<Integer> areaIndices,
                     String nsbtxFolderPath) {
        this.handler = handler;
        this.areaIndices = areaIndices;
        this.nsbtxFolderPath = nsbtxFolderPath;
    }

    public void saveAllNsbtx() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        nsbtxData = new ArrayList<>(areaIndices.size());
        errorMsgs = new ArrayList<>(areaIndices.size());
        for (int i = 0; i < areaIndices.size(); i++) {
            nsbtxData.add(null);
            errorMsgs.add(null);
        }

        int nFilesProcessed = 0;
        int nFilesConverted = 0;
        int nFilesNotConverted = 0;
        for (Integer areaIndex : areaIndices) {
            ConvertStatus exportStatus;
            boolean paletteMissed = false;
            boolean textureMissed = false;
            if (!Thread.currentThread().isInterrupted()) {
                try {
                    HashSet<Integer> usedMaterialIndices = new HashSet<>();

                    // Add materials always included
                    for (int i = 0; i < handler.getTileset().getMaterials().size(); i++) {
                        if (handler.getTileset().getMaterials().get(i).alwaysIncludeInImd()) {
                            usedMaterialIndices.add(i);
                        }
                    }

                    HashSet<Integer> usedTileIndices = new HashSet<>();
                    for (MapData mapData : handler.getMapMatrix().getMatrix().values()) {
                        if (mapData.getAreaIndex() == areaIndex) {
                            mapData.getGrid().addTileIndicesUsed(usedTileIndices);
                        }
                    }

                    for (Integer tileIndex : usedTileIndices) {
                        ArrayList<Integer> texIDs = handler.getTileset().get(tileIndex).getTextureIDs();
                        usedMaterialIndices.addAll(texIDs);
                    }

                    //Map used for knowing which palette is used by a certain texture
                    //They key is the texture name, the value is the palette name
                    Map<String, String> texPalNames = new HashMap<>();
                    for (Integer matIndex : usedMaterialIndices) {
                        TilesetMaterial mat = handler.getTileset().getMaterial(matIndex);
                        if (!texPalNames.containsKey(mat.getTextureNameImd())) {
                            texPalNames.put(mat.getTextureNameImd(), mat.getPaletteNameImd());
                        }
                    }

                    //Map used for knowing which texture is used by a certain palette
                    //They key is the palette name, the value is the texture name
                    Map<String, String> palTexNames = new HashMap<>();
                    for (Integer matIndex : usedMaterialIndices) {
                        TilesetMaterial mat = handler.getTileset().getMaterial(matIndex);
                        if (!palTexNames.containsKey(mat.getPaletteNameImd())) {
                            palTexNames.put(mat.getPaletteNameImd(), mat.getTextureNameImd());
                        }
                    }

                    Nsbtx2 nsbtx = new Nsbtx2();
                    for (Integer matIndex : usedMaterialIndices) {
                        TilesetMaterial mat = handler.getTileset().getMaterial(matIndex);

                        boolean isTransparent = Utils.hasTransparentColor(mat.getTextureImg());

                        if ((!nsbtx.isTextureNameUsed(mat.getTextureNameImd())
                                && (!nsbtx.isPaletteNameUsed(mat.getPaletteNameImd())))) {
                            nsbtx.addTextureAndPalette(-1, -1,
                                    mat.getTextureImg(),
                                    Nsbtx2.jcbToFormatLookup[mat.getColorFormat()],
                                    isTransparent,
                                    mat.getTextureNameImd(),
                                    mat.getPaletteNameImd()
                            );
                        } else if ((nsbtx.isTextureNameUsed(mat.getTextureNameImd()))
                                && (!nsbtx.isPaletteNameUsed(mat.getPaletteNameImd()))) {
                            try {
                                nsbtx.addPalette(
                                        nsbtx.getTextureNames().indexOf(mat.getTextureNameImd()),
                                        nsbtx.getPaletteNames().indexOf(texPalNames.get(mat.getTextureNameImd())),
                                        mat.getPaletteNameImd(),
                                        mat.getTextureImg());
                            } catch (Exception ex) {
                                paletteMissed = true;
                            }
                        } else if ((!nsbtx.isTextureNameUsed(mat.getTextureNameImd()))
                                && (nsbtx.isPaletteNameUsed(mat.getPaletteNameImd()))) {
                            try {
                                nsbtx.addTexture(
                                        nsbtx.getTextureNames().indexOf(palTexNames.get(mat.getPaletteNameImd())),
                                        nsbtx.getPaletteNames().indexOf(mat.getPaletteNameImd()),
                                        mat.getTextureImg(),
                                        Nsbtx2.jcbToFormatLookup[mat.getColorFormat()],
                                        isTransparent,
                                        mat.getTextureNameImd()
                                );
                            } catch (Exception ex) {
                                textureMissed = true;
                            }
                        }
                    }

                    NsbtxImd imd = new NsbtxImd(nsbtx);

                    String pathSave = nsbtxFolderPath + File.separator + "Area_" + areaIndex + ".imd";
                    imd.saveToFile(pathSave);

                    File file = new File(pathSave);

                    if (file.exists()) {
                        String filename = new File(pathSave).getName();
                        filename = Utils.removeExtensionFromPath(filename);
                        try {
                            String converterPath = "converter/g3dcvtr.exe";
                            String[] cmd;
                            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                                cmd = new String[]{converterPath, pathSave, "-etex", "-o", filename};
                            } else {
                                cmd = new String[]{"wine", converterPath, pathSave, "-etex", "-o", filename};
                                // NOTE: wine call works only with relative path
                            }

                            if (!Files.exists(Paths.get(converterPath))) {
                                throw new IOException();
                            }

                            Process p = new ProcessBuilder(cmd).start();

                            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                            StringBuilder outputString = new StringBuilder();
                            String        line;
                            while ((line = stdError.readLine()) != null) {
                                outputString.append(line).append("\n");
                            }

                            p.waitFor();
                            p.destroy();

                            String nsbPath = Utils.removeExtensionFromPath(pathSave);
                            nsbPath = Utils.addExtensionToPath(nsbPath, "nsbtx");

                            filename = Utils.removeExtensionFromPath(filename);
                            filename = Utils.addExtensionToPath(filename, "nsbtx");

                            System.out.println(System.getProperty("user.dir"));
                            File srcFile = new File(System.getProperty("user.dir") + File.separator + filename);
                            File dstFile = new File(nsbPath);
                            if (srcFile.exists()) {
                                try {
                                    Files.move(srcFile.toPath(), dstFile.toPath(),
                                            StandardCopyOption.REPLACE_EXISTING);
                                    //srcFile.renameTo(new File(nsbPath));
                                    if (paletteMissed) {
                                        exportStatus = ConvertStatus.PALETTE_MISSED_STATUS;
                                    } else if(textureMissed){
                                        exportStatus = ConvertStatus.TEXTURE_MISSED_STATUS;
                                    }else {
                                        exportStatus = ConvertStatus.SUCCESS_STATUS;
                                    }
                                    nFilesConverted++;
                                    nsbtxData.set(nFilesProcessed, nsbtx);
                                } catch (IOException ex) {
                                    nFilesNotConverted++;
                                    exportStatus = ConvertStatus.MOVE_FILE_ERROR_STATUS;
                                    errorMsgs.set(nFilesProcessed, "File was not moved to the save directory. \n"
                                            + "Reopen Pokemon DS Map Studio and try again.");
                                }

                                if (file.exists()) {
                                    try {
                                        Files.delete(file.toPath());
                                    } catch (IOException ex) {
                                        log.warn(ex);
                                    }
                                }
                            } else {
                                nFilesNotConverted++;
                                exportStatus = ConvertStatus.CONVERSION_ERROR_STATUS;
                                errorMsgs.set(nFilesProcessed, "There was a problem creating the NSBMD file. \n"
                                        + "The output from the converter is:\n"
                                        + outputString);
                            }
                        } catch (IOException ex) {
                            nFilesNotConverted++;
                            exportStatus = ConvertStatus.CONVERTER_NOT_FOUND_STATUS;
                            errorMsgs.set(nFilesProcessed,
                                    "The program \"g3dcvtr.exe\" is not found in the \"converter\" folder.\n"
                                            + "Put the program and its *.dll files in the folder and try again.");

                        } catch (InterruptedException ex) {
                            nFilesNotConverted++;
                            exportStatus = ConvertStatus.INTERRUPT_ERROR_STATUS;
                            errorMsgs.set(nFilesProcessed,
                                    "The model was not converted (InterruptedException)");
                        }
                    } else {
                        nFilesNotConverted++;
                        exportStatus = ConvertStatus.UNKNOWN_ERROR_STATUS;
                        errorMsgs.set(nFilesProcessed, "Unknown error");
                    }
                } catch (Exception ex) {
                    nFilesNotConverted++;
                    exportStatus = ConvertStatus.UNKNOWN_ERROR_STATUS;
                    errorMsgs.set(nFilesProcessed, "Unknown error");
                }

                if (nFilesConverted > 0) {
                    jlFilesConverted.setForeground(GREEN);
                }

                if (nFilesNotConverted > 0) {
                    jlFilesNotConverted.setForeground(RED);
                }

                if (nFilesNotConverted > 0) {
                    jlStatus.setForeground(RED);
                    jlStatus.setText("Finished with errors");

                    jlResult.setForeground(RED);
                    jlResult.setText(nFilesNotConverted + " Area(s) could not be converted into NSBTX");
                } else {
                    jlStatus.setForeground(GREEN);
                    jlStatus.setText("Finished");

                    jlResult.setForeground(GREEN);
                    jlResult.setText("All the Areas files have been converted into NSBTX");
                }

                tableModel.addRow(new Object[]{
                        "Area_" + areaIndex,
                        exportStatus
                });

                nFilesProcessed++;

                jlFilesProcessed.setText(nFilesProcessed + "/" + areaIndices.size());
                jlFilesConverted.setText(String.valueOf(nFilesConverted));
                jlFilesNotConverted.setText(String.valueOf(nFilesNotConverted));

                jProgressBar1.setValue((nFilesProcessed * 100) / areaIndices.size());
            }

        }
        jTable1.setRowSelectionInterval(0, 0);
        updateView(0);

        jbAccept.setEnabled(true);

        getRootPane().setDefaultButton(jbAccept);
        jbAccept.requestFocus();
    }

    public void updateView(int index) {
        try {
            CardLayout card = (CardLayout) jpCard.getLayout();
            if (nsbtxData.get(index) != null) {
                card.show(jpCard, "CardDisplay");
                jTextArea1.setText("");

                nsbtxPanel1.setNsbtx(nsbtxData.get(index));
                nsbtxPanel1.updateViewTextureNameList(0);
                nsbtxPanel1.updateViewPaletteNameList(0);
                nsbtxPanel1.updateView();
            } else if (errorMsgs.get(index) != null) {
                card.show(jpCard, "CardErrorInfo");
                jTextArea1.setText(errorMsgs.get(index));
            } else {
                card.show(jpCard, "CardDisplay");
                jTextArea1.setText("The NSBMD has been exported but it can't be displayed here.");

            }
        } catch (Exception ex) {
            log.warn(ex);
        }
    }

    private static class StatusColumnCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            //Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            ConvertStatus status = (ConvertStatus) value;
            l.setForeground(status.color);
            l.setText(status.msg);

            Font font = l.getFont();
            font = font.deriveFont(
                    Collections.singletonMap(
                            TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD));
            l.setFont(font);

            setHorizontalAlignment(JLabel.CENTER);

            //Return the JLabel which renders the cell.
            return l;

        }
    }

}
