import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class BackgroundPanel extends JPanel {
  private Image backgroundImage;

  public BackgroundPanel(String imagePath) {
    setLayout(new GridLayout(3, 1, 10, 20));
    setBorder(new EmptyBorder(20, 40, 20, 40));
    setOpaque(false);
    try {
      backgroundImage = ImageIO.read(new File(imagePath));
    } catch (Exception e) {
      System.err.println("Error loading bg image: " + e.getMessage());
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
      g.setColor(new Color(0, 0, 0, 0));
      g.fillRect(0, 0, getWidth(), getHeight());
    }
  }
}

public class GameMenu extends JFrame {
  private static final int MENU_WIDTH = 608;
  private static final int MENU_HEIGHT = 672;
  private Sound sound = new Sound();

  public GameMenu(String backgroundImagePath) {
    setTitle("The Adventure of Usagi");
    setSize(MENU_WIDTH, MENU_HEIGHT);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    playMusic(0);

    BackgroundPanel mainPanel = new BackgroundPanel(backgroundImagePath);

    JLabel titleLabel = new JLabel("うさぎの冒険", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Meiryo", Font.BOLD, 48));

    JButton flappyPurrBtn = createGameButton("運ばれる?");
    JButton usamanBtn = createGameButton("逃げよう");

    flappyPurrBtn.addActionListener(e -> launchGame("The Adventure of Usagi - Flappy Purr", new FlappyPurr()));
    usamanBtn.addActionListener(e -> launchGame("The Adventure of Usagi - Usaman", new Usaman()));

    mainPanel.add(titleLabel);
    mainPanel.add(flappyPurrBtn);
    mainPanel.add(usamanBtn);
    add(mainPanel);
    setVisible(true);
  }

  private JButton createGameButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Meiryo", Font.BOLD, 28));
    button.setFocusPainted(false);
    button.setBackground(new Color(255, 238, 140));
    button.setForeground(new Color(50, 50, 50));
    return button;
  }

  private void launchGame(String title, JComponent gameComponent) {
    JFrame gameFrame = new JFrame(title);
    gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gameFrame.setResizable(false);
    gameFrame.add(gameComponent);
    gameFrame.pack();
    gameFrame.setLocationRelativeTo(null);
    gameComponent.requestFocus();
    gameFrame.setVisible(true);

    stopMusic();
    setVisible(false);

    gameFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (gameComponent instanceof FlappyPurr) {
          ((FlappyPurr) gameComponent).stopMusic();
        } else if (gameComponent instanceof Usaman) {
          ((Usaman) gameComponent).stopMusic();
        }
      }

      @Override
      public void windowClosed(WindowEvent e) {
        playMusic(0);
        setVisible(true);
      }
    });
  }

  public void playMusic(int i) {
    sound.setFile(i);
    sound.play();
    sound.loop();
  }

  public void stopMusic() {
    sound.stop();
  }
}
