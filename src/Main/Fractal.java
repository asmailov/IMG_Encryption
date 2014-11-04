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

package Main;

import java.awt.Point;

/**
 * @author Aleksandr Šmailov
 */
public class Fractal{
    private final int[] pixels;
    private final int length;
    private int[] transf;
    private int iterations;
    private int levelReached;
    
    public int[] newPixels;

    /**
     * @param pixels pixels of the image.
     * @param length length of the side of the image.
     * @param transf transformation array.
     */
    public Fractal(int[] pixels, int length, int[] transf) {
        this.pixels = pixels;
        this.length = length;
        //this.newPixels = new int[length*length];
        this.newPixels = this.pixels.clone();
        setTransf(transf);
    }
    
    public void inverseTransformations(){
        for (int i = 0; i < transf.length; i++){
            transf[i] = SquareDihedralGroup.getInverse(transf[i]);
        }
    }
    
    /**
     * Creates fractal and returns the amount of iterations that were executed.
     * @param iter amount of iterations.
     * @return level reached.
     */
    public int createFractal(int iter){
        try{
            setIterations(iter);
            recursion(pixels, length, 0, 0, 0);
        } catch (IllegalArgumentException e){
            e.printStackTrace(System.err);
        }
        return levelReached;
    }
    
    public int decrypt(int iter){
        try{
            setIterations(iter);
            recursion(pixels, length, 0, 0, 0);
        } catch (IllegalArgumentException e){
            e.printStackTrace(System.err);
        }
        return levelReached;
    }
    
    /**
     * Main function of creating fractal from the image.
     * @param pixels pixels array of current level.
     * @param length length of the pixels array.
     * @param x x coordinate of difference vector.
     * @param y y coordinate of difference vector.
     * @param level current level in recursion.
     */
    private void recursion(int[] pixels, int length, int x, 
                                     int y, int level){
        // Once we reach iterations level we return.
        if (level == iterations){
            levelReached = level;
            return;
        }
        // If we reach 1 pixel length we return.
        if (length <= 1){
            levelReached = level;
            return;
        }
        // Increase level.
        level += 1;
        // Calculate half length so we can use this variable to divide current
        // image part into 4 sections.
        int halfLen = length / 2;
        // Special ocassion when we can't divide current image part 
        // symetrically needs to be processed, so we make a flag.
        boolean flag = false;
        
        if( length % 2 != 0){
            flag = true;
        }
        // Transformation coefficient must be 1 for the image fractal.
        float coeff = 1f;
        // Arrays for new image divisions.
        int[] array0, array1, array2, array3;
        array0 = new int[halfLen * halfLen];
        array1 = new int[halfLen * halfLen];
        array2 = new int[halfLen * halfLen];
        array3 = new int[halfLen * halfLen];
        
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                // Create new point at the current location.
                Point p = new Point(j, i);
                // Transform at 1'st position.
                if((i < halfLen) && (j < halfLen)){
                    SquareDihedralGroup.transform(p, transf[0],
                                                  coeff, halfLen);
                    array0[p.y * halfLen + p.x] = pixels[i * length + j];
                }
                // Transform at 2'nd position.
                if(!flag){
                    if((i < halfLen) && (j >= halfLen)){
                        p.setLocation(p.getX() - halfLen, p.getY());
                        SquareDihedralGroup.transform(p, transf[1],
                                                      coeff, halfLen);
                        array1[p.y * halfLen + p.x] = pixels[i * length + j];
                    }
                } else {
                    if((i < halfLen) && (j > halfLen)){
                        
                        p.setLocation(p.getX() - halfLen-1, p.getY());
                        SquareDihedralGroup.transform(p, transf[1],
                                                      coeff, halfLen);
                        array1[p.y * halfLen + p.x] = pixels[i * length + j];
                    }
                }
                // Transform at 3'rd position.
                if(!flag){
                    if((i >= halfLen) && (j < halfLen)){
                        p.setLocation(p.getX(), p.getY() - halfLen);
                        SquareDihedralGroup.transform(p, transf[2],
                                                      coeff, halfLen);
                        array2[p.y * halfLen + p.x] = pixels[i * length + j];
                    }
                } else {
                    if((i > halfLen) && (j < halfLen)){
                        
                        p.setLocation(p.getX(), p.getY() - halfLen-1);
                        SquareDihedralGroup.transform(p, transf[2],
                                                      coeff, halfLen);
                        array2[p.y * halfLen + p.x] = pixels[i * length + j];
                    }
                }
                // Transform at 4'th position.
                if(!flag){
                    if((i >= halfLen) && (j >= halfLen)){
                        p.setLocation(p.getX() - halfLen, p.getY() - halfLen);
                        SquareDihedralGroup.transform(p, transf[3],
                                                      coeff, halfLen);
                        array3[(p.y) * halfLen + p.x] = pixels[i * length + j];
                    }
                } else {
                    if((i > halfLen) && (j > halfLen)){
                        p.setLocation(p.getX() - halfLen-1, p.getY() - halfLen-1);
                        SquareDihedralGroup.transform(p, transf[3],
                                                      coeff, halfLen);
                        array3[p.y * halfLen + p.x] = pixels[i * length + j];
                    }
                }
            }
        }
        int fix = 0;
        if (flag){
            fix = 1;
        }
        // Transfer pixel data from partition 1 to real image.
        for(int i = 0; i < halfLen; i++){
            for(int j = 0; j < halfLen; j++){
                newPixels[(i+y) * this.length + (j + x)] = 
                        array0[i * halfLen + j];
            }
        }
        // Transfer pixel data from partition 2 to real image.
        for(int i = 0; i < halfLen; i++){
            for(int j = 0; j < halfLen; j++){
                newPixels[(i+y) * this.length + (j + x +halfLen)+fix] = 
                        array1[i * halfLen + j];
            }
        }
        // Transfer pixel data from partition 3 to real image.
        for(int i = 0; i < halfLen; i++){
            for(int j = 0; j < halfLen; j++){
                newPixels[(i+y+halfLen+fix) * this.length  + (j + x)] = 
                        array2[i * halfLen + j];
            }
        }
        // Transfer pixel data from partition 4 to real image.
        for(int i = 0; i < halfLen; i++){
            for(int j = 0; j < halfLen; j++){
                newPixels[(i+y+halfLen+fix) * this.length  + (j + x + halfLen)+fix] = 
                        array3[i * halfLen + j];
            }
        }
        // Use created arrays of pixels to go deeper until we reach
        // level = iterations or arrays consist of one pixel.
        recursion(array0, halfLen, x, y, level);
        recursion(array1, halfLen, x+halfLen+fix, y, level);
        recursion(array2, halfLen, x, y+halfLen+fix, level);
        recursion(array3, halfLen, x+halfLen+fix, y+halfLen+fix, level);
    }
    
    // Setters.

    /**
     * Set iteration amount in the recursion.
     * @param iterations amount of iterations in the recursion.
     * @throws IllegalArgumentException when argument is negative or zero.
     */
    private void setIterations(int iterations) throws IllegalArgumentException {
        if(iterations > 0){
            this.iterations = iterations;
        } else {
            String err = "Iterations must be positive, non zero number!";
            throw new IllegalArgumentException(err);
        }
    }
    /**
     * Sets transformation array.
     * @param transf transformation array.
     * @throws IllegalArgumentException when array length is not equal to 4 or
     * array elements are not in 0-7 range.
     */
    private void setTransf(int[] transf) 
            throws IllegalArgumentException {
        String err_message;
        if(transf.length == 4){
            for(int i = 0; i < transf.length; i++){
                if(transf[i] < 0 || transf[i] > 7){
                    err_message = "Transformation number must be in 0-7 range!";
                    throw new IllegalArgumentException(err_message);
                }
            }
            this.transf = transf;
        } else {
            err_message = "Transformation array must be of length 4!";
            throw new IllegalArgumentException(err_message);
        }
    }

    /**
     * Get pixels of the transformed image.
     * @return pixels of the transformed image.
     */
    public int[] getNewPixels() {
        return newPixels;
    }
    
    
}
