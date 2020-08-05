/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor.gameselector;

import editor.MainFrame;
import editor.TilesetRenderer;
import editor.game.Game;
import editor.handler.MapEditorHandler;
import editor.smartdrawing.SmartGrid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import tileset.TextureNotFoundException;
import tileset.Tileset;
import tileset.TilesetIO;
import utils.Utils;

/**
 *
 * @author Trifindo
 */
public class GameTsetSelectorDialog extends javax.swing.JDialog {

    private MapEditorHandler handler;
    public static final int ACEPTED = 0, CANCELED = 1;
    private int returnValue = CANCELED;
    private int newGame = Game.DIAMOND;

    //Separator 
    private static final String s = "/";

    private static final String rootFolderPath = "/" + "tilesets";
    private static final String none = "None";
    private ArrayList<String> folderPaths;
    private ArrayList<ArrayList<String>> subfolderPaths;
    private ArrayList<ArrayList<String>> tsetNames;

    /**
     * Creates new form GameTsetSelectorDialog
     */
    public GameTsetSelectorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        
        //Load folder paths
        folderPaths = getSubfolderNamesAsResource(rootFolderPath);
        folderPaths.add(none);

        //Load subfolder paths
        subfolderPaths = new ArrayList<>(folderPaths.size());
        for (int i = 0; i < folderPaths.size() - 1; i++) {
            ArrayList<String> subfolders = new ArrayList<>();
            subfolders = getSubfolderNamesAsResource(rootFolderPath + s + folderPaths.get(i));
            subfolderPaths.add(subfolders);
        }

        //Load tileset filenames
        tsetNames = new ArrayList<>(subfolderPaths.size());
        for (int i = 0; i < subfolderPaths.size(); i++) {
            ArrayList<String> subfiles = new ArrayList<>();
            for (int j = 0; j < subfolderPaths.get(i).size(); j++) {
                ArrayList<String> names;
                names = getSubfileNamesAsResource(rootFolderPath + s
                        + folderPaths.get(i) + s + subfolderPaths.get(i).get(j));
                if (names != null) {
                    subfiles.add(names.get(0));
                }
            }
            tsetNames.add(subfiles);
        }

        //Remove null folders and files
        for (int i = 0; i < subfolderPaths.size() - 1; i++) {
            if (subfolderPaths.get(i) == null) {
                subfolderPaths.remove(i);
                tsetNames.remove(i);
                i--;
            } else if (subfolderPaths.get(i).size() < 1) {
                subfolderPaths.remove(i);
                tsetNames.remove(i);
                i--;
            } else {
                for (int j = 0; j < subfolderPaths.get(i).size(); j++) {
                    if (subfolderPaths.get(i).get(j) == null) {
                        subfolderPaths.get(i).remove(j);
                        tsetNames.get(i).remove(j);
                        j--;
                    }
                }
                if (subfolderPaths.get(i).size() < 1) {
                    subfolderPaths.remove(i);
                    tsetNames.remove(i);
                    i--;
                }
            }
        }
        

        addItemsToJList(jlTsetFolder, folderPaths);
        jlTsetFolder.setSelectedIndex(0);
        addItemsToJList(jlTsetName, subfolderPaths.get(0));
        jlTsetName.setSelectedIndex(0);

        jScrollPane4.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanelIcon = new javax.swing.JPanel();
        jlGameIcon = new javax.swing.JLabel();
        jbFinish = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlGame = new JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlTsetFolder = new JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jlTsetName = new JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        tilesetThumbnailDisplay = new TilesetThumbnailDisplay();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Map - Select Game and Tileset");
        setResizable(false);

        jpanelIcon.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jlGameIcon.setMaximumSize(new java.awt.Dimension(32, 32));
        jlGameIcon.setMinimumSize(new java.awt.Dimension(32, 32));
        jlGameIcon.setPreferredSize(new java.awt.Dimension(32, 32));

        javax.swing.GroupLayout jpanelIconLayout = new javax.swing.GroupLayout(jpanelIcon);
        jpanelIcon.setLayout(jpanelIconLayout);
        jpanelIconLayout.setHorizontalGroup(
            jpanelIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelIconLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlGameIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpanelIconLayout.setVerticalGroup(
            jpanelIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelIconLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlGameIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jbFinish.setText("Finish");
        jbFinish.setToolTipText("");
        jbFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFinishActionPerformed(evt);
            }
        });

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        jLabel1.setText("Game:");

        jLabel2.setText("Tileset folders:");

        jLabel3.setText("Tileset name:");

        jlGame.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Pokemon Diamond", "Pokemon Pearl", "Pokemon Platinum", "Pokemon Heart Gold", "Pokemon Soul Silver", "Pokemon Black", "Pokemon White", "Pokemon Black 2", "Pokemon White 2" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jlGame.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlGame.setSelectedIndex(0);
        jlGame.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlGameValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jlGame);

        jlTsetFolder.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlTsetFolder.setSelectedIndex(0);
        jlTsetFolder.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlTsetFolderValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jlTsetFolder);

        jlTsetName.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlTsetName.setSelectedIndex(0);
        jlTsetName.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlTsetNameValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jlTsetName);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tilesetThumbnailDisplay.setMaximumSize(new java.awt.Dimension(128, 32767));
        tilesetThumbnailDisplay.setMinimumSize(new java.awt.Dimension(128, 100));
        tilesetThumbnailDisplay.setPreferredSize(new java.awt.Dimension(128, 137));

        javax.swing.GroupLayout tilesetThumbnailDisplayLayout = new javax.swing.GroupLayout(tilesetThumbnailDisplay);
        tilesetThumbnailDisplay.setLayout(tilesetThumbnailDisplayLayout);
        tilesetThumbnailDisplayLayout.setHorizontalGroup(
            tilesetThumbnailDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 132, Short.MAX_VALUE)
        );
        tilesetThumbnailDisplayLayout.setVerticalGroup(
            tilesetThumbnailDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(tilesetThumbnailDisplay);

        jLabel4.setText("Tileset preview:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpanelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(11, 11, 11))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpanelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbFinish))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFinishActionPerformed
        handler.setGameIndex(newGame);

        Tileset tileset;
        int folderIndex = jlTsetFolder.getSelectedIndex();
        int tilesetIndex = jlTsetName.getSelectedIndex();
        if (tilesetIndex < jlTsetFolder.getModel().getSize() - 1) {
            try {
                String path = rootFolderPath + s + folderPaths.get(folderIndex)
                        + s + subfolderPaths.get(folderIndex).get(tilesetIndex)
                        + s + tsetNames.get(folderIndex).get(tilesetIndex);
                System.out.println("Resulting path: " + path);
                tileset = TilesetIO.readTilesetFromFileAsResource(path);
                TilesetRenderer tr = new TilesetRenderer(tileset);
                try {
                    tr.renderTiles();
                } catch (NullPointerException e) {

                }
                tr.destroy();
            } catch (NullPointerException | TextureNotFoundException | IOException | IndexOutOfBoundsException ex) {
                System.out.println("Tileset not found");
                tileset = new Tileset();
                tileset.getSmartGridArray().add(new SmartGrid());
            }
        } else {
            tileset = new Tileset();
            tileset.getSmartGridArray().add(new SmartGrid());
        }
        handler.setTileset(tileset);

        returnValue = ACEPTED;
        dispose();
    }//GEN-LAST:event_jbFinishActionPerformed

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        returnValue = CANCELED;
        dispose();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jlGameValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlGameValueChanged
        newGame = jlGame.getSelectedIndex();
        updateViewGameIcon();
    }//GEN-LAST:event_jlGameValueChanged

    private void jlTsetFolderValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlTsetFolderValueChanged
        if (subfolderPaths != null) {
            if (jlTsetFolder.getSelectedIndex() < jlTsetFolder.getModel().getSize() - 1) {
                addItemsToJList(jlTsetName, subfolderPaths.get(jlTsetFolder.getSelectedIndex()));
                jlTsetName.setSelectedIndex(0);
            } else {
                addItemsToJList(jlTsetName, new ArrayList<>());
                jlTsetName.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_jlTsetFolderValueChanged

    private void jlTsetNameValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlTsetNameValueChanged
        if (subfolderPaths != null && tsetNames != null) {
            loadTilesetThumbnail(jlTsetFolder.getSelectedIndex(), jlTsetName.getSelectedIndex());
        }
    }//GEN-LAST:event_jlTsetNameValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameTsetSelectorDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(GameTsetSelectorDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GameTsetSelectorDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GameTsetSelectorDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GameTsetSelectorDialog dialog = new GameTsetSelectorDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFinish;
    private JList<String> jlGame;
    private javax.swing.JLabel jlGameIcon;
    private JList<String> jlTsetFolder;
    private JList<String> jlTsetName;
    private javax.swing.JPanel jpanelIcon;
    private TilesetThumbnailDisplay tilesetThumbnailDisplay;
    // End of variables declaration//GEN-END:variables

    public void init(MapEditorHandler handler) {
        this.handler = handler;
        updateViewGameIcon();
        loadTilesetThumbnail(0, 0);

    }

    public void updateViewGameIcon() {
        jlGameIcon.setIcon(new ImageIcon(handler.getGame().gameIcons[newGame]));
    }

    public int getReturnValue() {
        return returnValue;
    }

    private void loadTilesetThumbnail(int folderIndex, int tilesetIndex) {
        BufferedImage img;
        try {
            String thumbnailName = "TilesetThumbnail.png";
            System.out.println("Thumbnail name: " + thumbnailName);
            System.out.println("Path name: " + rootFolderPath + s + folderPaths.get(folderIndex) + s + subfolderPaths.get(folderIndex).get(tilesetIndex) + s + thumbnailName);
            img = Utils.loadImageAsResource(rootFolderPath + s + folderPaths.get(folderIndex) + s + subfolderPaths.get(folderIndex).get(tilesetIndex) + s + thumbnailName);
            tilesetThumbnailDisplay.setImage(img);
            tilesetThumbnailDisplay.repaint();
        } catch (IOException | IndexOutOfBoundsException | IllegalArgumentException ex) {
            tilesetThumbnailDisplay.setImage(null);
            tilesetThumbnailDisplay.repaint();
        }

    }

    private void addItemsToJList(JList list, ArrayList<String> items) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String item : items) {
            model.addElement(item.replace("_", " "));
        }
        list.setModel(model);
    }

    private ArrayList<String> getSubfolderNamesAsResource(String root) {
        System.out.println("Folder root: " + root);
        File mainFolder;
        /*
        try {
            mainFloder = new File(this.getClass().getResource(root).toURI());
        } catch (URISyntaxException ex) {*/
        try {
            //mainFolder = new File(MainFrame.class.getClass().getResource(root).getFile());
            mainFolder = new File(MainFrame.class.getResource(root).getFile());
            //mainFolder = getFileFromURL(root);
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }
        //}
        File[] subdirs = mainFolder.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });

        if (subdirs != null) {
            if (subdirs.length > 0) {
                ArrayList<String> paths = new ArrayList<>(subdirs.length);
                for (int i = 0; i < subdirs.length; i++) {
                    System.out.println("    " + subdirs[i].getName());
                    paths.add(subdirs[i].getName());
                }
                return paths;
            }
        }

        return new ArrayList<>();
    }

    private ArrayList<String> getSubfileNamesAsResource(String root) {
        System.out.println("File root folder: " + root);
        File mainFolder;
        /*try {
            mainFloder = new File(this.getClass().getResource(root).toURI());
        } catch (URISyntaxException ex) {*/
        try {
            //mainFloder = new File(MainFrame.class.getClass().getResource(root).getFile());
            mainFolder = new File(MainFrame.class.getResource(root).getFile());
            //mainFolder = getFileFromURL(root);
        } catch (NullPointerException ex) {
            return null;
        }
        //}
        File[] subdirs = mainFolder.listFiles(new FilenameFilter() {
            public boolean accept(File mainFloder, String filename) {
                return filename.endsWith(".pdsts");
            }
        });

        if (subdirs != null) {
            if (subdirs.length > 0) {
                System.out.println("    " + subdirs[0].getName());
                ArrayList<String> paths = new ArrayList<>(subdirs.length);
                for (int i = 0; i < subdirs.length; i++) {
                    //System.out.println(subdirs[i].getName());
                    paths.add(subdirs[i].getName());
                }
                return paths;
            }
        }
        System.out.println("Null :/");
        return null;
    }

    private File getFileFromURL(String path) {
        URL url = this.getClass().getClassLoader().getResource(path);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }

}
