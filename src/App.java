import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(() -> {
            GameMenu menu = new GameMenu("src\\background2.png");
            menu.setVisible(true);
        });
    }
}
