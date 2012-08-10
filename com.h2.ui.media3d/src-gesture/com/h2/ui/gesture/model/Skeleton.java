package com.h2.ui.gesture.model;

import java.util.List;

public class Skeleton {
   
   private Integer id;
   
   private Float x;
   
   private Float y;
   
   private Float z;
   
   private Integer frame;
   
   private Integer time;
   
   private Long timeRecorded;
   
   private List<Joint> joints;
   
   ////
   ////

   /**
    * @return the id
    */
   public Integer getId() {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(Integer id) {
      this.id = id;
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

   /**
    * @return the frame
    */
   public Integer getFrame() {
      return frame;
   }

   /**
    * @param frame the frame to set
    */
   public void setFrame(Integer frame) {
      this.frame = frame;
   }

   /**
    * @return the time
    */
   public Integer getTime() {
      return time;
   }

   /**
    * @param time the time to set
    */
   public void setTime(Integer time) {
      this.time = time;
   }

   /**
    * @return the joints
    */
   public List<Joint> getJoints() {
      return joints;
   }

   /**
    * @param joints the joints to set
    */
   public void setJoints(List<Joint> joints) {
      this.joints = joints;
   }

   /**
    * @return the timeRecorded
    */
   public Long getTimeRecorded() {
      return timeRecorded;
   }

   /**
    * @param timeRecorded the timeRecorded to set
    */
   public void setTimeRecorded(Long timeRecorded) {
      this.timeRecorded = timeRecorded;
   }

}
