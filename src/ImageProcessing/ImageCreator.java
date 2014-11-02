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

import java.awt.image.BufferedImage;

/**
 * @author Aleksandr Šmailov
 */
public class ImageCreator {
    private final int height;
    private final int width;
    private final int[] pixels;
    
    public ImageCreator(int[] pixels, int height, int width) {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
    }
    
    public BufferedImage createImage(){
        int[] pixelsForWriting;
        pixelsForWriting = handlePixels(pixels, height, width);
        BufferedImage image;
        image = getImageFromArray(pixelsForWriting, height, width);
        return image;
    }
    
    private int[] handlePixels(int[] pixels, int height, int width){
        int count = 0;
        int[] pixelsForWriting = new int[height * width * 4];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                int rgb = pixels[i * width + j];
                
                int alpha = (rgb >> 24) & 0xff;
                int red   = (rgb >> 16) & 0xff;
                int green = (rgb >>  8) & 0xff;
                int blue  = (rgb) & 0xff;

                pixelsForWriting[count] = red;
                count++;
                pixelsForWriting[count] = green;
                count++;
                pixelsForWriting[count] = blue;
                count++;
                pixelsForWriting[count] = alpha;
                count++;
            }
        }
        return pixelsForWriting;
    }
    
    public static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.getRaster().setPixels(0, 0, width, height, pixels);
        return image;
    }
}
