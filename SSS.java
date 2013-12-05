
import java.io.File;
import java.awt.*; 
import java.util.Vector; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;

public class SSS extends Thread {

    public static void main(String[] args) throws Exception { 

        // input image name
        String inFileName = "./testdata/color.png"; 
        if (args.length >= 1) {
            inFileName = args[0];
        }
 
        // load the file using Java's imageIO library 
        BufferedImage image = javax.imageio.ImageIO.read(new File(inFileName)); 
 
        // create gray image
        ExtendedBufferedImage ebimg = new ExtendedBufferedImage(image);
        BufferedImage grayImage = ebimg.getGrayImage();
        ImageIO.write(grayImage, "PNG", new File("gray.png"));

        // get edges in gray image
        ebimg = new ExtendedBufferedImage(grayImage);
        BufferedImage edgeImage = ebimg.getEdges2();
        ImageIO.write(edgeImage, "PNG", new File("edge.png"));


        // create a hough transform object with the right dimensions 
        HoughTransform h = new HoughTransform(image.getWidth(), image.getHeight()); 
 
        // add the points from the image (or call the addPoint method separately if your points are not in an image 
        h.addPoints(image); 
 
        // hough transform image
        BufferedImage htImage = h.getHoughArrayImage();
        ImageIO.write(htImage, "PNG", new File("ht.png"));

        // get the lines out 
        Vector<HoughLine> lines = h.getLines(100); 
 
        // draw the lines (in red color) back onto the image 
        for (int j = 0; j < lines.size(); j++) { 
            HoughLine line = lines.elementAt(j); 
            line.draw(image, Color.RED.getRGB()); 
        } 

        // save image
        ImageIO.write(image, "PNG", new File("output.png"));

    }


}
