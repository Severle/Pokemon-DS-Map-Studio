/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor.collisions;

import editor.bdhc.Bdhc;
import editor.handler.MapEditorHandler;
import editor.state.CollisionLayerState;
import editor.state.StateHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import utils.Utils;

/**
 *
 * @author Trifindo
 */
public class CollisionsEditorDialog extends javax.swing.JDialog {

    private MapEditorHandler handler;
    private CollisionHandler collisionHandler;
    
    
    /**
     * Creates new form PermissionsEditorDialog
     */
    public CollisionsEditorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        System.out.println("new collision editor dialog");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        collisionLayerSelector = new CollisionLayerSelector();
        jPanel2 = new javax.swing.JPanel();
        collisionsDisplay = new CollisionsDisplay();
        jPanel3 = new javax.swing.JPanel();
        collisionsTypesDisplay = new CollisionsTypesSelector();
        jPanel4 = new javax.swing.JPanel();
        jtfCollisionType = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jPanel6 = new javax.swing.JPanel();
        jbImportCollisions = new JButton();
        jbExportCollisions = new JButton();
        jPanel7 = new javax.swing.JPanel();
        jbUndo = new JButton();
        jbRedo = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Collision Editor (BW and BW2 can be bugged)");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Collision Layers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        javax.swing.GroupLayout collisionLayerSelectorLayout = new javax.swing.GroupLayout(collisionLayerSelector);
        collisionLayerSelector.setLayout(collisionLayerSelectorLayout);
        collisionLayerSelectorLayout.setHorizontalGroup(
            collisionLayerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );
        collisionLayerSelectorLayout.setVerticalGroup(
            collisionLayerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 84, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collisionLayerSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collisionLayerSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Collision Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        collisionsDisplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout collisionsDisplayLayout = new javax.swing.GroupLayout(collisionsDisplay);
        collisionsDisplay.setLayout(collisionsDisplayLayout);
        collisionsDisplayLayout.setHorizontalGroup(
            collisionsDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );
        collisionsDisplayLayout.setVerticalGroup(
            collisionsDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(collisionsDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(collisionsDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Collision Type Selector", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        collisionsTypesDisplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout collisionsTypesDisplayLayout = new javax.swing.GroupLayout(collisionsTypesDisplay);
        collisionsTypesDisplay.setLayout(collisionsTypesDisplayLayout);
        collisionsTypesDisplayLayout.setHorizontalGroup(
            collisionsTypesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 126, Short.MAX_VALUE)
        );
        collisionsTypesDisplayLayout.setVerticalGroup(
            collisionsTypesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(collisionsTypesDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(collisionsTypesDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Collision Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        jtfCollisionType.setEditable(false);
        jtfCollisionType.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtfCollisionType)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtfCollisionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opacity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        jSlider1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Collision File", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        jbImportCollisions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ImportTileIcon.png"))); // NOI18N
        jbImportCollisions.setText("Import");
        jbImportCollisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbImportCollisionsActionPerformed(evt);
            }
        });

        jbExportCollisions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ExportIcon.png"))); // NOI18N
        jbExportCollisions.setText("Export");
        jbExportCollisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExportCollisionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbImportCollisions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbExportCollisions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbImportCollisions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbExportCollisions)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Controls", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));

        jbUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/undoIcon.png"))); // NOI18N
        jbUndo.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/undoDisabledIcon.png"))); // NOI18N
        jbUndo.setEnabled(false);
        jbUndo.setFocusPainted(false);
        jbUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbUndoActionPerformed(evt);
            }
        });

        jbRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/redoIcon.png"))); // NOI18N
        jbRedo.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/redoDisabledIcon.png"))); // NOI18N
        jbRedo.setEnabled(false);
        jbRedo.setFocusPainted(false);
        jbRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRedoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbRedo)
                    .addComponent(jbUndo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        float value = jSlider1.getValue() / 100f;
        collisionsDisplay.transparency = value;
        collisionsDisplay.repaint();
    }//GEN-LAST:event_jSlider1StateChanged

    private void jbImportCollisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImportCollisionsActionPerformed
        openCollisionWithDialog();
        
    }//GEN-LAST:event_jbImportCollisionsActionPerformed

    private void jbExportCollisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExportCollisionsActionPerformed
        saveCollisionWithDialog();
    }//GEN-LAST:event_jbExportCollisionsActionPerformed

    private void jbUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbUndoActionPerformed
        undoState();
    }//GEN-LAST:event_jbUndoActionPerformed

    private void jbRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRedoActionPerformed
        redoState();
    }//GEN-LAST:event_jbRedoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private CollisionLayerSelector collisionLayerSelector;
    private CollisionsDisplay collisionsDisplay;
    private CollisionsTypesSelector collisionsTypesDisplay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSlider jSlider1;
    private JButton jbExportCollisions;
    private JButton jbImportCollisions;
    private JButton jbRedo;
    private JButton jbUndo;
    private javax.swing.JTextField jtfCollisionType;
    // End of variables declaration//GEN-END:variables

    public void init(MapEditorHandler handler, BufferedImage mapImage){
        //CollisionTypes colors = new CollisionTypes();
        this.handler = handler;
        collisionHandler = new CollisionHandler(handler, this);
        collisionsDisplay.init(handler, mapImage, collisionHandler);
        collisionsTypesDisplay.init(collisionHandler);
        collisionLayerSelector.init(collisionHandler);
        
        updateView();
    }
    
    public void updateView(){
        updateViewCollisionTypeName();
    }
    
    public void updateViewCollisionTypeName(){
        jtfCollisionType.setText(collisionHandler.getCollisionNameSelected());
    }
    
    public void openCollisionWithDialog(){
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastCollisionsDirectoryUsed()!= null) {
            fc.setCurrentDirectory(new File(handler.getLastCollisionsDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("Collision File (*.per)", Collisions.fileExtension));
        fc.setApproveButtonText("Open");
        fc.setDialogTitle("Open");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String path = fc.getSelectedFile().getPath();
                handler.setLastCollisionsDirectoryUsed(fc.getSelectedFile().getParent());
                
                handler.setCollisions(new Collisions(path));
                
                collisionHandler.resetMapStateHandler();
                updateView();
                collisionsDisplay.repaint();
                collisionLayerSelector.drawAllLayers();
                collisionLayerSelector.repaint();
                collisionsTypesDisplay.repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Can't open file", "Error collision file", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }
    
    private void saveCollisionWithDialog() {
        final JFileChooser fc = new JFileChooser();
        if (handler.getLastCollisionsDirectoryUsed() != null) {
            fc.setCurrentDirectory(new File(handler.getLastCollisionsDirectoryUsed()));
        }
        fc.setFileFilter(new FileNameExtensionFilter("Collision File (*.per)", Collisions.fileExtension));
        fc.setApproveButtonText("Save");
        fc.setDialogTitle("Save");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handler.setLastMapDirectoryUsed(fc.getSelectedFile().getParent());
            try {
                String path = fc.getSelectedFile().getPath();
                path = Utils.addExtensionToPath(path, Collisions.fileExtension);
                
                handler.getCollisions().saveToFile(path);
                JOptionPane.showMessageDialog(this, "Collision succesfully exported.", 
                        "Collisions saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex ) {
                JOptionPane.showMessageDialog(this, "Can't save file.", 
                        "Error saving collision", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void repaintDisplay(){
        collisionsDisplay.repaint();
    }
    
    public void repaintTypesDisplay(){
        collisionsTypesDisplay.repaint();
    }
    
    public void repaintLayerSelector(){
        collisionLayerSelector.repaint();
    }
    
    public void redrawSelectedLayerInSelector(){
        collisionLayerSelector.drawLayer(collisionHandler.getIndexLayerSelected());
    }
    
    public void undoState(){
        StateHandler collStateHandler = collisionHandler.getCollisionStateHandler();
        if (collStateHandler.canGetPreviousState()) {
            CollisionLayerState state = (CollisionLayerState) collStateHandler.getPreviousState(new CollisionLayerState("Collision Edit", collisionHandler));
            state.revertState();
            jbRedo.setEnabled(true);
            if (!collStateHandler.canGetPreviousState()) {
                jbUndo.setEnabled(false);
            }
            collisionsDisplay.repaint();
            collisionLayerSelector.drawLayer(state.getLayerIndex());
            collisionLayerSelector.repaint();
        }
    }
    
    public void redoState(){
        StateHandler collStateHandler = collisionHandler.getCollisionStateHandler();
        if (collStateHandler.canGetNextState()) {
            CollisionLayerState state = (CollisionLayerState) collStateHandler.getNextState();
            state.revertState();
            jbUndo.setEnabled(true);
            collisionsDisplay.repaint();
            collisionLayerSelector.drawLayer(state.getLayerIndex());
            collisionLayerSelector.repaint();
            if (!collStateHandler.canGetNextState()) {
                jbRedo.setEnabled(false);
            }
        }
    }
    
    /*
    public void undoMapState() {
        StateHandler mapStateHandler = handler.getMapStateHandler();
        if (mapStateHandler.canGetPreviousState()) {
            MapLayerState state = (MapLayerState) mapStateHandler.getPreviousState(new MapLayerState("Map Edit", handler));
            state.revertState();
            jbRedo.setEnabled(true);
            if (!mapStateHandler.canGetPreviousState()) {
                jbUndo.setEnabled(false);
            }
            mapDisplay.repaint();
            thumbnailLayerSelector.drawLayerThumbnail(state.getLayerIndex());
            thumbnailLayerSelector.repaint();
        }
    }

    public void redoMapState() {
        StateHandler mapStateHandler = handler.getMapStateHandler();
        if (mapStateHandler.canGetNextState()) {
            MapLayerState state = (MapLayerState) mapStateHandler.getNextState();
            state.revertState();
            jbUndo.setEnabled(true);
            mapDisplay.repaint();
            thumbnailLayerSelector.drawLayerThumbnail(state.getLayerIndex());
            thumbnailLayerSelector.repaint();
            if (!mapStateHandler.canGetNextState()) {
                jbRedo.setEnabled(false);
            }
        }
    }
    */
    
    public JButton getUndoButton(){
        return jbUndo;
    }
    
    public JButton getRedoButton(){
        return jbRedo;
    }
    
}
