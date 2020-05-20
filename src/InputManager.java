/*
 * InputManager.java
 * 
 * Tayler Mauk
 * 
 * Handles input events
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener
{
   // Player 1 controls
   private boolean isWKeyDown;
   private boolean isSKeyDown;

   // Player 2 controls
   private boolean isUpKeyDown;
   private boolean isDownKeyDown;
   
   ////////// CONSTRUCTORS //////////
   
   public InputManager()
   {
      isWKeyDown = false;
      isSKeyDown = false;
      isUpKeyDown = false;
      isDownKeyDown = false;
   }
   
   ////////// INTERNAL //////////
   
   public void update()
   {
      // Player 1
      if (isWKeyDown)
         jPong.getGameSimulation().movePaddle1(PongGameFramework.UP_DIRECTION);
      if (isSKeyDown)
         jPong.getGameSimulation().movePaddle1(PongGameFramework.DOWN_DIRECTION);

      // Player 2
      if (isUpKeyDown)
         jPong.getGameSimulation().movePaddle2(PongGameFramework.UP_DIRECTION);
      if (isDownKeyDown)
         jPong.getGameSimulation().movePaddle2(PongGameFramework.DOWN_DIRECTION);
         
   }
   
   ////////// OVERRIDES //////////
   
   // Used for debug
   @Override
   public String toString()
   {
      return "Player 1: " + isWKeyDown + ", " + isSKeyDown +
            "\nPlayer 2: " + isUpKeyDown + ", " + isDownKeyDown;
   }

   @Override
   public void keyPressed(KeyEvent e)
   {
      int key = e.getKeyCode();
      
      if (key == KeyEvent.VK_SPACE)
      {
         jPong.broadcast(jPong.Messages.TOGGLE_PAUSE);
         return;
      }
      
      if (key == KeyEvent.VK_N)
      {
         jPong.broadcast(jPong.Messages.NEW_GAME);
         return;
      }

      // Check for player 1
      if (key == KeyEvent.VK_W)
         isWKeyDown = true;
      if (key == KeyEvent.VK_S)
         isSKeyDown = true;

      // Check for player 2
      if (key == KeyEvent.VK_UP)
         isUpKeyDown = true;
      if (key == KeyEvent.VK_DOWN)
         isDownKeyDown = true;
      
   }

   @Override
   public void keyReleased(KeyEvent e)
   {
      int key = e.getKeyCode();
      
      // Release for player 1
      if (key == KeyEvent.VK_W)
         isWKeyDown = false;
      if (key == KeyEvent.VK_S)
         isSKeyDown = false;

      // Release for player 2
      if (key == KeyEvent.VK_UP)
         isUpKeyDown = false;
      if (key == KeyEvent.VK_DOWN)
         isDownKeyDown = false;
      
   }

   @Override
   public void keyTyped(KeyEvent e) {}
   
}
