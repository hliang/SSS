/*
 * Create a wrapper class that has an instance of a BufferedImage, and then add your methods in.
 */

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.lang.Math;

public class ExtendedBufferedImage{
    private BufferedImage image;

    public static final float[] sobelFilterX = new float[]{ -1, 0, 1, -2, 0, 2, -1, 0, 1 };
    public static final float[] sobelFilterY = new float[]{ -1, -2, -1, 0, 0, 0, 1, 2, 1 };

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
    public BufferedImage convolve(int filterRows, int filterCols float[] filterArray ) {
        BufferedImage dstImage = null;
        Kernel kernel = new Kernel(filterRows, filterCols, filterArray);  // tell the Kernel class that we want this array to be treated as a filterRows x filterCols matrix.
        ConvolveOp op = new ConvolveOp(kernel);
        dstImage = op.filter(image, null);  // when destination image is null, a BufferedImage will be created with the source ColorModel.
        return dstImage;

    }

    
    /*
     * edge detection
     */
    public BufferedImage getEdges(){
        BufferedImage edgeImage = new BufferedImage(image.getWidth(), image.getHeight(),
                                                    BufferedImage.TYPE_INT_ARGB);
        BufferedImage edgeX = convolve(sobelFilterX);
        BufferedImage edgeY = convolve(sobelFilterY);

        for (int x = 0; x < edgeImage.getWidth(); x++) {
            for (int y = 0; y < edgeImage.getHeight(); y++) {
                Color color;
                Color color1 = new Color(edgeX.getRGB(x, y));   // needs java.awt.Color
                Color color2 = new Color(edgeY.getRGB(x, y));   // needs java.awt.Color
                int red = (int) (Math.sqrt(Math.pow(color1.getRed(), 2) + Math.pow(color2.getRed(), 2))/1.42);
                int green = (int) (Math.sqrt(Math.pow(color1.getGreen(), 2) + Math.pow(color2.getGreen(), 2)) / 1.42);
                int blue = (int) (Math.sqrt(Math.pow(color1.getBlue(), 2) + Math.pow(color2.getBlue(), 2)) / 1.42);

                color = new Color(red, green, blue);
                int rgb = color.getRGB();
                edgeImage.setRGB(x, y, rgb);
            }
        }
        return edgeImage;

    }


}

