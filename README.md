# The Adventure of Usagi

**The Adventure of Usagi** is a Java-based game that combines classic arcade-style gameplay with creative visual storytelling. This project offers a experience through different modes, engaging art assets, and animations—all built using standard Java technologies.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Special Thanks](#special-thanks)

## Tech Stack

- **Java SE:**  
  The core language used for the project.

- **Swing & AWT:**  
  For rendering the user interface (menus, panels) and game graphics.

  - _Swing_ is used for creating custom panels, dialogs, and handling user input.
  - _AWT_ is leveraged for low-level drawing and event processing.

- **Java Timer:**  
  Implements the game loop to update and render game components at a consistent frame rate.

- **Image Handling:**  
  Uses `ImageIcon` and `ImageIO` for loading and managing game assets.

- **Sound Handling:**
  Uses `Sound` for loading and manage sound assets.

## Code Structure and Practices

The project is built with object-oriented principles and emphasizes modularity and easy maintainability:

- **Modularization:**  
  Code is neatly divided into various classes such as `FlappyPurr.java` for the mini-game logic, `GameMenu.java` for setting up the menu interface, and game asset management classes. Each class handles a specific function.

- **Separation of Concerns:**

  - Graphics routines and rendering are managed in dedicated methods (e.g., `drawHUD(Graphics g)`) to keep code organized.
  - Game logic (like dynamically placing obstacles in `placePipes()`) is handled separately from the graphics rendering.

- **Efficient Resource Management:**
  - Images and assets are loaded once during initialization (`loadImages()`), which minimizes performance overhead.

## Getting Started

### Prerequisites

- Java SE (JDK 8 or later)
- An IDE such as Eclipse, IntelliJ IDEA, or NetBeans for development

### Running the Game

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/TheAdventureOfUsagi.git
   ```
2. Navigate to the project directory and open it in your favorite IDE.
3. Run the main class:
   ```java
   public class App {
       public static void main(String[] args) throws Exception {
           SwingUtilities.invokeLater(() -> {
               GameMenu menu = new GameMenu("TheAdvantureofUsagi/src/background2.png");
               menu.setVisible(true);
           });
       }
   }
   ```

## Special Thanks

Special thanks to:

### Game Logics:

- **Kenny Yip Coding Java Game Tutorials:** https://www.youtube.com/@KennyYipCoding
- **How to Make a 2D Game in Java:** https://youtube.com/playlist?list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&si=R8a0TJhGogMnJQ_p

### Music:

- **パジャマパーティーズのうた (8bit arrange):** https://youtu.be/55C-gKYCbq8?si=ckGyL0BwqHZgLZy-
- **ゲームボーイ版　ちいかわ　タイトル画面【チップチューンアレンジ「ひとりごつ」】:** https://youtu.be/mCBSjVvnYK4?si=jvlNvfYxQyGMIkG7

- Additional online resources and blog posts that helped shape the architecture and design choices for this project.
- Also: Claude AI
