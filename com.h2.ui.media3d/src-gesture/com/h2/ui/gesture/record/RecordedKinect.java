package com.h2.ui.gesture.record;

import net.sf.jni4net.attributes.ClrMethod;
import microsoftkinectwrapper.IKinectHandler;
import microsoftkinectwrapper.ISkeletonFrameHandler;

public class RecordedKinect implements IKinectHandler {

   @Override
   @ClrMethod("(LMicrosoftKinectWrapper/ISkeletonFrameHandler;)V")
   public void addSkeletonHandler(ISkeletonFrameHandler arg0) {
      // TODO Auto-generated method stub

   }

   @Override
   @ClrMethod("()V")
   public void disableSkeleton() {
      // TODO Auto-generated method stub

   }

   @Override
   @ClrMethod("()V")
   public void enableSkeleton() {
      // TODO Auto-generated method stub

   }

   @Override
   @ClrMethod("()V")
   public void ensureRunning() {
      // TODO Auto-generated method stub

   }

   @Override
   @ClrMethod("()LSystem/String;")
   public String getSensors() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   @ClrMethod("()LSystem/String;")
   public String getSkeleton() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   @ClrMethod("()LSystem/String;")
   public String start() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   @ClrMethod("()LSystem/String;")
   public String status() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   @ClrMethod("()LSystem/String;")
   public String stop() {
      // TODO Auto-generated method stub
      return null;
   }

}
