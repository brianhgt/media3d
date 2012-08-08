package com.h2.ui.media3d.scene.shape;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;


public class ImageSphere extends Node {
   
   protected int zSamples;
   protected int radialSamples;
   protected boolean useEvenSlices;
   protected boolean interior;
   /** the distance from the center point each point falls on */
   public float radius;
   
   private List<Geometry> quadGeos;
   
   public ImageSphere(int zSamples, int radialSamples, float radius) {
      setzSamples(zSamples);
      setRadialSamples(radialSamples);
      setRadius(radius);
      
      computeMesh();
   }
   
   protected void computeMesh() {
      
      getLocalRotation().fromAngleNormalAxis(-FastMath.HALF_PI,
            Vector3f.UNIT_X);
      
      setQuadGeos(new ArrayList<Geometry>());
      
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
            
            getQuadGeos().add(quadGeo);
            
            attachChild(rTrans);
         }
      }
   }

   /**
    * @return the quadGeos
    */
   public List<Geometry> getQuadGeos() {
      return quadGeos;
   }

   /**
    * @param quadGeos the quadGeos to set
    */
   public void setQuadGeos(List<Geometry> quadGeos) {
      this.quadGeos = quadGeos;
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
   
}
