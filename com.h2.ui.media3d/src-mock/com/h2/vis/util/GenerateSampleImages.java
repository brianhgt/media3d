package com.h2.vis.util;

import com.h2.vis.util.ImageUtils.ENCODING;

public class GenerateSampleImages {
   
   public static void main(String[] args) {
      for (int i = 0; i < 500; i++) {
         
         String text = String.format("%03d", i);
         
         try {
            ImageUtils.writeImage(text, "mock", ENCODING.JPEG, 360, 480, text);
         } catch (Exception exp) {
            exp.printStackTrace();
         }
      }
   }

}
