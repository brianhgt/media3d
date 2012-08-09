package com.h2.ui.media3d.scene.shape;

import java.util.ArrayList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;


public class ImageSphere extends Node {
   
   protected int zSamples;
   protected int radialSamples;
   protected boolean useEvenSlices;
   protected boolean interior;
   /** the distance from the center point each point falls on */
   public float radius;
   
   private List<Entry> entries = new ArrayList<Entry>();
   
   ////
   ////

   public ImageSphere(int zSamples, int radialSamples, float radius) {
      setzSamples(zSamples);
      setRadialSamples(radialSamples);
      setRadius(radius);
      
      computeMesh();
   }
   
   protected void computeMesh() {
      
      getLocalRotation().fromAngleNormalAxis(-FastMath.HALF_PI,
            Vector3f.UNIT_X);
      
      setEntries(new ArrayList<Entry>());
      
//      for (int r = 0; r < radialSamples; r++) {
//         float azimuthalAngle = FastMath.TWO_PI * invRS * r;
//         for (int t = 1; t < (zSamples - 1); t++) {
//            float polarAngle = FastMath.PI * invZS * t + FastMath.PI + 1.5f;
//            
//            //rotate around z-axis
//            Node rTrans = new Node();
//            rTrans.getLocalRotation().fromAngleNormalAxis(azimuthalAngle,
//                  Vector3f.UNIT_Z);
//            
//            //rotate up/down
//            Node zTrans = new Node();
//            zTrans.getLocalRotation().fromAngleNormalAxis(polarAngle,
//                  Vector3f.UNIT_Y);
//            rTrans.attachChild(zTrans);
//            
//            //move to radius
//            Node radiusTrans = new Node();
//            radiusTrans.getLocalTranslation().addLocal(Vector3f.UNIT_X);
//            radiusTrans.getLocalTranslation().multLocal(getRadius());
//            zTrans.attachChild(radiusTrans);
//            
//            //offset image
//            Node rotateTrans = new Node();
//            rotateTrans.getLocalRotation().fromAngleNormalAxis(FastMath.HALF_PI,
//                  Vector3f.UNIT_Y);
//            radiusTrans.attachChild(rotateTrans);
//            
//            
////            float imageSize = getRadius() * zRadius;
//            
//            Quad quad = new Quad(0.5f, 0.5f);
//            Geometry quadGeo = new Geometry("Image quad", quad);
//            rotateTrans.attachChild(quadGeo);
//            
//            getQuadGeos().add(quadGeo);
//            
//            attachChild(rTrans);
//         }
//      }
      
      float width = 1.0f;
      float padding = 0.2f;
      
      float arcAngle = FastMath.asin((width + padding) / getRadius());
      
      for (float t = -FastMath.QUARTER_PI; t < FastMath.QUARTER_PI; t += arcAngle) {
         
         float zRadius;
         if (t < 0) {
            zRadius = Math.abs(FastMath.cos(t) * getRadius());
         }
         else {
            zRadius = Math.abs(FastMath.cos(t + arcAngle) * getRadius());
         }
         
         float deltaArcAngle = Math.abs(FastMath.asin((width + padding) / zRadius));
         
         int deltaWhole = (int) (FastMath.TWO_PI / deltaArcAngle);
         float deltaRem = FastMath.TWO_PI % deltaArcAngle;
         float delta = deltaRem / deltaWhole;
         
         for (float r = 0; r < FastMath.TWO_PI - deltaArcAngle; r += deltaArcAngle + delta) {
            
            //rotate around z-axis
            Node rTrans = new Node();
            rTrans.getLocalRotation().fromAngleNormalAxis(r,
                  Vector3f.UNIT_Z);
            
            //rotate up/down
            Node zTrans = new Node();
            zTrans.getLocalRotation().fromAngleNormalAxis(t,
                  Vector3f.UNIT_Y);
            rTrans.attachChild(zTrans);
            
            //move to radius
            Node radiusTrans = new Node();
            radiusTrans.getLocalTranslation().addLocal(Vector3f.UNIT_X);
            radiusTrans.getLocalTranslation().multLocal(getRadius());
            zTrans.attachChild(radiusTrans);
            
            //offset image
            Node rotateTrans1 = new Node();
            rotateTrans1.getLocalRotation().fromAngleNormalAxis(FastMath.HALF_PI,
                  Vector3f.UNIT_X);
            radiusTrans.attachChild(rotateTrans1);
            
            Node rotateTrans2 = new Node();
            rotateTrans2.getLocalRotation().fromAngleNormalAxis(FastMath.HALF_PI,
                  Vector3f.UNIT_Y);
            rotateTrans2.getLocalTranslation().addLocal(
                  new Vector3f(width / 2, -width / 2, 0));
            rotateTrans1.attachChild(rotateTrans2);
            
            
//            float imageSize = getRadius() * zRadius;
            
            Quad quad = new Quad(width, width);
            Geometry quadGeo = new Geometry("Image quad", quad);
            rotateTrans2.attachChild(quadGeo);
            
            Entry entry = new Entry();
            entry.setGeometry(quadGeo);
            
            
            getEntries().add(entry);
            
            attachChild(rTrans);
         }
      }
   }
   
   public void createTitle(AssetManager assetManager, Entry entry,
         String text) {
      BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
      BitmapText helloText = new BitmapText(guiFont, false);
      helloText.setSize(guiFont.getCharSet().getRenderedSize());
      helloText.setText(text);
      
      entry.getTranslation().attachChild(helloText);
   }
   
   protected void addEntry(Entry entry) {
      int index = entries.size();
      //TODO
   }
   
   public static class Entry {
      
      private Node translation;

      private String title;
      
      private Geometry geometry;
      
      private Texture thumbnail;
      
      ////
      ////

      public Geometry getGeometry() {
         return geometry;
      }

      public void setGeometry(Geometry geometry) {
         this.geometry = geometry;
      }

      public String getTitle() {
         return title;
      }

      public void setTitle(String title) {
         this.title = title;
      }
      
      public Texture getThumbnail() {
         return thumbnail;
      }

      public void setThumbnail(Texture thumbnail) {
         this.thumbnail = thumbnail;
      }

      /**
       * @return the translation
       */
      public Node getTranslation() {
         return translation;
      }

      /**
       * @param translation the translation to set
       */
      public void setTranslation(Node translation) {
         this.translation = translation;
      }
      
   }

   /**
    * @return the zSamples
    */
   public int getzSamples() {
      return zSamples;
   }

   /**
    * @param zSamples the zSamples to set
    */
   public void setzSamples(int zSamples) {
      this.zSamples = zSamples;
   }

   /**
    * @return the radialSamples
    */
   public int getRadialSamples() {
      return radialSamples;
   }

   /**
    * @param radialSamples the radialSamples to set
    */
   public void setRadialSamples(int radialSamples) {
      this.radialSamples = radialSamples;
   }

   /**
    * @return the useEvenSlices
    */
   public boolean isUseEvenSlices() {
      return useEvenSlices;
   }

   /**
    * @param useEvenSlices the useEvenSlices to set
    */
   public void setUseEvenSlices(boolean useEvenSlices) {
      this.useEvenSlices = useEvenSlices;
   }

   /**
    * @return the radius
    */
   public float getRadius() {
      return radius;
   }

   /**
    * @param radius the radius to set
    */
   public void setRadius(float radius) {
      this.radius = radius;
   }
   
   public List<Entry> getEntries() {
      return entries;
   }

   public void setEntries(List<Entry> entries) {
      this.entries = entries;
   }
   
}
