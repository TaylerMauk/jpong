/*
 * jPong.java
 * 
 * Tayler Mauk
 * 
 * A Pong clone in Java
 */

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class jPong
{
   // Time (in milliseconds) between each frame
   public static final int FRAME_DELAY = 1;
   
   public enum Messages
   {
      NEW_GAME,
      PAUSE,
      RESUME,
      TOGGLE_PAUSE
   }
   
   private static PongGameFramework gameSimulation;
   private static PongField playArea;
   private static InputManager input;
   private static AudioManager audio;
   
   public static void main(String[] args) throws InterruptedException
   {
      audio = new AudioManager();
      gameSimulation = new PongGameFramework();
      
      // Main window creation
      JFrame mainWindow = new JFrame();
      mainWindow.setTitle("jPong");
      mainWindow.setResizable(false);
      mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Open the arcade cabinet image
      BufferedImage arcadeBox = null;
      try
      {
         arcadeBox = ImageIO.read(new File("images/pong-arcade-box_ui.png"));
      } 
      catch (IOException e) {}
      
      int backgroundWidth = arcadeBox.getWidth();
      int backgroundHeight = arcadeBox.getHeight();
      
      // Need a layered pane to render panel on top of label with image
      JLayeredPane uiContainer = new JLayeredPane();
      uiContainer.setPreferredSize(new Dimension(
            backgroundWidth,
            backgroundHeight
            )
      );
      
      JLabel arcadeUi = new JLabel(new ImageIcon(arcadeBox));
      uiContainer.add(arcadeUi, new Integer(0));
      arcadeUi.setBounds(0, 0, backgroundWidth, backgroundHeight);
      
      playArea = new PongField();
      
      input = new InputManager();
      playArea.addKeyListener(input);
      //uiContainer.add(playArea, new Integer(0));
      
      // Position panel to be inside of CRT
      uiContainer.add(playArea, new Integer(1));
      playArea.setBounds(
            340,
            300,
            PongGameFramework.FIELD_WIDTH,
            PongGameFramework.FIELD_HEIGHT
      );

      // Add to content pane in order to size the frame correctly
      // (JFrame includes the border, which may vary in size)
      mainWindow.getContentPane().add(uiContainer);
      mainWindow.pack();
      mainWindow.setLocationRelativeTo(null);
      mainWindow.setVisible(true);
      
      // Start game simulation in its own thread
      (new Thread(gameSimulation)).start();
   }
   
   // Useful for more complex messages
   public static void broadcast(Messages messageCode)
   {
      switch(messageCode)
      {
      case NEW_GAME:
         gameSimulation.newGame();
         break;
      case PAUSE:
         gameSimulation.pause();
         break;
      case RESUME:
         gameSimulation.resume();
         break;
      case TOGGLE_PAUSE:
         if (gameSimulation.isGamePaused())
            gameSimulation.resume();
         else
            gameSimulation.pause();
         break;
      default:
         break;
      }
   }
   
   ////////// ACCESSORS //////////
   
   public static PongGameFramework getGameSimulation()
   {
      return gameSimulation;
   }
   
   public static InputManager getInputManager()
   {
      return input;
   }
   
   public static AudioManager getAudioManager()
   {
      return audio;
   }
   
   public static PongField getPlayArea()
   {
      return playArea;
   }
}
