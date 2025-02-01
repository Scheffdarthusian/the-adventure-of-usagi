import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
  private Clip clip;
  private URL[] soundURL;

  public Sound() {
    soundURL = new URL[30];
    try {
      // Use forward slashes for resource paths
      soundURL[0] = getClass().getResource("/sound/background.wav");
      soundURL[1] = getClass().getResource("/sound/pajama_party_8bit.wav");
      soundURL[2] = getClass().getResource("/sound/usagi_ha.wav");
      soundURL[3] = getClass().getResource("/sound/usagi_uta.wav");
      if (soundURL[0] == null) {
        System.err.println("Warning: Could not find sound file");
      }
    } catch (Exception e) {
      System.err.println("Error loading sound resources: " + e.getMessage());
    }
  }

  public void setFile(int i) {
    try {
      if (soundURL[i] == null) {
        System.err.println("Sound file not found at index " + i);
        return;
      }
      AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
      clip = AudioSystem.getClip();
      clip.open(ais);
    } catch (Exception e) {
      System.err.println("Error setting up audio: " + e.getMessage());
    }
  }

  public void play() {
    try {
      if (clip != null) {
        clip.start();
      }
    } catch (Exception e) {
      System.err.println("Error playing audio: " + e.getMessage());
    }
  }

  public void loop() {
    try {
      if (clip != null) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
      }
    } catch (Exception e) {
      System.err.println("Error looping audio: " + e.getMessage());
    }
  }

  public void stop() {
    try {
      if (clip != null && clip.isRunning()) {
        clip.stop();
      }
    } catch (Exception e) {
      System.err.println("Error stopping audio: " + e.getMessage());
    }
  }
}