package com.h2.ui.media3d.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import microsoftkinectwrapper.ISkeletonFrameHandler;
import microsoftkinectwrapper.KinectHandler;
import net.sf.jni4net.Bridge;
import net.sf.jni4net.attributes.ClrMethod;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;
import com.h2.ui.gesture.SimpleGestureController;
import com.h2.ui.gesture.SkeletonShape;
import com.h2.ui.gesture.model.Joint;
import com.h2.ui.gesture.model.JointType;
import com.h2.ui.gesture.model.Skeleton;
import com.h2.ui.gesture.model.SkeletonRoot;
import com.h2.ui.media3d.com.jme3.asset.plugins.UrlLocator;
import com.h2.ui.media3d.input.OrbitCamera;
import com.h2.ui.media3d.scene.shape.ImageSphere;
import com.h2.ui.media3d.scene.shape.ImageSphere.Entry;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.system.JmeContext.Type;
import com.jme3.texture.plugins.AWTLoader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class VisEntryPoint extends SimpleApplication {
   
   
   public static void main(String[] args) {
      ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
            "classpath:applicationContext.xml");
      SimpleApplication app = ctx.getBean(VisEntryPoint.class);
      app.start();
   }

//   private boolean mock = true;
   private boolean mock = false;
   
   private OrbitCamera camera;
   
   private Skeleton skeleton;
   
   private SkeletonShape skeletonShape;
   
   private SimpleGestureController controller = new SimpleGestureController();
   
   private KinectHandler kinectHandler;
   
   private String libParentDir;
   
   private String libDir;

   @Override
   public void simpleInitApp() {
//      AWTLoader loader = new AWTLoader();
      
      assetManager.registerLoader(AWTLoader.class, "jpg");
      assetManager.registerLocator(null, UrlLocator.class);
      
      YouTubeService service = new YouTubeService("Vis");
      
      try {
         
         
         ImageSphere imageSphere = new ImageSphere(10, 30, 5f);
         
         if (!mock) {
            int pages = 100;
            int maxResults = 50;
            
            for (int startIndex = 0; startIndex < pages; startIndex += maxResults) {
               YouTubeQuery youTubeQuery = new YouTubeQuery(
                     new URL("http://gdata.youtube.com/feeds/api/"
                     + "standardfeeds/top_favorites"));

               youTubeQuery.setStartIndex(startIndex + 1);
               youTubeQuery.setMaxResults(maxResults);
               
               VideoFeed feed = service.query(youTubeQuery, VideoFeed.class);
               
               for (int i = 0; i < feed.getEntries().size(); i++) {
                  if (i + startIndex >= imageSphere.getEntries().size()) {
                     break;
                  }
                  VideoEntry entry = feed.getEntries().get(i);
                  
                  Geometry geo = imageSphere.getEntries().get(i + (startIndex))
                        .getGeometry();
                  Material mat = new Material(assetManager,
                        "Common/MatDefs/Misc/ColoredTextured.j3md");
//                  if (mock) {
//                     mat.setTexture("ColorMap", assetManager.loadTexture(
//                           String.format("%03d", (i + page * 50) % 50) + ".jpg"));
//                  }
//                  else {
                     mat.setTexture("ColorMap", assetManager.loadTexture(
                           entry.getMediaGroup().getThumbnails().get(1).getUrl()));
//                  }
                  
                  
//                  URL thumb = new URL(entry.getMediaGroup().getThumbnails().get(0)
//                        .getUrl());
//                  
//                  mat.setTexture("ColorMap", new Texture2D(
//                        loader.load(thumb.openStream(), false)));
                  
                  geo.setMaterial(mat);
               }
               
            }
         }
         
         for (Entry entry : imageSphere.getEntries()) {
            if (entry.getGeometry().getMaterial() == null) {
               Material mat = new Material(assetManager,
                     "Common/MatDefs/Misc/ColoredTextured.j3md");
               mat.setTexture("ColorMap",
                     assetManager.loadTexture("textures/elmo_01.png"));
               entry.getGeometry().setMaterial(mat);
            }
         }
         
         System.out.println("Size: " + imageSphere.getEntries().size());
         
         camera = new OrbitCamera(getCam(), imageSphere, inputManager);
         camera.setEnabled(true);
         camera.setDragToRotate(true);
         camera.setSmoothMotion(true);
         camera.setInvertVerticalAxis(true);
         camera.setMaxVerticalRotation(FastMath.QUARTER_PI);
         camera.setMinVerticalRotation(-FastMath.QUARTER_PI);
         camera.setChasingSensitivity(1f);
         camera.setRotationSensitivity(15f);
         camera.setZoomSensitivity(2f);
         
         rootNode.attachChild(imageSphere);
         
      } catch (MalformedURLException exp) {
         // TODO Auto-generated catch block
         exp.printStackTrace();
      } catch (IOException exp) {
         // TODO Auto-generated catch block
         exp.printStackTrace();
      } catch (ServiceException exp) {
         // TODO Auto-generated catch block
         exp.printStackTrace();
      }
      
      
      
      ////
      skeletonShape = new SkeletonShape(assetManager);
      
      try {
        
       File parentLibDir = new File(getLibParentDir());
       File libDir = new File(parentLibDir, getLibDir());
       Bridge.init(libDir);
       
       File path = new File(libDir, "KinectServer.j4n.dll").getCanonicalFile();
       Bridge.LoadAndRegisterAssemblyFrom(path);
       
       kinectHandler = new KinectHandler();
       System.out.println(kinectHandler.start());
       
       kinectHandler.addSkeletonHandler(new ISkeletonFrameHandler() {
          
          XStream xstream;
          {
             xstream = new XStream();
             applyAlias(xstream);
          }
          
          @Override
          @ClrMethod("(LSystem/String;)V")
          public void onEvent(String event) {
             SkeletonRoot root = (SkeletonRoot) xstream.fromXML(event);
             Collection<Skeleton> items = root.getSkeletons();
             for (Skeleton item : items) {
//                skeleton.update(item);
                skeleton = item;
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
       
    
    } catch (IOException exp) {
       // TODO Auto-generated catch block
       exp.printStackTrace();
    }
    
    rootNode.attachChild(skeletonShape);
      
      
      ////
      

      /** Must add a light to make the lit object visible! */
      DirectionalLight sun = new DirectionalLight();
      sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
      sun.setColor(ColorRGBA.White);
      rootNode.addLight(sun);
      
      //camera changes
//      getFlyByCamera().setDragToRotate(true);
//      getFlyByCamera().setMoveSpeed(12f);
      
//      
//      getCamera().setLocation(new Vector3f(10f, 0f, 0f));
//      getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
   }
   
   @Override
   public void update() {
      
      controller.update(camera, skeleton);
//      rootNode.updateGeometricState();
//      inputManager.update(timer.getTimePerFrame() * speed);
      
      super.update();
//      if (camera != null) {
//         camera.update(timer.getTimePerFrame() * speed);
//      }
   }
   
   public void initInputCamera(InputManager inputManager) {
      
      if (context.getType() == Type.Display) {
         inputManager.addMapping(INPUT_MAPPING_EXIT,
               new KeyTrigger(KeyInput.KEY_ESCAPE));
     }
      
      inputManager.addMapping(INPUT_MAPPING_CAMERA_POS, new KeyTrigger(KeyInput.KEY_C));
      inputManager.addMapping(INPUT_MAPPING_MEMORY, new KeyTrigger(KeyInput.KEY_M));
      inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F5));
      inputManager.addListener(actionListener, INPUT_MAPPING_EXIT,
              INPUT_MAPPING_CAMERA_POS, INPUT_MAPPING_MEMORY, INPUT_MAPPING_HIDE_STATS);
      
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

}
