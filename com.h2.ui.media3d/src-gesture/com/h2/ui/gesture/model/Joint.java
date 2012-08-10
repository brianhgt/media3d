package com.h2.ui.gesture.model;

public class Joint {
   
   private JointType jointType;

   private Float x = 0.0F;
   
   private Float y = 0.0F;
   
   private Float z = 0.0F;
   
   ////
   ////
   
   public Joint() {
      //default
   }
   
   public Joint(JointType jointType) {
      setJointType(jointType);
   }

   /**
    * @return the jointType
    */
   public JointType getJointType() {
      return jointType;
   }

   /**
    * @param jointType the jointType to set
    */
   public void setJointType(JointType jointType) {
      this.jointType = jointType;
   }

   /**
    * @return the x
    */
   public Float getX() {
      return x;
   }

   /**
    * @param x the x to set
    */
   public void setX(Float x) {
      this.x = x;
   }

   /**
    * @return the y
    */
   public Float getY() {
      return y;
   }

   /**
    * @param y the y to set
    */
   public void setY(Float y) {
      this.y = y;
   }

   /**
    * @return the z
    */
   public Float getZ() {
      return z;
   }

   /**
    * @param z the z to set
    */
   public void setZ(Float z) {
      this.z = z;
   }
   
}
