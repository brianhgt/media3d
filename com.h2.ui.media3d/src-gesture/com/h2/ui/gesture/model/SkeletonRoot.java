package com.h2.ui.gesture.model;

import java.util.List;

public class SkeletonRoot {

   private List<Skeleton> skeletons;
   
   ////
   ////
   
   public SkeletonRoot() {
      //default
   }
   
   public SkeletonRoot(List<Skeleton> skeletons) {
      setSkeletons(skeletons);
   }

   /**
    * @return the skeletons
    */
   public List<Skeleton> getSkeletons() {
      return skeletons;
   }

   /**
    * @param skeletons the skeletons to set
    */
   public void setSkeletons(List<Skeleton> skeletons) {
      this.skeletons = skeletons;
   }
}
