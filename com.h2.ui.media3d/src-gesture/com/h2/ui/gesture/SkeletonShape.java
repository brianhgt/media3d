package com.h2.ui.gesture;

import java.util.HashMap;
import java.util.Map;

import com.h2.ui.gesture.model.Joint;
import com.h2.ui.gesture.model.JointType;
import com.h2.ui.gesture.model.Skeleton;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class SkeletonShape extends Node {
   
   private Map<JointType, Node> joints = new HashMap<JointType, Node>();
   
   private AssetManager assetManager;
   
   public SkeletonShape(AssetManager assetManager) {
      this.assetManager = assetManager;
      
      computeMesh();
   }
   
   protected void computeMesh() {
      Node rootNode = new Node();
      
      rootNode.scale(5.0F);
      
      for (JointType type : JointType.values()) {
         addJoint(rootNode, new Joint(type));
      }
      this.attachChild(rootNode);
   }

   public void update(Skeleton skeleton) {
      if (skeleton == null) {
         return;
      }
      
      for (Joint joint : skeleton.getJoints()) {
         Node transNode = getJoints().get(joint.getJointType());
         transNode.setLocalTranslation(joint.getX(), joint.getY(), joint.getZ());
      }
   }
   
   
   private void addJoint(Node rootNode, Joint joint) {
      
      Node transNode = new Node();
      
      transNode.setLocalTranslation(joint.getX(), joint.getY(), joint.getZ());
      
      Sphere sphere = new Sphere(10, 10, 0.07f);
//      sphere.setMode(Mode.Triangles);
      
      Geometry sphereGeo = new Geometry(joint.getJointType().toString(), sphere);
      Material mat_tl = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
      mat_tl.setTexture("ColorMap", assetManager.loadTexture("textures/elmo_01.png"));
//      mat_tl.setTexture("ColorMap", assetManager.loadTexture("textures/mars_01.png"));
      sphereGeo.setMaterial(mat_tl);
      transNode.attachChild(sphereGeo);
      
      rootNode.attachChild(transNode);
      
      getJoints().put(joint.getJointType(), transNode);
   }

   /**
    * @return the joints
    */
   public Map<JointType, Node> getJoints() {
      return joints;
   }

   /**
    * @param joints the joints to set
    */
   public void setJoints(Map<JointType, Node> joints) {
      this.joints = joints;
   }

}
