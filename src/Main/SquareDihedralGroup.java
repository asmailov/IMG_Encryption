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
public class SquareDihedralGroup {
    
    public static void transform(Point p, int type, float coefficient, 
                                 int vectorLength){
//        switch (type){
//            case 0: p.setLocation(p.getX()/2, p.getY()/2);
//                    break;
//            case 1: p.setLocation(-p.getY()/2 + vectorLength/2, p.getX()/2);
//                    break;
//            case 2: p.setLocation(-p.getX()/2 + vectorLength/2, -p.getY()/2 +
//                        vectorLength/2);
//                    break;
//            case 3: p.setLocation(p.getY()/2, -p.getX()/2 + vectorLength/2);
//                break;
//            case 4: p.setLocation(-p.getY()/2 + vectorLength/2, -p.getX()/2 + 
//                        vectorLength/2);
//                break;
//            case 5: p.setLocation(p.getX()/2, -p.getY()/2 + vectorLength/2);
//                break;
//            case 6: p.setLocation(p.getY()/2, p.getX()/2);
//                break;
//            case 7: p.setLocation(-p.getX()/2 + vectorLength/2, p.getY()/2);
//                break;
//        }
        vectorLength -= 1;
        switch (type){
            case 0: p.setLocation(p.getX(), p.getY());
                    break;
            case 1: p.setLocation(-p.getY() + vectorLength, p.getX());
                    break;
            case 2: p.setLocation(-p.getX() + vectorLength, -p.getY() +
                        vectorLength);
                    break;
            case 3: p.setLocation(p.getY(), -p.getX() + vectorLength);
                break;
            case 4: p.setLocation(-p.getY() + vectorLength, -p.getX() + 
                        vectorLength);
                break;
            case 5: p.setLocation(p.getX(), -p.getY() + vectorLength);
                break;
            case 6: p.setLocation(p.getY(), p.getX());
                break;
            case 7: p.setLocation(-p.getX() + vectorLength, p.getY());
                break;
        }
        if(type >= 0 && type < 8){
            p.setLocation(p.getX() * coefficient, p.getY() * coefficient);
        }
    }
}
