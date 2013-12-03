/*
 * Create a wrapper class that has an instance of a BufferedImage, and then add your methods in.
 */

import java.awt.image.BufferedImage;
import java.awt.Color;

public class ExtendedBufferedImage{
    private BufferedImage image;

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
     * edge detection
     */
    public void getEdges(){

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


}

