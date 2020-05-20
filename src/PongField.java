/*
 * PongField.java
 * 
 * Tayler Mauk
 * 
 * Defines the visual aspects of the game
 */

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PongField extends JPanel
{
   // Marks horizontal center of field
   private int centerX;
   
   private int centerLineBarWidth;
   private int centerLineBarHeight;
   private int centerLineVerticalPad;
   
   private int ballX;
   private int ballY;
   private int paddle1Y;
   private int paddle2Y;
   private int player1Score;
   private int player2Score;
   
   ////////// CONSTRUCTORS //////////
   
   public PongField()
   {
      super();
      
      // Needed to read input
      setFocusable(true);
      
      centerX = PongGameFramework.FIELD_WIDTH / 2;
      
      centerLineVerticalPad = PongGameFramework.FIELD_HEIGHT / 200;
      centerLineBarWidth = (int)Math.round(PongGameFramework.FIELD_WIDTH * 0.003);
      
      // Divide by 60 because 30 lines should fit on screen with equal blank
      // space between them
      centerLineBarHeight = PongGameFramework.FIELD_HEIGHT / 60;
   }
   
   ////////// INTERNAL //////////
   
   // Stores necessary information from simulation to update visuals
   public void storeUpdate(int ballX, int ballY, int paddle1Y, int paddle2Y, int player1Score, int player2Score)
   {
      this.ballX = ballX;
      this.ballY = ballY;
      this.paddle1Y = paddle1Y;
      this.paddle2Y = paddle2Y;
      this.player1Score = player1Score;
      this.player2Score = player2Score;
   }
   
   ////////// OVERRIDES //////////
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      
      // Background fill
      g.setColor(Color.black);
      g.fillRect(0, 0, PongGameFramework.FIELD_WIDTH, PongGameFramework.FIELD_HEIGHT);
      
      g.setColor(Color.white);
      
      // Scoreboard
      ScoreboardUi.updateScoreboard(g, player1Score, player2Score);
      
      // Ball
      g.fillRect(
            ballX,
            ballY,
            PongGameFramework.BALL_DIAMETER,
            PongGameFramework.BALL_DIAMETER
      );
      
      // Center line
      for (int i = centerLineVerticalPad; i < PongGameFramework.FIELD_HEIGHT;
            i += centerLineBarHeight * 2)
      {
         g.fillRect(
               (centerX - centerLineVerticalPad),
               i,
               centerLineBarWidth,
               centerLineBarHeight
         );
      }
      
      // Left Paddle
      g.fillRect(
            PongGameFramework.PADDLE_HORIZONTAL_PAD,
            paddle1Y,
            PongGameFramework.PADDLE_WIDTH,
            PongGameFramework.PADDLE_HEIGHT
      );
      
      // Right Paddle
      g.fillRect(
            (PongGameFramework.FIELD_WIDTH - PongGameFramework.PADDLE_HORIZONTAL_PAD),
            paddle2Y,
            PongGameFramework.PADDLE_WIDTH,
            PongGameFramework.PADDLE_HEIGHT
      );
   }
}

/*
 * ScoreboardUi (helper class)
 * 
 * Draws alphanumeric shapes to for use on the scoreboard. Functions similar to
 * a 7 segment LED counter.
 */

class ScoreboardUi
{
   private static final int LEFT_SCOREBOARD_ORIGIN_X = PongGameFramework.FIELD_WIDTH / 4;
   private static final int RIGHT_SCOREBOARD_ORIGIN_X = (int)(PongGameFramework.FIELD_WIDTH * 0.75);
   private static final int SCOREBOARD_TOP_PAD = PongGameFramework.FIELD_HEIGHT / 20;
   private static final int OVERALL_SHAPE_WIDTH = PongGameFramework.FIELD_WIDTH / 20;
   private static final int OVERALL_SHAPE_HEIGHT = OVERALL_SHAPE_WIDTH * 2;
   private static final int SHAPE_PIECE_HEIGHT = OVERALL_SHAPE_HEIGHT / 10;
   private static final int SHAPE_SPACER = SHAPE_PIECE_HEIGHT;
   
   private static int scoreboardOriginX;
   
   ////////// INTERNAL //////////
   
   public static void updateScoreboard(Graphics g, int player1Score, int player2Score)
   {
      // Handle player 1's score
      setToUpdateForPlayer(1);
      drawScore(g, player1Score);

      // Handle player 2's score
      setToUpdateForPlayer(2);
      drawScore(g, player2Score);
   }
   
   public static void drawZero(Graphics g)
   {
      drawTopLine(g);
      drawFullLeftLine(g);
      drawFullRightLine(g);
      drawBottomLine(g);
   }
   
   public static void drawOne(Graphics g)
   {
      drawFullLeftLine(g);
   }
   
   public static void drawTwo(Graphics g)
   {
      drawTopLine(g);
      drawLowerHalfLeftLine(g);
      drawUpperHalfRightLine(g);
      drawMiddleLine(g);
      drawBottomLine(g);
   }
   
   public static void drawThree(Graphics g)
   {
      drawTopLine(g);
      drawFullRightLine(g);
      drawMiddleLine(g);
      drawBottomLine(g);
   }
   
   public static void drawFour(Graphics g)
   {
      drawUpperHalfLeftLine(g);
      drawFullRightLine(g);
      drawMiddleLine(g);
   }
   
   public static void drawFive(Graphics g)
   {
      drawTopLine(g);
      drawUpperHalfLeftLine(g);
      drawLowerHalfRightLine(g);
      drawMiddleLine(g);
      drawBottomLine(g);
   }
   
   public static void drawSix(Graphics g)
   {
      drawTopLine(g);
      drawFullLeftLine(g);
      drawLowerHalfRightLine(g);
      drawMiddleLine(g);
      drawBottomLine(g);
   }
   
   public static void drawSeven(Graphics g)
   {
      drawTopLine(g);
      drawFullRightLine(g);
   }
   
   public static void drawEight(Graphics g)
   {
      drawTopLine(g);
      drawFullLeftLine(g);
      drawFullRightLine(g);
      drawMiddleLine(g);
      drawBottomLine(g);
   }
   
   public static void drawNine(Graphics g)
   {
      drawTopLine(g);
      drawFullRightLine(g);
      drawMiddleLine(g);
      drawUpperHalfLeftLine(g);
   }
   
   // Determines which numbers to draw
   private static void drawScore(Graphics g, int score)
   {
      switch(score)
      {
      case 0:
         drawZero(g);
         break;
      case 1:
         drawOne(g);
         break;
      case 2:
         drawTwo(g);
         break;
      case 3:
         drawThree(g);
         break;
      case 4:
         drawFour(g);
         break;
      case 5:
         drawFive(g);
         break;
      case 6:
         drawSix(g);
         break;
      case 7:
         drawSeven(g);
         break;
      case 8:
         drawEight(g);
         break;
      case 9:
         drawNine(g);
         break;
      case 10:
         drawOne(g);
         moveOriginToNextShapeSpace();
         drawZero(g);
         break;
      case 11:
         drawOne(g);
         moveOriginToNextShapeSpace();
         drawOne(g);
         break;
      default:
         drawMiddleLine(g);
      }
   }
   
   // Sets the corresponding origin based on player number
   private static boolean setToUpdateForPlayer(int player)
   {
      // Check for out of bounds
      if (player <= 0 || player > 2)
         return false;
      
      if (player == 1)
         scoreboardOriginX = LEFT_SCOREBOARD_ORIGIN_X;
      else
         scoreboardOriginX = RIGHT_SCOREBOARD_ORIGIN_X;
      
      return true;
   }
   
   private static void moveOriginToNextShapeSpace()
   {
      scoreboardOriginX += OVERALL_SHAPE_WIDTH + SHAPE_SPACER;
   }
   
   // Shape definitions for each segment
   
   private static void drawTopLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX,
            SCOREBOARD_TOP_PAD,
            OVERALL_SHAPE_WIDTH,
            SHAPE_PIECE_HEIGHT
      );
   }
   
   private static void drawMiddleLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX,
            SCOREBOARD_TOP_PAD + (OVERALL_SHAPE_HEIGHT / 2) - SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_WIDTH,
            SHAPE_PIECE_HEIGHT
      );
   }
   
   private static void drawBottomLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX,
            OVERALL_SHAPE_HEIGHT + (SHAPE_PIECE_HEIGHT * 3),
            OVERALL_SHAPE_WIDTH,
            SHAPE_PIECE_HEIGHT
      );
   }
   
   private static void drawFullLeftLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX,
            SCOREBOARD_TOP_PAD,
            SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_HEIGHT
      );
   }
   
   private static void drawUpperHalfLeftLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX,
            SCOREBOARD_TOP_PAD,
            SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_HEIGHT / 2
      );
   }
   
   private static void drawLowerHalfLeftLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX,
            SCOREBOARD_TOP_PAD + (OVERALL_SHAPE_HEIGHT / 2),
            SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_HEIGHT / 2
      );
   }
   
   private static void drawFullRightLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX + (OVERALL_SHAPE_WIDTH  - SHAPE_PIECE_HEIGHT),
            SCOREBOARD_TOP_PAD,
            SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_HEIGHT
      );
   }
   
   private static void drawUpperHalfRightLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX + (OVERALL_SHAPE_WIDTH  - SHAPE_PIECE_HEIGHT),
            SCOREBOARD_TOP_PAD,
            SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_HEIGHT / 2
      );
   }
   
   private static void drawLowerHalfRightLine(Graphics g)
   {
      g.fillRect(
            scoreboardOriginX + (OVERALL_SHAPE_WIDTH  - SHAPE_PIECE_HEIGHT),
            SCOREBOARD_TOP_PAD + (OVERALL_SHAPE_HEIGHT / 2),
            SHAPE_PIECE_HEIGHT,
            OVERALL_SHAPE_HEIGHT / 2
      );
   }
}