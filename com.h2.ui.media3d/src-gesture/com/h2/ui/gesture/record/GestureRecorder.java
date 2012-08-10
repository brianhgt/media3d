package com.h2.ui.gesture.record;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.h2.ui.gesture.ISkeletonEventListener;
import com.h2.ui.gesture.model.Joint;
import com.h2.ui.gesture.model.JointType;
import com.h2.ui.gesture.model.Skeleton;
import com.h2.ui.gesture.model.SkeletonRoot;
import com.thoughtworks.xstream.XStream;

public class GestureRecorder {
   
   /** Used for logging */
   private static final Logger LOG = LoggerFactory
         .getLogger(GestureRecorder.class);
   
   
   public static final String START = "START_RECORD";
   
   public static final String STOP = "STOP_RECORD";
   
   public static final String LOOP = "LOOP_RECORD";
   
   public static final String PRINT = "PRINT_RECORD";
   
   public static final String PAUSE = "PAUSE_RECORD";
   
   ////
   
   private String _outputDir;
   
   private String _inputDir;
   
   ////
   
   private List<Skeleton> skeletons;
   
   private Boolean recording = false;
   
   private Boolean print = false;

   private Boolean loop = false;
   
   private Boolean pause = false;
   
   ////
   ////
   
   public void startRecord() {
      setSkeletons(new ArrayList<Skeleton>());
      setRecording(true);
   }
   
   public void stopRecord() {
      setRecording(false);
      write("test");
   }
   
   public ISkeletonEventListener playBack(final ISkeletonEventListener listener,
         String name, Integer startFrame, Integer stopFrame, Boolean loop) {
      setLoop(loop);
      final List<Skeleton> skeletons = load(new File(getInputDir(), name + ".xml"));
      
      final int start = (startFrame == null ? 0 : startFrame);
      final int stop = (stopFrame == null ? skeletons.size() : stopFrame);
      
      Thread thread = new Thread(new Runnable() {
         @Override
         public void run() {
            
            try {
               Thread.sleep(1000);
            } catch (InterruptedException exp) {
               exp.printStackTrace();
            }
            
            long time = 0;
            long timeNext = 0;
            
            do {
               for (int i = start; i < stop; i++) {
                  
                  while(getPause()) {
                     try {
                        Thread.sleep(1000);
                     } catch (InterruptedException exp) {
                        //empty
                     }
                  }
                  
                  listener.onEvent(skeletons.get(i));
                  
                  if (getPrint()) {
                     LOG.debug("Frame: " + i);
                  }
                  
                  if (i + 1 < skeletons.size()) {
                     time = skeletons.get(i).getTimeRecorded();
                     timeNext = skeletons.get(i + 1).getTimeRecorded();
                     
                     try {
                        Thread.sleep(timeNext - time);
                     } catch (InterruptedException exp) {
                        exp.printStackTrace();
                     }
                  }
               }
            } while(getLoop());
            
         }
      });
      thread.start();
      
      return listener;
   }
   
   public void record(Skeleton skeleton) {
      skeleton.setTimeRecorded(System.currentTimeMillis());
      getSkeletons().add(skeleton);
   }
   
   public void write(String name) {
      write(new File(getOutputDir(), name + ".xml"), getSkeletons());
   }
   
   public void write(File file, List<Skeleton> Skeletons) {
      
      XStream xstream = new XStream();
      applyAlias(xstream);
      
      String xml = xstream.toXML(new SkeletonRoot(Skeletons));
      
      FileOutputStream fos = null;
      try {
         fos = new FileOutputStream(file);
         
         fos.write(xml.getBytes());
         fos.flush();
         fos.close();
         
         LOG.debug("File has been saved: " + file);
      } catch (IOException exp) {
         LOG.error(exp.getMessage());
         LOG.debug("Details: ", exp);
      }
   }
   
   private List<Skeleton> load(File input) {
      XStream xstream = new XStream();
      applyAlias(xstream);
      
      SkeletonRoot root = null;
      InputStream is = null;
      try {
         is = new FileInputStream(input);
         root = (SkeletonRoot)xstream.fromXML(is);
         is.close();
         LOG.debug("Item has been loaded: " + input);
         return root.getSkeletons();
      } catch (IOException exp) {
         LOG.error(exp.getMessage());
         LOG.error("Details: ", exp);
         return Collections.emptyList();
      }
   }
   
   private void applyAlias(XStream xstream) {
      xstream.addDefaultImplementation(ArrayList.class, Collection.class);
      
      xstream.alias("skeletons", SkeletonRoot.class);
      xstream.addImplicitCollection(SkeletonRoot.class, "skeletons");
      
      xstream.alias("skeleton", Skeleton.class);
      xstream.alias("joint", Joint.class);
      xstream.alias("jointType", JointType.class);
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

   /**
    * @return the outputDir
    */
   public String getOutputDir() {
      return _outputDir;
   }

   /**
    * @param outputDir the outputDir to set
    */
   public void setOutputDir(String outputDir) {
      _outputDir = outputDir;
   }

   /**
    * @return the inputDir
    */
   public String getInputDir() {
      return _inputDir;
   }

   /**
    * @param inputDir the inputDir to set
    */
   public void setInputDir(String inputDir) {
      _inputDir = inputDir;
   }

   /**
    * @return the recording
    */
   public Boolean getRecording() {
      return recording;
   }

   /**
    * @param recording the recording to set
    */
   public void setRecording(Boolean recording) {
      this.recording = recording;
   }
   
   public Boolean getPrint() {
      return print;
   }

   public void setPrint(Boolean print) {
      this.print = print;
   }

   public Boolean getLoop() {
      return loop;
   }

   public void setLoop(Boolean loop) {
      this.loop = loop;
   }

   /**
    * @return the pause
    */
   public Boolean getPause() {
      return pause;
   }

   /**
    * @param pause the pause to set
    */
   public void setPause(Boolean pause) {
      this.pause = pause;
   }

}
