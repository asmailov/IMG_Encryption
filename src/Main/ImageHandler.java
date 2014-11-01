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

/**
 *
 * @author Aleksandr Šmailov
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
    
public class ImageHandler {
    private Color[][] pixelColors;
    private BufferedImage  image;
    private int width;
    private int height;
    
    /**
     * Reads an image and creates array of colors of every pixel in the
     * picture.
     * @param file path to picture.
     */
    public ImageHandler(String file) {
        try {
            File input = new File(file);
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
            
            pixelColors = new Color[height][width];
            
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    pixelColors[i][j] = new Color(image.getRGB(j, i));
                }
             }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    /**
     * @return array of pixel colors.
     */
    public Color[][] getPixelColors() {
        return pixelColors;
    }
    
}

