package com.h2.ui.gesture;

import java.util.HashMap;
import java.util.Map;

import com.h2.ui.gesture.model.Joint;
import com.h2.ui.gesture.model.JointType;
import com.h2.ui.gesture.model.Skeleton;
import com.h2.ui.media3d.input.OrbitCamera;
import com.jme3.math.Vector3f;

public class SimpleGestureController {

   public void update(OrbitCamera camera, Skeleton skeleton) {
      if (skeleton == null) {
         return;
      }
      
      Map<JointType, Joint> joints = new HashMap<JointType, Joint>();
      for (Joint joint : skeleton.getJoints()) {
         joints.put(joint.getJointType(), joint);
      }
      Joint joint;
      
      joint = joints.get(JointType.ElbowRight);
      Vector3f elbowRight = new Vector3f(joint.getX(), joint.getY(), joint.getZ());
      joint = joints.get(JointType.WristRight);
      Vector3f wristRight = new Vector3f(joint.getX(), joint.getY(), joint.getZ());
      
      joint = joints.get(JointType.ElbowLeft);
      Vector3f elbowLeft = new Vector3f(joint.getX(), joint.getY(), joint.getZ());
      joint = joints.get(JointType.WristLeft);
      Vector3f wristLeft = new Vector3f(joint.getX(), joint.getY(), joint.getZ());
      
      
      Vector3f armLeft = elbowLeft.subtract(wristLeft);
      armLeft.normalizeLocal();
      
      Vector3f armRight = elbowRight.subtract(wristRight);
      armRight.normalizeLocal();
      
      float angleRightX = Vector3f.UNIT_X.angleBetween(armRight);
      float angleRightZ = Vector3f.UNIT_Z.angleBetween(armRight);
//      System.out.println("angleRightX = " + angleRightX);
//      System.out.println("angleRightZ = " + angleRightZ);
      
      float xMax = 1.5F;
      float xMin = 1.3F;
      float xMid = (xMax - xMin) / 2 + xMin;
      
      float angleLeftZ = Vector3f.UNIT_Z.angleBetween(armLeft);
      System.out.println("angleLeftZ = " + angleLeftZ);
      
      float zLeftMax = 1.1F;
      float zLeftMin = 0.7F;
      float zLeftMid = (zLeftMax - zLeftMin) / 2 + zLeftMin;
      
//      System.out.println("Left = " + (angleX < xMid));
//      System.out.println("Up = " + (angleZ < xMid));
      
      if (camera != null) {
         String name;
         float value;
         if (angleRightX < xMid) {
            name = OrbitCamera.ChaseCamMoveLeft;
         }
         else {
            name = OrbitCamera.ChaseCamMoveRight;
         }
         
         value = 0.01F * Math.abs(angleRightX - xMid);
         value = Math.min(value, 0.005F);
         System.out.println("value = " + value);
         
         camera.setCanRotate(true);
         camera.onAnalog(name, value, 0.0F);
         camera.setCanRotate(false);
         
         
         if (angleLeftZ < zLeftMid) {
            name = OrbitCamera.ChaseCamZoomIn;
         }
         else {
            name = OrbitCamera.ChaseCamZoomOut;
         }
         
         value = 0.01F * Math.abs(angleLeftZ - zLeftMid);
         value = Math.min(value, 0.005F);
         System.out.println("value = " + value);
         
         camera.setCanRotate(true);
         camera.onAnalog(name, value, 0.0F);
         camera.setCanRotate(false);
      }
   }
}
