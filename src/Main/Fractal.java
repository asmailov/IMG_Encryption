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
public class Fractal {
    private final int[] pixels;
    private final int length;
    private int[] transformations;
    private int iterations;
    private int currIteration;

    public Fractal(int[] pixels, int length, int[] transf) {
        this.pixels = pixels;
        this.length = length;
        setTransformations(transf);
    }
    
    public void createFractal(int iter){
        try{
            setIterations(iter);
            recursion(pixels, length);
        } catch (Exception e){
            e.printStackTrace(System.err);
        }
    }
    
    public int[] recursion(int[] pixels, int length){
//        if (getCurrIteration() == getIterations()){
//            return null;
//        }
        if (length <= 1){
            return null;
        }
        int halfLen = length / 2;
        float coeff = 1f;
        
        int[] array1, array2, array3, array4;
        array1 = new int[length*length];
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                Point p = new Point(j, i);
                if((i < halfLen) && (j < halfLen)){
//                    System.out.println(p.toString());
                    SquareDihedralGroup.transform(p, transformations[0],
                                                  coeff, halfLen);
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                    
                }
                if((i < halfLen) && (j >= halfLen)){
//                    System.out.println(p.toString());
                    p.setLocation(p.getX() - halfLen, p.getY());
                    SquareDihedralGroup.transform(p, transformations[1],
                                                  coeff, halfLen);
                    p.setLocation(p.getX() + halfLen, p.getY());
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                }
                if((i >= halfLen) && (j < halfLen)){
//                    System.out.println(p.toString());
                    p.setLocation(p.getX(), p.getY() - halfLen);
                    SquareDihedralGroup.transform(p, transformations[2],
                                                  coeff, halfLen);
                    p.setLocation(p.getX(), p.getY() + halfLen);
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                }
                if((i >= halfLen) && (j >= halfLen)){
//                    System.out.println(p.toString());
                    p.setLocation(p.getX() - halfLen, p.getY() - halfLen);
                    SquareDihedralGroup.transform(p, transformations[3],
                                                  coeff, halfLen);
                     p.setLocation(p.getX() + halfLen, p.getY() + halfLen);
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                }
            }
        }
        return array1;
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
    private void setTransformations(int[] transf) 
            throws IllegalArgumentException {
        String err_message;
        if(transf.length == 4){
            for(int i = 0; i < transf.length; i++){
                if(transf[i] < 0 || transf[i] > 7){
                    err_message = "Transformation number must be in 0-7 range!";
                    throw new IllegalArgumentException(err_message);
                }
            }
            this.transformations = transf;
        } else {
            err_message = "Transformation array must be of length 4!";
            throw new IllegalArgumentException(err_message);
        }
    }
    
    
}
