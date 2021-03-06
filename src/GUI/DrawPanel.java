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
import Main.SquareDihedralGroup;
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
    private int animationMode;
    private int drawingMode;
    private int start;
    
    private String imagePath;
    private BufferedImage imageToEncryptDecrypt;
    private BufferedImage encryptedDecryptedImage;
    private int cropWidth;
    private int cropHeight;
    private ImageHandler handler;
    private ImageCreator creator;
    private Fractal fractal;
    private int iterations;
    private int[] transf;
    
    private BufferedImage[] frames;
    private int currFrame;
    /**
     * DrawPanel constructor.
     */
    public DrawPanel(){
        init();
    }
    
    private void drawImage(Graphics2D g, BufferedImage image){
        g.drawImage(image,x0,y0-image.getHeight()+1,null);
    }
    /**
     * Initialize variables.
     */
    private void init(){
        // Drawing mode 0 means no image drawing at all.
        drawingMode = 0;
        animationMode = 0;
        currFrame = 0;
        cropWidth = 0;
        cropHeight = 0;
        
        transf = new int[4];
        
        iterations = 1;
        start = 0;
        // Creating and starting new Thread so we can do animation.
        if (animator == null) {
            animator = new Thread(this, "Animation");
            animator.start();
        }
    }
    
    /**
     * Encrypt image.
     * @return iteration(level) reached.
     */
    public int encryptImage(){
        handler = new ImageHandler(getImagePath());
        fractal = new Fractal(handler.getPaddedPixels(),
                              handler.getPaddedImageLength(),
                              getTransf());
        int iterReached = fractal.createFractal(getIterations());
        creator = new ImageCreator(fractal.getNewPixels(), 
                                   handler.getPaddedImageLength(),
                                   handler.getPaddedImageLength());
        setEncryptedDecryptedImage(creator.createImage());
        return iterReached;
    }
    
    /**
     * Decrypt image.
     * @return iteration(level) reached.
     */
    public int decryptImage(){
        handler = new ImageHandler(getImagePath());
        int[] transforms;
        transforms = SquareDihedralGroup.inverseTransformations(getTransf());
        fractal = new Fractal(handler.getPaddedPixels(),
                              handler.getPaddedImageLength(),
                              transforms);
        int iterReached = fractal.destroyFractal(getIterations());
        creator = new ImageCreator(fractal.getNewPixels(), 
                                   handler.getPaddedImageLength(),
                                   handler.getPaddedImageLength());
        if(getCropWidth() == 0 || getCropHeight() == 0){
            setCropWidth(handler.getPaddedImageLength());
            setCropHeight(handler.getPaddedImageLength());
        }
        BufferedImage img = creator.createImage();
        BufferedImage img2 = handler.cropImage(img, getCropWidth(), 
                                               getCropHeight());
        if(img2 != null){
            setEncryptedDecryptedImage(img2);
        } else {
            setEncryptedDecryptedImage(img);
        }
        return iterReached;
    }
    
    /**
     * Create animation frames.
     */
    public void createFrames(){
        int iter = getIterations();
        frames = new BufferedImage[iter+1];
        frames[0] = imageToEncryptDecrypt;
        // If animation mode 0 then we need to create encryption frames.
        if(getAnimationMode() == 0){
            for(int i = 1; i <= iter; i++){
                fractal.createFractal(i);
                creator = new ImageCreator(fractal.getNewPixels(), 
                                           handler.getPaddedImageLength(),
                                           handler.getPaddedImageLength());
                frames[i] = creator.createImage();
                
            }
        }
        // If animation mode 1 then we need to create decryption frames.
        if(getAnimationMode() == 1){
            for(int i = 1; i <= iter; i++){
                int[] transforms;
                transforms = SquareDihedralGroup.inverseTransformations(
                        getTransf());
                fractal = new Fractal(handler.getPaddedPixels(),
                                      handler.getPaddedImageLength(),
                                      transforms);
                fractal.destroyFractal(i);
                creator = new ImageCreator(fractal.getNewPixels(), 
                                           handler.getPaddedImageLength(),
                                           handler.getPaddedImageLength());
                if(getCropWidth() == 0 || getCropHeight() == 0){
                    setCropWidth(handler.getPaddedImageLength());
                    setCropHeight(handler.getPaddedImageLength());
                }
                BufferedImage img = creator.createImage();
                BufferedImage img2 = handler.cropImage(img,getCropWidth(), 
                                                       getCropHeight());
                if(img2 != null){
                    frames[i] = img2;
                } else {
                    frames[i] = img;
                }
                
            }
        }
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
        // Draws differend images depending on the drawing mode.
        switch (drawingMode){
                case 0: 
                    // No drawing at alltogether.
                        break;
                    // Draw starting image.
                case 1: drawImage(g2d, imageToEncryptDecrypt);
                        break;
                    // Draw encrypted/decrypted image.
                case 2: drawImage(g2d, encryptedDecryptedImage);
                        break;
                    // Draw (animation) current frame.
                case 3: drawImage(g2d, frames[currFrame]);
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
            try {
                repaint();
                if(drawingMode == 3){
                    Thread.sleep(700);
                    if(currFrame != frames.length-1){
                        currFrame++;
                    } else {
                        currFrame = 0;
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }
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

    /**
     * @return image path.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Set image path.
     * @param imagePath image path.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @param imageToEncryptDecrypt image we want to encrypt or decrypt.
     */
    public void setImageToEncryptDecrypt(BufferedImage imageToEncryptDecrypt) {
        this.imageToEncryptDecrypt = imageToEncryptDecrypt;
    }

    /**
     * @param encryptedDecryptedImage encrypted/decrypted image.
     */
    private void setEncryptedDecryptedImage(
            BufferedImage encryptedDecryptedImage) {
        this.encryptedDecryptedImage = encryptedDecryptedImage;
    }
    
    /**
     * @return encrypted/decrypted image.
     */
    public BufferedImage getEncryptedDecryptedImage() {
        return encryptedDecryptedImage;
    }
    
    /**
     * Sets drawing mode. (0 - nothing, 1 - starting image, 2 - encrypted or
     * decrypted image, 3 - animation)
     * @param drawingMode drawing mode.
     * @throws IllegalArgumentException
     */
    public void setDrawingMode(int drawingMode) throws IllegalArgumentException{
        if(drawingMode < 0 || drawingMode > 3){
            String err = "Drawing mode must be in 0-3 range!";
            throw new IllegalArgumentException(err);
        } else {
            this.drawingMode = drawingMode;
        }
    }

    /**
     * Set number of iterations we want to do.
     * @param iterations number of iterations.
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    
    /**
     * @return number of iterations we want to do.
     */
    public int getIterations() {
        return iterations;
    }
    
    /**
     * Set specific transformation at {@link DrawPanel#transf} array.
     * @param i position in {@link DrawPanel#transf} array.
     * @param transf transformation (0-7).
     */
    public void setTransf(int i, int transf){
        this.transf[i] = transf;
    }

    /**
     * @return transformation array.
     */
    public int[] getTransf() {
        return transf;
    }

    /**
     * @return animation mode.
     */
    public int getAnimationMode() {
        return animationMode;
    }

    /**
     * @param animationMode animation mode we want to use.
     * @throws IllegalArgumentException when mode is not within (0-1) range.
     */
    public void setAnimationMode(int animationMode) 
            throws IllegalArgumentException{
        if(animationMode < 0 || animationMode > 1){
            String err = "Animation mode must be in 0-1 range!";
            throw new IllegalArgumentException(err);
        } else {
            this.animationMode = animationMode;
        }
    }

    /**
     * @return cropping width which we use for decrypted image cropping. 
     */
    public int getCropWidth() {
        return cropWidth;
    }

    /**
     * Set width for cropping.
     * @param cropWidth width for cropping.
     * @throws IllegalArgumentException when trying to set width as negative
     * number.
     */
    public void setCropWidth(int cropWidth) throws IllegalArgumentException{
        if(cropWidth < 0){
            String err = "Crop width must be postive number!";
            throw new IllegalArgumentException(err);
        } else {
            this.cropWidth = cropWidth;
        }
        
    }

    /**
     * @return cropping height which we use for decrypted image cropping. 
     */
    public int getCropHeight() {
        return cropHeight;
    }

    /**
     * Set height for cropping.
     * @param cropHeight height for cropping.
     * @throws IllegalArgumentException when trying to set width as negative
     * number.
     */
    public void setCropHeight(int cropHeight) throws IllegalArgumentException{
        if(cropHeight < 0){
            String err = "Crop height must be postive number!";
            throw new IllegalArgumentException(err);
        } else {
            this.cropHeight = cropHeight;
        }
    }

    /**
     * @param currFrame frame of animation we are currently want to show.
     */
    public void setCurrFrame(int currFrame) {
        this.currFrame = currFrame;
    }
}
