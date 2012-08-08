package com.h2.vis.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
   
   public static enum ENCODING {
      JPEG, PNG, GIF
   }

   /**
    * Used for creating sample images.
    * 
    * @param fileName
    * @param fileDir
    * @param encoding
    * @param width
    * @param height
    * @param text
    * @return
    * @throws Exception
    * @since 1.1
    */
   public static File writeImage(String fileName, String fileDir,
         ENCODING encoding, int width, int height, String text)
         throws Exception {
      if (fileName == null || fileDir == null) {
         throw new IOException("File name or directory is null");
      }
      if (!new File(fileDir).exists()) {
         throw new IOException("File directory does not exist: " + fileDir);
      }
      return writeImage(fileName, fileDir, encoding,
            ImageUtils.generateSampleImage(width, height, text));
   }
   
   /**
    * Writes a RenderedImage to specified encoding.
    * 
    * @param fileName
    * @param fileDir
    * @param encoding
    * @param rendImage
    * @return
    * @throws IOException
    * @since 1.1
    */
   public static File writeImage(String fileName, String fileDir,
         ENCODING encoding, RenderedImage rendImage) throws IOException {
      File file = null;
      
      switch (encoding) {
         case JPEG:
            // Save as JPEG
            boolean exists = (new File(fileDir)).exists();
            if (exists) {
               file = new File(fileDir, fileName + ".jpg");
            }
            else {
               new File(fileDir).mkdirs();
               file = new File(fileDir, fileName + ".jpg");
            }
            
            ImageIO.write(rendImage, "jpg", file);
            break;
         case PNG:
            // Save as PNG
            exists = (new File(fileDir)).exists();
            if (exists) {
               file = new File(fileDir, fileName + ".png");
            }
            else {
               new File(fileDir).mkdirs();
               file = new File(fileDir, fileName + ".png");
            }
             ImageIO.write(rendImage, "png", file);
            break;
         case GIF:
            // Save as GIF
            exists = (new File(fileDir)).exists();
            if (exists) {
               file = new File(fileDir, fileName + ".gif");
            }
            else {
               new File(fileDir).mkdirs();
               file = new File(fileDir, fileName + ".gif");
            }
            
            ImageIO.write(rendImage, "gif", file);
            break;
         
         default:
            break;
      }
      return file;
   }
   
   /**
    * NOTE: THIS IS IMAGE SCALING CODE THAT WORKS, AND THAT USES AWT ONLY
    * (NO JAI OR ANY OTHER TOOLKIT). IT IS NOT AS FAST AS JAI, AND SO WE
    * ARE NOT USING IT, BUT COULD IF JAI PROVES PROBLEMATIC Scales and
    * renders the input image onto the output image, averaging the given
    * number of sample pixels per input "tile". If sample size is 0 or
    * negative, then a full area averaging is done. private static void
    * areaAverage(BufferedImage in, BufferedImage out, int sampleSize) {
    * int inW = in.getWidth(); int inH = in.getHeight(); int outW =
    * out.getWidth(); int outH = out.getHeight();
    * 
    * System.out.println("Reducing from "+inW+" x "+inH+" to "+outW+" x "
    * +outH) ; int xTileSize = inW / outW; int yTileSize = inH / outH; if
    * (xTileSize == 0 || yTileSize == 0) { throw new
    * IllegalArgumentException(
    * "Destination image must be smaller than source"); }
    * 
    * int sampleRate; int pixPerTile = xTileSize * yTileSize; if
    * (sampleSize > 0 && (sampleSize < pixPerTile / 2)) { sampleRate =
    * pixPerTile / sampleSize; // Integer divide, will be floor } else {
    * sampleRate = 1; }
    * 
    * // For each destination pixel for (int x = 0; x < outW; ++x) { for
    * (int y =0; y < outH; ++y) {
    * 
    * // Reset the computed pixel int red = 0; int green = 0; int blue =
    * 0;
    * 
    * int i = 0; int sampled = 0;
    * 
    * // For each source pixel in the tile for (int sx = x * xTileSize,
    * xmax = sx + xTileSize; sx < xmax; ++sx) { for (int sy = y *
    * yTileSize, ymax = sy + yTileSize; sy < ymax; ++sy) { if (++i %
    * sampleRate != 0) continue;
    * 
    * int p = in.getRGB(sx, sy); int r = (p >> 16) & 0xff; int g = (p >>
    * 8) & 0xff; int b = (p >> 0) & 0xff; red += r; green += g; blue +=
    * b; ++sampled; } }
    * 
    * red = red / sampled; green = green / sampled; blue = blue /
    * sampled;
    * 
    * int rgb = 0xff000000 | red << 16 | green << 8 | blue;
    * 
    * out.setRGB(x, y, rgb); } } }
    * 
    * @param width 
    * @param height 
    * @param text 
    * @return 
    */
   public static RenderedImage generateSampleImage(int width, int height,
         String text) {

      BufferedImage bufferedImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);

      // Create a graphics contents on the buffered image
      Graphics2D g2d = bufferedImage.createGraphics();

      Color[] testColorArr = { Color.GRAY, Color.YELLOW, Color.CYAN,
            Color.GREEN, Color.MAGENTA, Color.RED, Color.BLUE };

      for (int i = 0; i < testColorArr.length; i++) {
         g2d.setColor(testColorArr[i]);
         g2d.fillRect(i * (width / testColorArr.length), 0, (i + 1)
               * (width / testColorArr.length), height);
      }
      g2d.setColor(Color.black);
      g2d.drawOval(0, 0, Math.min(width, height), Math.min(width, height));
      g2d.drawString(text, 2, height / 2);

      g2d.dispose();

      return bufferedImage;
   }
}
