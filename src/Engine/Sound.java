package Engine;




import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.io.File;




public class Sound
{
  //Add a clip to hold the audio clip
  private Clip clip;




  public Sound(){
  try
  {
      URL url = this.getClass().getClassLoader().getResource("Resources/Echoes-Of-Doom-_Loop_.wav");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Echoes-Of-Doom-_Loop_.wav").getAbsoluteFile());
      clip = AudioSystem.getClip();
      clip.open(audioIn);
      clip.loop(Clip.LOOP_CONTINUOUSLY);
  }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
  {
      e.printStackTrace();
  }
}




  public void play()
  {
      if (clip != null)
      {
          clip.loop(Clip.LOOP_CONTINUOUSLY);
      }
  }
}
