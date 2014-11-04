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
import Main.Fractal;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private int xDiff, yDiff;
    
    private static Thread animator;
    private boolean animating;
    private int drawingMode;
    private int speed;
    private int start;
    
    private String imagePath;
    private BufferedImage imageToEncrypt;
    private BufferedImage encryptedImage;
    private ImageHandler handler;
    private ImageCreator creator;
    private Fractal fractal;
    private int iterations;
    private int[] transf;
    
    /**
     * DrawPanel constructor.
     */
    public DrawPanel(){
        init();
    }
    ImageHandler i = new ImageHandler("C:/Users/Alex/Desktop/face2.jpg");
//    ImageHandler i = new ImageHandler("C:/Users/Alex/Desktop/test4.jpg");
    private BufferedImage img;// = i.getImage();
    
    private void drawImage(Graphics2D g, BufferedImage image){
        g.drawImage(image,x0,y0-image.getHeight()+1,null);
    }
    /**
     * Initialize variables.
     */
    private void init(){
        // Drawing mode 0 means no image drawing at all.
        drawingMode = 0;
        animating = false;
        
        transf = new int[4];
        
        iterations = 1;
//        int[] a = i.getPaddedPixels();
//        int length = i.getPaddedImageLength();
//        
//        ImageCreator u = new ImageCreator(a, length, length);
//        
//        int[] transf = {1,1,1,1};
//        Fractal f = new Fractal(a, length, transf);
//        f.createFractal(8);
////        u = new ImageCreator(f.getNewPixels(), length, length);
////        img = u.createImage();
//        
//        Fractal ff = new Fractal(f.getNewPixels(), length, transf);
//        ff.inverseTransformations();
////        ff.createFractal(1);
//        ff.decrypt(8);
//        u = new ImageCreator(ff.getNewPixels(), length, length);
//        img = u.createImage();
        start = 0;
        // Creating and starting new Thread so we can do animation.
        if (animator == null) {
            animator = new Thread(this, "Animation");
            animator.start();
        }
    }
    
    public int encryptImage(){
        handler = new ImageHandler(getImagePath());
        fractal = new Fractal(handler.getPaddedPixels(),
                              handler.getPaddedImageLength(),
                              getTransf());
        int iterReached = fractal.createFractal(getIterations());
        creator = new ImageCreator(fractal.getNewPixels(), 
                                   handler.getPaddedImageLength(),
                                   handler.getPaddedImageLength());
        setEncryptedImage(creator.createImage());
        return iterReached;
    }
    
    private void fractal(int q){
        int[] a = i.getPaddedPixels();
        int length = i.getPaddedImageLength();
        ImageCreator u;
        
        int[] transf = {1,1,1,1};
        Fractal f = new Fractal(a, length, transf);
        f.createFractal(q);
        u = new ImageCreator(f.getNewPixels(), length, length);
        img = u.createImage();
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
        //drawAxes(g2d);
        //drawPixels(g2d);
        switch (drawingMode){
                case 0: 
                        break;
                case 1: drawImage(g2d, imageToEncrypt);
                        break;
                case 2: drawImage(g2d, encryptedImage);
                        break;
        }
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
            
//            try {
//                for (int i = 1; i <= 7; i++) {
//                    
//                    fractal(i);
                    repaint();
//                    Thread.sleep(1000);
//                }
//                
//                
//            } catch (InterruptedException ex) {
//                ex.printStackTrace(System.err);
//            }
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
    public int getxDiff() {
        return xDiff;
    }

    /**
     * @param xDiff x coordinate of previous mouse location(one tick 
     * from current).
     */
    public void setxDiff(int xDiff) {
        this.xDiff = xDiff;
    }

    /**
     * @return y coordinate of previous mouse location(one tick from current).
     */
    public int getyDiff() {
        return yDiff;
    }

    /**
     * @param yDiff y coordinate of previous mouse location(one tick 
     * from current).
     */
    public void setyDiff(int yDiff) {
        this.yDiff = yDiff;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public void setImageToEncrypt(BufferedImage imageToEncrypt) {
        this.imageToEncrypt = imageToEncrypt;
    }

    public void setEncryptedImage(BufferedImage encryptedImage) {
        this.encryptedImage = encryptedImage;
    }
    
    public void setDrawingMode(int drawingMode) throws IllegalArgumentException{
        if(drawingMode < 0 || drawingMode > 2){
            String err = "Drawing mode must be in 0-2 range!";
            throw new IllegalArgumentException(err);
        } else {
            this.drawingMode = drawingMode;
        }
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    
    public int getIterations() {
        return iterations;
    }
    
    public void setTransf(int i, int transf){
        this.transf[i] = transf;
    }

    public int[] getTransf() {
        return transf;
    }
}
