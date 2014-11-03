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
    
    /**
     * @param pixels array of pixels in the default RGB color model (TYPE_INT_ARGB) 
     * and default sRGB colorspace.
     * @param height height of the image.
     * @param width width of the image.
     */
    public ImageCreator(int[] pixels, int height, int width) {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
    }
    
    /**
     * Creates an image from pixels array.
     * @return BufferedImage.
     */
    public BufferedImage createImage(){
        int[] pixelsForWriting;
        pixelsForWriting = handlePixels(pixels, height, width);
        BufferedImage image;
        image = getImageFromArray(pixelsForWriting, height, width);
        return image;
    }
    
    /**
     * Creates array of pixels which are suitable for 
     * {@link #getImageFromArray(int[], int, int) getImageFromArray} method.
     * @return array of pixels.
     */
    private int[] handlePixels(int[] pixels, int height, int width){
        int count = 0;
        int[] pixelsForWriting = new int[height * width * 4];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                // Take ARGB value.
                int rgb = pixels[i * width + j];
                // Parse the values from int.
                int alpha = (rgb >> 24) & 0xff;
                int red   = (rgb >> 16) & 0xff;
                int green = (rgb >>  8) & 0xff;
                int blue  = (rgb) & 0xff;
                // Add values to the array.
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
    
    /**
     * Creates {@link BufferedImage} from pixels array.
     * @param pixels pixels array.
     * @param width width of the image.
     * @param height height of the image.
     * @return BufferedImage.
     */
    private static BufferedImage getImageFromArray(int[] pixels, int width, 
                                                   int height) {
        // Create buffered image.
        BufferedImage image = new BufferedImage(width, height, 
                                                BufferedImage.TYPE_INT_ARGB);
        // Set image's pixels.
        image.getRaster().setPixels(0, 0, width, height, pixels);
        // Return newly created image.
        return image;
    }
}
