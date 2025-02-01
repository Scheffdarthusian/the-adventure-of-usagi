import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class FlappyPurr extends JPanel implements ActionListener, KeyListener {
  private final double WIN_CONDITION = 10;

  private final int boardWidth = 608, boardHeight = 672;
  private final int birdX = boardWidth / 8, birdY = boardHeight / 2;
  private final int birdWidth = 57, birdHeight = 57;
  private final int pipeWidth = 64, pipeHeight = 512;
  private int velocityX = -4, velocityY = 0, gravity = 1;
  private double score = 0;
  private boolean gameOver = false, gameClear = false;

  private Sound sound = new Sound();
  private Image backGroundImage, birdImage, topPipImage, bottomPipImage;
  private Image gameOverImage, gameClearImage;

  private Bird bird;
  private ArrayList<Pipe> pipes = new ArrayList<>();
  private Timer gameLoop, placePipesTimer;

  public FlappyPurr() {
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setFocusable(true);
    addKeyListener(this);
    loadImages();
    bird = new Bird(birdImage);

    placePipesTimer = new Timer(1700, e -> placePipes());
    gameLoop = new Timer(1000 / 60, this);
  }

  private void loadImages() {
    backGroundImage = new ImageIcon(getClass().getResource("/FlappyPurrAssets/gRvB7u.jpg")).getImage();
    birdImage = new ImageIcon(getClass().getResource("/FlappyPurrAssets/FlappyPurr.png")).getImage();
    topPipImage = new ImageIcon(getClass().getResource("/FlappyPurrAssets/toppipe.png")).getImage();
    bottomPipImage = new ImageIcon(getClass().getResource("/FlappyPurrAssets/bottompipe.png")).getImage();
    gameOverImage = new ImageIcon(getClass().getResource("/FlappyPurrAssets/usagi_fell.jpg")).getImage();
    gameClearImage = new ImageIcon(getClass().getResource("/FlappyPurrAssets/usagi_landed.png")).getImage();
  }

  public void placePipes() {
    int randomPipeY = (int) (0 - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
    int openingSpace = boardHeight / 4;

    Pipe topPipe = new Pipe(topPipImage);
    topPipe.y = randomPipeY;
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipImage);
    bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
    pipes.add(bottomPipe);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  private void draw(Graphics g) {
    // Draw background, bird and pipes
    g.drawImage(backGroundImage, 0, 0, boardWidth, boardHeight, null);
    g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
    for (Pipe pipe : pipes) {
      g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
    }

    // Draw score (common to all states)
    drawScore(g);

    // Overlay game state messages if needed
    if (gameOver || gameClear) {
      playMusicOnce(2);
      if (gameOver) {
        g.drawImage(gameOverImage, 4, 6, null);
        drawCenteredText(g, "おちた！ " + (int) score);
      } else {
        g.drawImage(gameClearImage, 0, 0, null);
        drawCenteredText(g, "着陸！ ");
      }
    }
  }

  private void drawScore(Graphics g) {
    String scoreStr = " Score: " + (int) score;
    g.setFont(new Font("Meiryo", Font.BOLD, 24));
    // Draw black border
    g.setColor(Color.BLACK);
    g.drawString(scoreStr, 9, 39);
    // Draw score in white on top
    g.setColor(Color.WHITE);
    g.drawString(scoreStr, 10, 40);
  }

  private void drawCenteredText(Graphics g, String text) {
    g.setFont(new Font("Meiryo", Font.BOLD, 48));
    // Draw with slight offsets for border
    g.setColor(Color.BLACK);
    g.drawString(text, 119, 299);
    g.drawString(text, 120, 300);
    g.drawString(text, 121, 301);
    g.drawString(text, 122, 302);
    // Main text in white
    g.setColor(Color.WHITE);
    g.drawString(text, 120, 300);
  }

  private void move() {
    // Update bird
    velocityY += gravity;
    bird.y = Math.max(bird.y + velocityY, 0);

    // Update pipes and check collision and score
    for (Pipe pipe : pipes) {
      pipe.x += velocityX;
      if (!pipe.passed && bird.x > pipe.x + pipe.width) {
        pipe.passed = true;
        score += 0.5;
        if (score >= WIN_CONDITION) {
          gameClear = true;
        }
      }
      if (collision(bird, pipe)) {
        gameOver = true;
      }
    }
    if (bird.y > boardHeight)
      gameOver = true;
  }

  private boolean collision(Bird a, Pipe b) {
    return a.x < b.x + b.width && a.x + a.width > b.x &&
        a.y < b.y + b.height && a.y + a.height > b.y;
  }

  private void adjustPipeDelay() {
    if (score > 10)
      placePipesTimer.setDelay(1600);
    if (score > 20)
      placePipesTimer.setDelay(1500);
    if (score > 30)
      placePipesTimer.setDelay(1400);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    adjustPipeDelay();
    if (gameOver || gameClear) {
      placePipesTimer.stop();
      gameLoop.stop();
      stopMusic();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      velocityY = -9;
      if (gameOver || gameClear)
        restartGame();
    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      gameLoop.start();
      placePipesTimer.start();
      playMusic(0);
    }
  }

  private void restartGame() {
    bird.y = birdY;
    velocityY = 0;
    pipes.clear();
    score = 0;
    gameOver = false;
    gameClear = false;
    gameLoop.start();
    placePipesTimer.start();
    playMusic(0);

  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  public void playMusic(int i) {
    sound.setFile(i);
    sound.play();
    sound.loop();
  }

  public void playMusicOnce(int i) {
    sound.setFile(i);
    sound.play();
  }

  public void stopMusic() {
    sound.stop();
  }

  // Inner classes
  class Bird {
    int x = birdX, y = birdY, width = birdWidth, height = birdHeight;
    Image img;

    Bird(Image img) {
      this.img = img;
    }
  }

  class Pipe {
    int x = boardWidth, y = 0, width = pipeWidth, height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe(Image img) {
      this.img = img;
    }
  }
}
