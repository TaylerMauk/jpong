/*
 * PongGameFramework.java
 * 
 * Tayler Mauk
 * 
 * Outlines the rules for Pong
 */

import java.util.Random;

public class PongGameFramework implements Runnable
{
   public static final int FIELD_WIDTH = 330;
   public static final int FIELD_HEIGHT = 240;
   public static final int PADDLE_WIDTH = FIELD_WIDTH / 70;
   public static final int PADDLE_HEIGHT = PADDLE_WIDTH * 5;
   public static final int PADDLE_MOVE_SPEED = 1;
   public static final int BALL_DIAMETER = PADDLE_HEIGHT / 4;
   
   // Area behind the paddles (the score area)
   public static final int PADDLE_HORIZONTAL_PAD =
         (int)Math.round(FIELD_WIDTH * 0.03);
   
   // For readability of directions
   public static final int UP_DIRECTION = -1;
   public static final int DOWN_DIRECTION = 1;
   
   // In original Pong, the paddles could not touch the top or bottom of screen.
   // These pads help recreate that behavior
   private final int PADDLE_MOVEMENT_TOP_PAD = FIELD_HEIGHT / 20;
   private final int PADDLE_MOVEMENT_BOTTOM_PAD = 
         (FIELD_HEIGHT - PADDLE_HEIGHT) - PADDLE_MOVEMENT_TOP_PAD;
   
   // Delay (in milliseconds) to wait after score
   private final int SCORE_DELAY = 3000;
   
   private PongObject ball;
   private PongObject paddle1;
   private PongObject paddle2;
   
   // Used for colliding with objects on right and bottom of ball
   private int ballBouncePad;
   
   private int player1Score;
   private int player2Score;
   private int winningPlayer;

   private boolean isGamePaused;
   private boolean isPlaying;
   private boolean someoneScored;
   
   ////////// CONSTRUCTORS //////////
   
   public PongGameFramework()
   {
      isPlaying = false;
      
      ballBouncePad = FIELD_HEIGHT - BALL_DIAMETER;
      
      ball = new PongObject();
      paddle1 = new PongObject();
      paddle2 = new PongObject();
      
      reset();
   }
   
   ////////// ACCESSORS //////////
   
   public boolean isGamePaused()
   {
      return isGamePaused;
   }
   
   ////////// INTERNAL //////////
   
   public void updateBallPosition()
   {
      // Only simulate some of the time (slows object down)
      if (System.currentTimeMillis() % 7 != 0)
         return;
      
      // Can get away with if-else-if structure because the ball cannot be
      // affected by both paddles and the sides in the same frame
      if (doesPaddle1HitBall())
      {
         ball.yDirection = paddle1.yDirection;
         ball.xDirection *= -1;
         
         jPong.getAudioManager().play(AudioManager.Sounds.PADDLE_HIT);
      }
      else if (doesPaddle2HitBall())
      {
         ball.yDirection = paddle2.yDirection;
         ball.xDirection *= -1;
         
         jPong.getAudioManager().play(AudioManager.Sounds.PADDLE_HIT);
      }
      else if ((ball.yPosition >= ballBouncePad) || (ball.yPosition <= 0))
      {
         // Check if ball will collide with edge of field
         // Change direction if it will
         ball.yDirection *= -1;
         jPong.getAudioManager().play(AudioManager.Sounds.SIDE_HIT);
      }
      
      
      ball.xPosition += ball.xDirection;
      ball.yPosition += ball.yDirection;
   }
   
   public void movePaddle1(int direction)
   {
      if (isGamePaused)
         return;
      
      // Only simulate some of the time (slows object down)
      if (System.currentTimeMillis() % 4 != 0)
         return;
      
      // Check for end of paddle movement area
      if (direction == UP_DIRECTION)
      {
         if (paddle1.yPosition <= PADDLE_MOVEMENT_TOP_PAD)
            paddle1.yPosition = PADDLE_MOVEMENT_TOP_PAD;
      }
      else
      {
         if (paddle1.yPosition >= PADDLE_MOVEMENT_BOTTOM_PAD)
            paddle1.yPosition = PADDLE_MOVEMENT_BOTTOM_PAD;
      }
      
      paddle1.yDirection = direction;
      paddle1.yPosition += direction;
   }
   
   public void movePaddle2(int direction)
   {
      if (isGamePaused)
         return;

      // Only simulate some of the time (slows object down)
      if (System.currentTimeMillis() % 4 != 0)
         return;
      
      // Check for end of paddle movement area
      if (direction == UP_DIRECTION)
      {
         if (paddle2.yPosition <= PADDLE_MOVEMENT_TOP_PAD)
            paddle2.yPosition = PADDLE_MOVEMENT_TOP_PAD;
      }
      else
      {
         if (paddle2.yPosition >= PADDLE_MOVEMENT_BOTTOM_PAD)
            paddle2.yPosition = PADDLE_MOVEMENT_BOTTOM_PAD;
      }

      paddle2.yDirection = direction;
      paddle2.yPosition += direction;
   }
   
   public void update()
   {
      // Check for winner
      if (player1Score == 11)
      {
         winningPlayer = 1;
         return;
      }
      
      if (player2Score == 11)
      {
         winningPlayer = 2;
         return;
      }
      
      updateBallPosition();

      // Scoring system (counts once ball falls off board)
      if (ball.xPosition > FIELD_WIDTH)
      {
         ++player1Score;
         someoneScored = true;
      }
      else if (ball.xPosition < -BALL_DIAMETER)
      {
         ++player2Score;
         someoneScored = true;
      }
      
      if (someoneScored)
      {
         // Play score sound
         jPong.getAudioManager().play(AudioManager.Sounds.SCORE);
         
         // Manual repaint to reflect score
         updateVisuals();
         
         try {Thread.sleep(SCORE_DELAY);}
         catch (Exception e) {}
         
         serve();
      }
      
      someoneScored = false;
   }
   
   public void newGame()
   {
      reset();
      isPlaying = true;
   }
   
   public void pause()
   {
      isGamePaused = true;
   }
   
   public void resume()
   {
      isGamePaused = false;
   }
   
   // Initial ball spawn at center line
   private void serve()
   {
      Random rand = new Random();
      
      ball.xDirection = getRandomDirection();
      ball.yDirection = getRandomDirection();
      ball.xPosition = FIELD_WIDTH / 2;
      
      // Ball position is random position in middle 2 quarters of field height
      ball.yPosition = rand.nextInt(
            FIELD_HEIGHT - (FIELD_HEIGHT / 2)) + (FIELD_HEIGHT / 4
            );
   }
   
   // Returns true if ball is colliding with paddle1
   private boolean doesPaddle1HitBall()
   {
      int paddleCollisionBound = paddle1.xPosition + PADDLE_WIDTH;

      // Check if ball is colliding with paddle
      if (paddleCollisionBound == ball.xPosition)
      {
         // Check if top of ball is within paddle height
         if (ball.yPosition >= paddle1.yPosition)
         {
            if (ball.yPosition <= paddle1.yPosition + PADDLE_HEIGHT)
               return true;
         }
         else
         {
            // Check if bottom of ball is within paddle height
            if (ball.yPosition + BALL_DIAMETER >= paddle1.yPosition)
               return true;
         }
      }
      
      return false;
   }

   // Returns true if ball is colliding with paddle2
   private boolean doesPaddle2HitBall()
   {
      int ballCollisionBound = ball.xPosition + BALL_DIAMETER;
      
      // Check if ball is colliding with paddle
      if (ballCollisionBound == paddle2.xPosition)
      {
         // Check if top of ball is within paddle height
         if (ball.yPosition >= paddle2.yPosition)
         {
            if (ball.yPosition <= paddle2.yPosition + PADDLE_HEIGHT)
               return true;
         }
         else
         {
            // Check if bottom of ball is within paddle height
            if (ball.yPosition + BALL_DIAMETER >= paddle2.yPosition)
               return true;
         }
      }
      
      return false;
   }
   
   // Returns a random int between [-1, 1]
   // includes/excludes zero according to includeZero
   private int getRandomDirection()
   {
      Random rand = new Random();
      int dir;
      
      // Get random direction (-1 or 1)
      do
      {
         dir = rand.nextInt(3) - 1;
      
      } while (dir == 0);
         
      return dir;
   }
   
   // Resets game to initial state
   private void reset()
   {
      isGamePaused = false;
      someoneScored = false;

      player1Score = 0;
      player2Score = 0;
      winningPlayer = 0;

      ball.xDirection = getRandomDirection();
      ball.yDirection = getRandomDirection();
      ball.xPosition = FIELD_WIDTH / 2;
      ball.yPosition = FIELD_HEIGHT / 2;
      
      paddle1.xPosition = PADDLE_HORIZONTAL_PAD;
      paddle1.yPosition = FIELD_HEIGHT / 2;
      paddle2.xPosition = FIELD_WIDTH - PADDLE_HORIZONTAL_PAD;
      paddle2.yPosition = FIELD_HEIGHT / 2;
   }
   
   private void ballBounceIdleAnimation()
   {
      // Only simulate some of the time (slows object down)
      if (System.currentTimeMillis() % 7 != 0)
         return;
      
      if ((ball.yPosition >= ballBouncePad) || (ball.yPosition <= 0))
      {
         // Check if ball will "collide" with edge of field
         // Change direction if it will
         ball.yDirection *= -1;
      }
      // Bounce for left and right sides (should not be used during game play)
      else if ((ball.xPosition >= (FIELD_WIDTH - BALL_DIAMETER)) ||
            (ball.xPosition <= 0))
         ball.xDirection *= -1;
      
      
      ball.xPosition += ball.xDirection;
      ball.yPosition += ball.yDirection;
   }
   
   private void updateVisuals()
   {
      jPong.getPlayArea().storeUpdate(
            ball.xPosition, 
            ball.yPosition,
            paddle1.yPosition,
            paddle2.yPosition,
            player1Score,
            player2Score
      );
      jPong.getPlayArea().repaint();
   }
   
   ////////// OVERRIDES //////////
   
   // Used for debug
   @Override
   public String toString()
   {
      return "Field Dimesnions: " + FIELD_WIDTH + "x" + FIELD_HEIGHT + "\n" +
            "Ball Position: " + ball.xPosition + ", " + ball.yPosition + "\n" +
            "Ball Direction: " + ball.xDirection + ", " + ball.yDirection + "\n" +
            "Paddle 1 Position: " + paddle1.xPosition + ", " +
            paddle1.yPosition + "\n" + "Paddle 2 Position: " +
            paddle2.xPosition + ", " + paddle2.yPosition + "\n" +
            "Player 1 Score: " + player1Score + "\n" + "Player 2 Score: " +
            player1Score + "\n";
   }
   
   // Serves as the main game loop
   @Override
   public void run()
   {
      while (true)
      {
         // For idle animation
         ball.xDirection = 1;
         ball.yDirection = 2;
         paddle1.yPosition = -50;
         paddle2.yPosition = -50;

         while (!isPlaying)
         {
            // Show idle animation
            ballBounceIdleAnimation();
            updateVisuals();
            
            try {Thread.sleep(jPong.FRAME_DELAY);}
            catch (Exception e) {}
         }

         while (winningPlayer == 0)
         {
            if (isGamePaused)
            {
               // Do nothing
            }
            else
            {
               jPong.getInputManager().update();
               this.update();
               updateVisuals();
            }
            
            try {Thread.sleep(jPong.FRAME_DELAY);}
            catch (Exception e) {}
         }
         
         // End of match
         isPlaying = false;
      }
   }
}

/*
 * PongObject (helper class)
 * 
 * Serves as (basically) a structure for objects on screen. Object locations
 * are the top left pixel.
 */

class PongObject
{
   public int xPosition;
   public int yPosition;
   public int xDirection;
   public int yDirection;
   
   public PongObject()
   {
      xPosition = 0;
      yPosition = 0;
      xDirection = 0;
      yDirection = 0;
   }
}
