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

    public Fractal(int[] pixels, int length) {
        this.pixels = pixels;
        this.length = length;
    }
    
    public int[] recursion(int[] pixels, int length){
        int[] array1, array2, array3, array4;
        array1 = new int[length*length];
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                Point p = new Point(j, i);
                if((i < length/2) && (j < length/2)){
                    
//                    System.out.println(p.toString());
                    SquareDihedralGroup.transform(p, 1, length);
                    p.x = p.x - length/2;
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
//                    System.out.println(j + " " + i);
//                    System.out.println(i * length +j);
//                    System.out.println(p.x + " " + p.y);
//                    System.out.println(p.x * length + p.y);
//                    int a = pixels[i * length + j];
                    array1[p.y * length + p.x] = pixels[i * length + j];
                    
                }
                if((i < length/2) && (j >= length/2)){
//                    System.out.println(p.toString());
                    SquareDihedralGroup.transform(p, 1, length);
                    p.y = p.y - length/2;
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    //System.out.println("2");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                }
                if((i >= length/2) && (j < length/2)){
//                    System.out.println(p.toString());
                    SquareDihedralGroup.transform(p, 1, length);
                    p.y = p.y + length/2;
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    
                    //System.out.println("3");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                }
                if((i >= length/2) && (j >= length/2)){
//                    System.out.println(p.toString());
                    SquareDihedralGroup.transform(p, 1, length);
                    p.x = p.x + length/2;
//                    System.out.println(p.toString());
//                    System.out.println("_______________");
                    //System.out.println("4");
                    array1[p.y * length + p.x] = pixels[i * length + j];
                }
            }
        }
        return array1;
    }
}
