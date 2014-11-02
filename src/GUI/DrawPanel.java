/*
 * The MIT License
 *
 * Copyright 2014 Aleksandr Šmailov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package GUI;

import ImageProcessing.ImageCreator;
import ImageProcessing.ImageHandler;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * @author Aleksandr Šmailov
 */
public class DrawPanel extends JPanel implements Runnable{
    
    private int width, height;
    private int x0,y0;
    private int xDrag, yDrag;
    
    private static Thread animator;
    private int speed;
    private int start;
    private int[] pixels;
    
    /**
     * DrawPanel constructor.
     */
    public DrawPanel(){
        init();
    }
    ImageHandler i = new ImageHandler("C:/Users/Alex/Desktop/test2.png");
    private BufferedImage img;// = i.getImage();
    private void drawImage(Graphics2D g){
        g.drawImage(img,x0,y0-img.getHeight()+1,null);
    }
    /**
     * Initialize variables.
     */
    private void init(){
        int[] a = i.getPaddedPixels();
        int length = (int) Math.sqrt((double)a.length);
        ImageCreator u = new ImageCreator(a, length, length);
        img = u.createImage();
        for (int k = 0; k < a.length; k++){
            //System.out.println(a[k]);
        }
        start = 0;
        // Creating and starting new Thread so we can do animation.
        if (animator == null) {
            animator = new Thread(this, "Animation");
            animator.start();
        }
    }
    public static Image getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.getRaster().setPixels(0, 0, width, height, pixels);
        return image;
    }
    
    /**
     * Method draws X and Y axes on the JPanel.
     * @param g Graphics2D
     */
    private void drawAxes(Graphics2D g){
        this.width = this.getWidth();
        this.height = this.getHeight();
        // Draw axes.
        // X axis.
        g.drawLine(0, y0, width, y0);
        // Y axis.
        g.drawLine(x0, 0, x0, height);
        // Diameter of Axes centre.
        int diameter;
        // Should be set to odd number, otherwise it will position not in the
        // center.
        diameter = 5;
        Ellipse2D.Double circle;
        circle = new Ellipse2D.Double(x0-diameter/2, y0-diameter/2, 
                                      diameter, diameter);
        g.fill(circle);
    }
    
    /**
     * Execute drawing.
     * @param g Graphics
     */
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        // Enable antialias
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw axes.
        drawAxes(g2d);
        //drawPixels(g2d);
        drawImage(g2d);
    }
    
    /**
     * Calculates starting point of X and Y axes.
     */
    public void calcStartingPoint(){
        if (start == 0){
            this.width = this.getWidth();
            this.height = this.getHeight();
            this.x0 = this.getWidth()/8;
            this.y0 = this.getHeight() - this.getHeight()/8;
        }
        if (start <= 1){
            start++;
        }
    }
    
    /**
     * Overriding this so we can draw our own Graphic.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Calculating correct starting point.
        calcStartingPoint();
        // Do drawing.
        doDrawing(g);
    }
    
    /**
     * Run method.
     */
    @Override
    public void run() {
        while(true){
            repaint();
        }
    }
    
    // Setters and getters.
    
   
    /**
     * @return x0 coordinate on JPanel representing origin point of X and Y
     * intersection.
     */
    public int getX0() {
        return x0;
    }

    /**
     * @param x0 x0 coordinate on JPanel representing origin point of X and Y
     * intersection.
     */
    public void setX0(int x0) {
        this.x0 = x0;
    }

    /**
     * @return y0 coordinate on JPanel representing origin point of X and Y
     * intersection.
     */
    public int getY0() {
        return y0;
    }

    /**
     * @param y0 y0 coordinate on JPanel representing origin point of X and Y
     * intersection.
     */
    public void setY0(int y0) {
        this.y0 = y0;
    }
    
    /**
     * @return speed of animation.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed speed of animation.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return x coordinate of previous mouse location(one tick from current).
     */
    public int getxDrag() {
        return xDrag;
    }

    /**
     * @param xDiff x coordinate of previous mouse location(one tick 
     * from current).
     */
    public void setxDiff(int xDiff) {
        this.xDrag = xDiff;
    }

    /**
     * @return y coordinate of previous mouse location(one tick from current).
     */
    public int getyDrag() {
        return yDrag;
    }

    /**
     * @param yDiff y coordinate of previous mouse location(one tick 
     * from current).
     */
    public void setyDiff(int yDiff) {
        this.yDrag = yDiff;
    }
}
