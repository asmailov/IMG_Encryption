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

package ImageProcessing;

/**
 *
 * @author Aleksandr Šmailov
 */

import java.awt.image.BufferedImage;
import java.io.*;
import static java.lang.Math.round;
import java.util.Arrays;
import javax.imageio.ImageIO;
    
public class ImageHandler {
    
    private BufferedImage  image;
    private int width;
    private int height;
    
    private int[] pixels;
    private int[] paddedPixels;
    
    /**
     * Reads an image and creates array of 
     * @param file path to picture.
     */
    public ImageHandler(String file) {
        try {
            File input = new File(file);
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
            int size = width * height;
            pixels = new int[size];
            
            int count = 0;
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
//                    int rgb = image.getRGB(j, i);
//                    int alpha = (rgb >> 24) & 0xff;
//                    int red   = (rgb >> 16) & 0xff;
//                    int green = (rgb >>  8) & 0xff;
//                    int blue  = (rgb) & 0xff;
//                    
//                    pixels[count] = red;
//                    count++;
//                    pixels[count] = green;
//                    count++;
//                    pixels[count] = blue;
//                    count++;
//                    pixels[count] = alpha;
//                    count++;
                    pixels[count] = image.getRGB(j, i);
                    count++;
                }
             }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * @return array of grayscale pixel values.
     */
    public int[] getGrayValues(){
        int[] grayValues = new int[pixels.length];
        int count = 0;
        int red,green,blue;
        
        for (int i = 0; i < pixels.length; i++){
            red   = (pixels[i] >> 16) & 0xff;
            green = (pixels[i] >>  8) & 0xff;
            blue  = (pixels[i]) & 0xff;
            
            red = (int)round((red * 0.299));
            green = (int)round((green * 0.587));
            blue = (int)round((blue *0.114));
            
            grayValues[count] = red + green + blue;
            count++;
        }
        return grayValues;
    }
    
    public int[] getPaddedPixels(){
        if(width == height){
            return pixels;
        }
        int paddedSize;
        int length;
        int flag;
        if(width > height){
            length = width;
            flag = 0;
        } else {
            length = height;
            flag = 1;
        }
        paddedSize = length * length;
        paddedPixels = new int[paddedSize];
        int blackARGB = -16777216;
        Arrays.fill(paddedPixels, blackARGB);
        int count = 0;
        int count2 = 0;
//        for(int i=0; i<length; i++){
//            for(int j=0; j<length; j++){
//                if((i * height + j) == (i * length + j)){
//                    paddedPixels[count] = pixels[count2];
//                }
//            }
//        }
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                paddedPixels[i * length + j] = pixels[i * width + j];
                //System.out.println(i * width + j);
            }
        }
        
        return paddedPixels;
    }
    
    /**
     * Snatches buffered image.
     * @return buffered image.
     */
    public BufferedImage getImage() {
        return image;
    }
    /**
     * @return array of pixels in the default RGB color model (TYPE_INT_ARGB) 
     * and default sRGB colorspace.
     */
    public int[] getPixelsARGB() {
        return pixels;
    }
}

