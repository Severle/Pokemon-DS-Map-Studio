/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor.animationeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Trifindo
 */
public class AnimationDisplay extends javax.swing.JPanel {

    private static final int size = 160;
    private AnimationHandler animHandler;
    private BufferedImage backImg;

    private float scale = 2.0f;

    /**
     * Creates new form AnimationDisplay
     */
    public AnimationDisplay() {
        initComponents();

        this.setPreferredSize(new Dimension(size, size));
        
        backImg = createBackImg();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setForeground(new Color(102, 102, 102));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backImg != null) {
            g.drawImage(backImg, 0, 0, null);
        }

        if (animHandler != null) {
            BufferedImage img = animHandler.getCurrentFrameImage();
            if (img != null) {
                int x = getWidth() / 2 - img.getWidth() / 2;
                int y = getHeight() / 2 - img.getHeight() / 2;
                g.drawImage(img, x, y, null);
            }
        }
    }

    public void init(AnimationHandler animHandler) {
        this.animHandler = animHandler;
    }

    public BufferedImage createBackImg() {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        int tileSize = 8;
        int numCells = size / tileSize;
        Color[] colors = new Color[]{Color.white, Color.lightGray};
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                g.setColor(colors[(i + j) % 2]);
                g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
            }
        }
        return img;
    }

}
