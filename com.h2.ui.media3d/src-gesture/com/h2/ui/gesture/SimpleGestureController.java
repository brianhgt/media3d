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
      
      
      Vector3f armRight = elbowRight.subtract(wristRight);
      armRight.normalizeLocal();
      
      float angleX = Vector3f.UNIT_X.angleBetween(armRight);
      float angleZ = Vector3f.UNIT_Z.angleBetween(armRight);
      System.out.println("angleX = " + angleX);
      System.out.println("angleZ = " + angleZ);
      
      //1.95 - 0.55
      
      if (camera != null) {
         String name;
         if (angleX < 0) {
            name = OrbitCamera.ChaseCamMoveLeft;
         }
         else {
            name = OrbitCamera.ChaseCamMoveRight;
         }
         
         camera.setCanRotate(true);
         camera.onAnalog(name, angleX / 100F, 0.0F);
         camera.setCanRotate(false);
      }
   }
}
