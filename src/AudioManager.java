/*
 * AudioManager.java
 * 
 * Tayler Mauk
 * 
 * Handles audio operations
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioManager
{
   public enum Sounds
   {
      PADDLE_HIT,
      SIDE_HIT,
      SCORE
   }
   
   // Audio playback components
   private AudioInputStream paddleHitAudioIn;
   private Clip paddleHitClip;
   
   private AudioInputStream sideHitAudioIn;
   private Clip sideHitClip;
   
   private AudioInputStream scoreAudioIn;
   private Clip scoreClip;
   
   ////////// CONSTRUCTORS //////////
   
   public AudioManager()
   {
      // All sounds must be buffered or else there is lag on first play back
      try
      {
         paddleHitAudioIn =
               AudioSystem.getAudioInputStream(new File("sounds/paddle_hit.wav"));
         paddleHitClip = AudioSystem.getClip();
         paddleHitClip.open(paddleHitAudioIn);
         
         sideHitAudioIn =
               AudioSystem.getAudioInputStream(new File("sounds/side_hit.wav"));
         sideHitClip = AudioSystem.getClip();
         sideHitClip.open(sideHitAudioIn);
         
         scoreAudioIn =
               AudioSystem.getAudioInputStream(new File("sounds/score.wav"));
         scoreClip = AudioSystem.getClip();
         scoreClip.open(scoreAudioIn);
      }
      catch (Exception e) {e.printStackTrace();}
   }
   
   ////////// INTERNAL //////////
   
   public void play(Sounds sound)
   {
      // Each sound is set to frame 0 to play from beginning
      switch(sound)
      {
      case PADDLE_HIT:
         paddleHitClip.flush();
         paddleHitClip.setFramePosition(0);
         paddleHitClip.start();
         break;
      case SIDE_HIT:
         sideHitClip.flush();
         sideHitClip.setFramePosition(0);
         sideHitClip.start();
         break;
      case SCORE:
         scoreClip.flush();
         scoreClip.setFramePosition(0);
         scoreClip.start();
         break;
      default:
         break;
      }
   }
   
   ////////// OVERRIDES //////////
   
   // Used for debug
   @Override
   public String toString()
   {
      return "paddleHitClip: " + paddleHitClip + "\nsideHitClip: " +
            sideHitClip + "\nscoreClip: " + scoreClip;
   }
}
