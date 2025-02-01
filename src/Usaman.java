import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class Usaman extends JPanel implements ActionListener, KeyListener {
  private static final int LIVES_INIT = 1;
  private static final int WIN_CONDITION = 500;
  private static final int TILE_SIZE = 32;
  private static final int ROW_COUNT = 21;
  private static final int COLUMN_COUNT = 19;
  private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE;
  private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
  
  private Sound sound = new Sound();
  
  private Image treeImage, foodImage;
  private Image goblinRightImage, goblinLeftImage;
  private Image usagiRightImage, usagiLeftImage;
  private Image gameOverImage, gameClearImage;

  private Block Usagi;
  private HashSet<Block> trees, foods, goblins;
  private Timer gameLoop;
  private final char[] directions = { 'U', 'D', 'L', 'R' };
  private Random random = new Random();
  private int score = 0;
  private int lives = LIVES_INIT;
  private boolean gameOver, gameClear;

  // Map: X = tree, O = skip, P = Usagi, ' ' = food, g = goblin.
  private final String[] tileMap = {
      "XXXXXXXXXXXXXXXXXXX",
      "X        X        X",
      "X XX XXX X XXX XX X",
      "X                 X",
      "X XX X XXXXX X XX X",
      "X    X       X    X",
      "XXXX XXXX XXXX XXXX",
      "OOOX X       X XOOO",
      "XXXX X XXgXX X XXXX",
      "x       ggg       x",
      "XXXX X XXXXX X XXXX",
      "OOOX X       X XOOO",
      "XXXX X XXXXX X XXXX",
      "X        X        X",
      "X XX XXX X XXX XX X",
      "X  X     U     X  X",
      "XX X X XXXXX X X XX",
      "X    X   X   X    X",
      "X XXXXXX X XXXXXX X",
      "X                 X",
      "XXXXXXXXXXXXXXXXXXX"
  };

  class Block {
    int x, y, width, height;
    Image image;
    final int startX, startY;
    char direction = 'U';
    int velocityX = 0, velocityY = 0;

    Block(Image image, int x, int y, int width, int height) {
      this.image = image;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.startX = x;
      this.startY = y;
    }

    void updateDirection(char newDirection) {
      char prevDirection = this.direction;
      this.direction = newDirection;
      updateVelocity();
      move(velocityX, velocityY);
      // check collision with trees; if collision, revert change
      for (Block tree : trees) {
        if (collision(this, tree)) {
          move(-velocityX, -velocityY);
          this.direction = prevDirection;
          updateVelocity();
          break;
        }
      }
    }

    void updateVelocity() {
      switch(this.direction) {
        case 'U': velocityX = 0; velocityY = -TILE_SIZE / 4; break;
        case 'D': velocityX = 0; velocityY = TILE_SIZE / 4; break;
        case 'L': velocityX = -TILE_SIZE / 4; velocityY = 0; break;
        case 'R': velocityX = TILE_SIZE / 4; velocityY = 0; break;
      }
    }

    void move(int dx, int dy) {
      x += dx;
      y += dy;
    }

    void reset() {
      x = startX;
      y = startY;
    }
  }

  public Usaman() {
    setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    setBackground(new Color(218, 247, 166));
    addKeyListener(this);
    setFocusable(true);
    loadImages();
    loadMap();
    
    // Initialize goblin directions once map is loaded
    for (Block goblin : goblins) {
      char newDir = directions[random.nextInt(4)];
      goblin.updateDirection(newDir);
      goblin.image = (goblin.direction == 'L') ? goblinLeftImage : goblinRightImage;
    }
    
    gameLoop = new Timer(50, this);
    gameLoop.start();
    playMusic(1);
  }

  private void loadImages() {
    treeImage = new ImageIcon(getClass().getResource("/UsamanAssets/tree.png")).getImage();
    goblinRightImage = new ImageIcon(getClass().getResource("/UsamanAssets/Goblin_transparent_right.png")).getImage();
    goblinLeftImage = new ImageIcon(getClass().getResource("/UsamanAssets/Goblin_transparent_left.png")).getImage();
    usagiRightImage = new ImageIcon(getClass().getResource("/UsamanAssets/Usagi1_right.png")).getImage();
    usagiLeftImage = new ImageIcon(getClass().getResource("/UsamanAssets/Usagi1_left.png")).getImage();
    foodImage = new ImageIcon(getClass().getResource("/UsamanAssets/kinoko.png")).getImage();
    gameOverImage = new ImageIcon(getClass().getResource("/UsamanAssets/usagi_in_jail.png")).getImage();
    gameClearImage = new ImageIcon(getClass().getResource("/UsamanAssets/usagi_dance.png")).getImage();
  }

  public void loadMap() {
    trees = new HashSet<>();
    foods = new HashSet<>();
    goblins = new HashSet<>();

    for (int r = 0; r < ROW_COUNT; r++) {
      for (int c = 0; c < COLUMN_COUNT; c++) {
        char tile = tileMap[r].charAt(c);
        int x = c * TILE_SIZE;
        int y = r * TILE_SIZE;
        switch (tile) {
          case 'X':
            trees.add(new Block(treeImage, x, y, 26, 26));
            break;
          case 'g':
            goblins.add(new Block(goblinLeftImage, x, y, TILE_SIZE, TILE_SIZE));
            break;
          case 'U':
            Usagi = new Block(usagiRightImage, x, y, TILE_SIZE, TILE_SIZE);
            break;
          case ' ':
            foods.add(new Block(foodImage, x + 14, y + 14, 14, 14));
            break;
          default:
            break;
        }
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  private void draw(Graphics g) {
    // Draw Usagi, goblins, trees, and foods
    g.drawImage(Usagi.image, Usagi.x, Usagi.y, Usagi.width, Usagi.height, null);
    for (Block b : goblins) {
      g.drawImage(b.image, b.x, b.y, b.width, b.height, null);
    }
    for (Block b : trees) {
      g.drawImage(treeImage, b.x, b.y, b.width, b.height, null);
    }
    for (Block b : foods) {
      g.drawImage(foodImage, b.x, b.y, b.width, b.height, null);
    }
    // Draw HUD
    drawHUD(g);
    // Draw game over / clear scenes if needed.
    if (gameOver) {
      playMusicOnce(2);
      g.drawImage(gameOverImage, 150, 180, null);
      drawShadowText(g, "捕まれた！" + score, 140, 160);
    } else if (gameClear) {
      playMusicOnce(3);
      g.drawImage(gameClearImage, 150, 180, null);
      drawShadowText(g, "逃げた！", 140, 160);
    }
  }

  private void drawHUD(Graphics g) {
    String status = "Lives: " + lives + "  Score: " + score;
    g.setFont(new Font("Meiryo", Font.BOLD, 30));
    drawShadowText(g, status, 10, 35);
  }
  
  private void drawShadowText(Graphics g, String text, int x, int y) {
    // Draw shadow text for a drop shadow effect
    g.setColor(Color.BLACK);
    g.drawString(text, x - 2, y - 2);
    g.drawString(text, x - 1, y - 1);
    g.setColor(Color.WHITE);
    g.drawString(text, x, y);
  }

  public void move() {
    // Move Usagi and then check collision with trees
    Usagi.move(Usagi.velocityX, Usagi.velocityY);
    for (Block tree : trees) {
      if (collision(Usagi, tree)) {
        Usagi.move(-Usagi.velocityX, -Usagi.velocityY);
        break;
      }
    }
    
    // Goblin movement and collision with trees or board edges
    for (Block goblin : goblins) {
      if (collision(goblin, Usagi)) {
        lives--;
        if (lives == 0) {
          gameOver = true;
          return;
        }
        resetPositions();
      }
      if (goblin.y == TILE_SIZE * 9 && (goblin.direction == 'L' || goblin.direction == 'R')) {
        goblin.updateDirection('U');
      }
      goblin.move(goblin.velocityX, goblin.velocityY);
      for (Block tree : trees) {
        if (collision(goblin, tree) || goblin.x <= 0 || goblin.x + goblin.width >= BOARD_WIDTH) {
          goblin.move(-goblin.velocityX, -goblin.velocityY);
          goblin.updateDirection(directions[random.nextInt(4)]);
          break;
        }
      }
    }
    
    // Food collision and score update
    Block eaten = null;
    for (Block food : foods) {
      if (collision(Usagi, food)) {
        eaten = food;
        score += 10;
        if (score >= WIN_CONDITION) {
          gameClear = true;
        }
      }
    }
    if (eaten != null) foods.remove(eaten);
  }

  public boolean collision(Block a, Block b) {
    return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
  }

  public void resetPositions() {
    Usagi.reset();
    Usagi.velocityX = 0;
    Usagi.velocityY = 0;
    for (Block goblin : goblins) {
      goblin.reset();
      goblin.updateDirection(directions[random.nextInt(4)]);
    }
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

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (gameOver || gameClear) {
      gameLoop.stop();
      stopMusic();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (gameOver || gameClear) {
      loadMap();
      resetPositions();
      stopMusic();
      lives = LIVES_INIT;
      score = 0;
      gameOver = false;
      gameClear = false;
      gameLoop.start();
      playMusic(1);
      return;
    }
    
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_UP) {
      Usagi.updateDirection('U');
    } else if (key == KeyEvent.VK_DOWN) {
      Usagi.updateDirection('D');
    } else if (key == KeyEvent.VK_LEFT) {
      Usagi.updateDirection('L');
      Usagi.image = usagiLeftImage;
    } else if (key == KeyEvent.VK_RIGHT) {
      Usagi.updateDirection('R');
      Usagi.image = usagiRightImage;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}
  
  @Override
  public void keyTyped(KeyEvent e) {}
}
