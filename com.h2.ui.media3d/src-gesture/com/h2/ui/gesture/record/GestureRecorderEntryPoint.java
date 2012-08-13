package com.h2.ui.gesture.record;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import microsoftkinectwrapper.IKinectHandler;
import microsoftkinectwrapper.ISkeletonFrameHandler;
import microsoftkinectwrapper.KinectHandler;
import net.sf.jni4net.Bridge;
import net.sf.jni4net.attributes.ClrMethod;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.h2.ui.gesture.ISkeletonEventListener;
import com.h2.ui.gesture.SimpleGestureController;
import com.h2.ui.gesture.SkeletonShape;
import com.h2.ui.gesture.model.Joint;
import com.h2.ui.gesture.model.JointType;
import com.h2.ui.gesture.model.Skeleton;
import com.h2.ui.gesture.model.SkeletonRoot;
import com.h2.ui.media3d.com.jme3.asset.plugins.UrlLocator;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.texture.plugins.AWTLoader;
import com.thoughtworks.xstream.XStream;

public class GestureRecorderEntryPoint extends SimpleApplication {
   
   
   public static void main(String[] args) {
      ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
            "classpath:gesture-ctx.xml");
      SimpleApplication app = ctx.getBean(GestureRecorderEntryPoint.class);
      app.start();
   }
   
   private Skeleton skeleton;
   
   private SkeletonShape skeletonShape;
   
   private SimpleGestureController controller = new SimpleGestureController();
   
   private IKinectHandler kinectHandler;
   
   private String libParentDir;
   
   private String libDir;
   
   private GestureRecorder recorder;
   
   private ISkeletonEventListener skeletonListener;

   @Override
   public void simpleInitApp() {
      
      assetManager.registerLoader(AWTLoader.class, "jpg");
      assetManager.registerLocator(null, UrlLocator.class);
      
      skeletonShape = new SkeletonShape(assetManager);
      
      //Setup Kinect
      initKinect(true);
      
      
      rootNode.attachChild(skeletonShape);
      
      /** Must add a light to make the lit object visible! */
      DirectionalLight sun = new DirectionalLight();
      sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
      sun.setColor(ColorRGBA.White);
      rootNode.addLight(sun);
      
      //camera changes
      getFlyByCamera().setDragToRotate(true);
      getFlyByCamera().setMoveSpeed(12f);
      
//      
//      getCamera().setLocation(new Vector3f(10f, 0f, 0f));
//      getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
      
      initRecordInput();
   }
   
   protected void initKinect(Boolean recording) {
      
      if (recording) {
         getRecorder().playBack(new ISkeletonEventListener() {
            
            @Override
            public void onEvent(Skeleton skeletonInput) {
               skeleton = skeletonInput;
            }
         }, "test", 760, null, true);
         
         kinectHandler = new RecordedKinect();
         
         return;
      }
      
      ////
      
      try {
         File parentLibDir = new File(getLibParentDir());
         File libDir = new File(parentLibDir, getLibDir());
         Bridge.init(libDir);
         
         File path = new File(libDir, "KinectServer.j4n.dll").getCanonicalFile();
         Bridge.LoadAndRegisterAssemblyFrom(path);
         
      } catch (IOException exp) {
         // TODO Auto-generated catch block
         exp.printStackTrace();
      }
      
      kinectHandler = new KinectHandler();
      System.out.println(kinectHandler.start());
      
      //add listener for callback events
      setSkeletonListener(new ISkeletonEventListener() {
         
         @Override
         public void onEvent(Skeleton skeletonInput) {
            skeleton = skeletonInput;
            
            if (getRecorder().getRecording()) {
               skeletonInput.setTimeRecorded(System.currentTimeMillis());
               getRecorder().record(skeletonInput);
            }
         }
      });
      
      kinectHandler.addSkeletonHandler(new ISkeletonFrameHandler() {
         
         private XStream xstream;
         {
            xstream = new XStream();
            applyAlias(xstream);
         }
         
         @Override
         @ClrMethod("(LSystem/String;)V")
         public void onEvent(String event) {
            SkeletonRoot root = (SkeletonRoot) xstream.fromXML(event);
            List<Skeleton> items = root.getSkeletons();
            if (items != null && !items.isEmpty()) {
               getSkeletonListener().onEvent(items.get(0));
            }
            rootNode.updateGeometricState();
         }
         
         private void applyAlias(XStream xstream) {
            
            xstream.addDefaultImplementation(ArrayList.class, Collection.class);
            
            xstream.alias("skeletons", SkeletonRoot.class);
            xstream.addImplicitCollection(SkeletonRoot.class, "skeletons");
            
            xstream.alias("skeleton", Skeleton.class);
            xstream.alias("joint", Joint.class);
            xstream.alias("jointType", JointType.class);
         }
      });
      
      Thread thread = new Thread(new Runnable() {
         
         @Override
         public void run() {
            while(true) {
               kinectHandler.ensureRunning();
               try {
                  Thread.sleep(5000);
               } catch (InterruptedException exp) {
                  exp.printStackTrace();
               }
            }
         }
      });
      thread.setDaemon(false);
      thread.start();
      
      kinectHandler.enableSkeleton();
   }
   
   public void initRecordInput() {
      
      inputManager.addMapping(GestureRecorder.START,
            new KeyTrigger(KeyInput.KEY_F9));
      inputManager.addMapping(GestureRecorder.STOP,
            new KeyTrigger(KeyInput.KEY_F10));
      inputManager.addMapping(GestureRecorder.PAUSE,
            new KeyTrigger(KeyInput.KEY_F11));
      inputManager.addMapping(GestureRecorder.LOOP,
            new KeyTrigger(KeyInput.KEY_F12));
      inputManager.addMapping(GestureRecorder.PRINT,
            new KeyTrigger(KeyInput.KEY_P));
      
      inputManager.addListener(new ActionListener() {

         public void onAction(String name, boolean value, float tpf) {
             if (!value) {
                 return;
             }
             else if (name.equals(GestureRecorder.START)) {
                getRecorder().startRecord();
             }
             else if (name.equals(GestureRecorder.STOP)) {
                getRecorder().stopRecord();
             }
             else if (name.equals(GestureRecorder.LOOP)) {
                
                if (getRecorder().getLoop()) {
                   getRecorder().setLoop(false);
                }
                else {
                   getRecorder().setLoop(true);
                }
             }
             else if (name.equals(GestureRecorder.PRINT)) {
                
                if (getRecorder().getPrint()) {
                   getRecorder().setPrint(false);
                }
                else {
                   getRecorder().setPrint(true);
                }
             }
             else if (name.equals(GestureRecorder.PAUSE)) {
                
                if (getRecorder().getPause()) {
                   getRecorder().setPause(false);
                }
                else {
                   getRecorder().setPause(true);
                }
             }
         }
     },
     GestureRecorder.START, GestureRecorder.STOP, GestureRecorder.LOOP,
     GestureRecorder.PRINT, GestureRecorder.PAUSE);
   }
   
   @Override
   public void update() {
      
      skeletonShape.update(skeleton);
      controller.update(null, skeleton);
      
      super.update();
   }
   
   @Override
   protected void finalize() throws Throwable {
      kinectHandler.stop();
      
      super.finalize();
   }

   /**
    * @return the libParentDir
    */
   public String getLibParentDir() {
      return libParentDir;
   }

   /**
    * @param libParentDir the libParentDir to set
    */
   public void setLibParentDir(String libParentDir) {
      this.libParentDir = libParentDir;
   }

   /**
    * @return the libDir
    */
   public String getLibDir() {
      return libDir;
   }

   /**
    * @param libDir the libDir to set
    */
   public void setLibDir(String libDir) {
      this.libDir = libDir;
   }

   /**
    * @return the recorder
    */
   public GestureRecorder getRecorder() {
      return recorder;
   }

   /**
    * @param recorder the recorder to set
    */
   public void setRecorder(GestureRecorder recorder) {
      this.recorder = recorder;
   }

   /**
    * @return the skeletonListener
    */
   public ISkeletonEventListener getSkeletonListener() {
      return skeletonListener;
   }

   /**
    * @param skeletonListener the skeletonListener to set
    */
   public void setSkeletonListener(ISkeletonEventListener skeletonListener) {
      this.skeletonListener = skeletonListener;
   }

}
