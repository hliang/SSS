/*
 * Create a wrapper class that has an instance of a BufferedImage, and then add your methods in.
 */

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.lang.Math;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ExtendedBufferedImage{
    private BufferedImage image;

    public static final float[] sobelFilterX = new float[]{ 1.0f, 0.0f, -1.0f, 
                                                            2.0f, 0.0f, -2.0f, 
                                                            1.0f, 0.0f, -1.0f };
    public static final float[] sobelFilterY = new float[]{ 1.0f, 2.0f, 1.0f,
                                                            0.0f, 0.0f, 0.0f, 
                                                            -1.0f, -2.0f, -1.0f };

    public ExtendedBufferedImage(BufferedImage image){
         this.image = image;
    }


    /**
     * convert color image to gray scale image
     */
    public BufferedImage getGrayImage() {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(),
                                                    BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));   // needs java.awt.Color
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                red = green = blue = (int)(red * 0.299 + green * 0.587 + blue * 0.114);
                color = new Color(red, green, blue);
                int rgb = color.getRGB();
                grayImage.setRGB(x, y, rgb);
            }
        }
        //*/
        /*
         * OR use this method
         */
        /*
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        */
        return grayImage;
    }


    /* 
     * Convert from grayscale to black and white (rescale intensity values from [0,255] to [0,1]).
     */
    public void getTwoBitImage() {
    }


    /*
     * resize
     */
    public void resize() {

    }


    /*
     * rotate
     */
    public void rotate() {

    }

    /*
     * convolve with filter
     */
    public BufferedImage convolve(int filterRows, int filterCols, float[] filterArray ) {
        BufferedImage dstImage = null;
        Kernel kernel = new Kernel(filterRows, filterCols, filterArray);  // tell the Kernel class that we want this array to be treated as a filterRows x filterCols matrix.
        ConvolveOp op = new ConvolveOp(kernel);
        dstImage = op.filter(image, null);  // when destination image is null, a BufferedImage will be created with the source ColorModel.
        return dstImage;

    }

    
    /*
     * edge detection
     * bug ?: edge from dark to light is not recognized well,
     *      although edge from light to dark is well detected.
     */
    public BufferedImage getEdges(){
        BufferedImage edgeImage = new BufferedImage(image.getWidth(), image.getHeight(),
                                                    BufferedImage.TYPE_INT_ARGB);
        BufferedImage edgeX = convolve(3, 3, sobelFilterX);
        BufferedImage edgeY = convolve(3, 3, sobelFilterY);
        for (int x = 0; x < edgeImage.getWidth(); x++) {
            for (int y = 0; y < edgeImage.getHeight(); y++) {
                int oldColorX = edgeX.getRGB(x, y);
                int oldColorY = edgeY.getRGB(x, y);
                int alpha = 0xff;
                int newColorX = oldColorX | (alpha << 24); // set alpha chanel to 0xFF
                int newColorY = oldColorY | (alpha << 24); // set alpha chanel to 0xFF

                //Color newColorX = new Color(oldColorX.getRed(), oldColor.getGreen(), oldColor.getBlue()); // creat new color with alpha chanel set to 0xFF;
                //Color newColorY = new Color(oldColorY.getRed(), oldColor.getGreen(), oldColor.getBlue()); // creat new color with alpha chanel set to 0xFF;
                // set color
                edgeX.setRGB(x, y, newColorX);
                edgeY.setRGB(x, y, newColorY);
            }
        }
        
        // save images showing horizontal lines and vertical lines
        try {
            ImageIO.write(edgeX, "PNG", new File("edgeX.png"));
            ImageIO.write(edgeY, "PNG", new File("edgeY.png"));
        } catch(IOException e){
            e.printStackTrace();
        }

        // color in the edge image
        for (int x = 0; x < edgeImage.getWidth(); x++) {
            for (int y = 0; y < edgeImage.getHeight(); y++) {
                Color color;
                Color colorX = new Color(edgeX.getRGB(x, y));   // needs java.awt.Color
                Color colorY = new Color(edgeY.getRGB(x, y));   // needs java.awt.Color
                int red = (int) (Math.sqrt(Math.pow(colorX.getRed(), 2) + Math.pow(colorY.getRed(), 2))/1.42);
                int green = (int) (Math.sqrt(Math.pow(colorX.getGreen(), 2) + Math.pow(colorY.getGreen(), 2)) / 1.42);
                int blue = (int) (Math.sqrt(Math.pow(colorX.getBlue(), 2) + Math.pow(colorY.getBlue(), 2)) / 1.42);

                color = new Color(red, green, blue);
                int rgb = color.getRGB();
                edgeImage.setRGB(x, y, rgb);
            }
        }
        return edgeImage;
    }


    /*
     * edge detection method coded by hand, without using ConvolveOp
     */
    public BufferedImage getEdges2(){
        BufferedImage edgeImage = new BufferedImage(image.getWidth(), image.getHeight(),
                                                    BufferedImage.TYPE_INT_ARGB);
        BufferedImage edgeX = new BufferedImage(image.getWidth(), image.getHeight(),
                                                    BufferedImage.TYPE_INT_ARGB);
        BufferedImage edgeY = new BufferedImage(image.getWidth(), image.getHeight(),
                                                    BufferedImage.TYPE_INT_ARGB);
        for (int x = 1; x < edgeImage.getWidth() - 1; x++) {
            for (int y = 1; y < edgeImage.getHeight() - 1; y++) {
                int targetSumX = 0;
                int targetSumY = 0;
                int targetSumZ = 0;
                int k = 0;
                // convolve with sobel filter
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        int blueChanel = image.getRGB(i, j) & 0x000000FF ; // get blue. for gray image, one chanel (R/G/B) is enough
                        targetSumX += (int) (blueChanel * sobelFilterX[k]);
                        targetSumY += (int) (blueChanel * sobelFilterY[k]);
                        k++;
                    }
                }
                // normalize (need improvement)
                targetSumX = Math.abs(targetSumX / 4);
                targetSumY = Math.abs(targetSumY / 4);
                // intensity value for the final image, which contains edges in both X and Y direction
                targetSumZ = (int) ((Math.sqrt(Math.pow(targetSumX, 2) + Math.pow(targetSumY, 2))) / Math.sqrt(2));
                // if (targetSumX > 255) { targetSumX = 255; } // or should I normalized it? or just divide by 4 (sharpest edge gives the highest value of 255+255*2+255)?
                // if (targetSumY > 255) { targetSumY = 255; }
                // if (targetSumZ > 255) { targetSumZ = 255; }
                // targetSumX = (targetSumX << 16) | 0xFF000000; // creat red color
                // targetSumY = (targetSumY << 16) | 0xFF000000; // creat red color
                // targetSumZ = (targetSumZ << 16) | 0xFF000000; // creat red color
                Color cX = new Color(targetSumX, targetSumX, targetSumX);
                Color cY = new Color(targetSumY, targetSumY, targetSumY);
                Color cZ = new Color(targetSumZ, targetSumZ, targetSumZ);
                edgeX.setRGB(x, y, cX.getRGB()); // set color
                edgeY.setRGB(x, y, cY.getRGB()); // set color
                edgeImage.setRGB(x, y, cZ.getRGB()); // set color
            }
        }

        // save images showing horizontal lines and vertical lines
        try {
            ImageIO.write(edgeX, "PNG", new File("edgeX.png"));
            ImageIO.write(edgeY, "PNG", new File("edgeY.png"));
        } catch(IOException e){
            e.printStackTrace();
        }

        return edgeImage;

    }

}

