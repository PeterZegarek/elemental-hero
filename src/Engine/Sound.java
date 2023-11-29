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
  private static Clip clip1;
  private static Clip clip2;
  private static Clip clip3;
  private static Clip clip4;
  private static Clip clip5;
  private static Clip clip6;
  private static Clip clip7;

  public Sound(int level){
  }

   public static void play(int level)
   {
    if (clip2 != null) {
      clip2.stop();
      clip2.flush();
    }

    if (clip3 != null) {
      clip3.stop();
      clip3.flush();
    }
    if (clip1 != null) {
      clip1.stop();
      clip1.flush();
    }if (clip4 != null) {
      clip4.stop();
      clip4.flush();
    }if (clip5 != null) {
      clip5.stop();
      clip5.flush();
    }if (clip6 != null) {
      clip6.stop();
      clip6.flush();
    }if (clip7 != null) {
      clip7.stop();
      clip7.flush();
    }
    //level 2
    if (level == 2){
  try
  {
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Echoes-Of-Doom-_Loop_.wav").getAbsoluteFile());
      clip2 = AudioSystem.getClip();
      clip2.open(audioIn);
      clip2.loop(Clip.LOOP_CONTINUOUSLY);
  }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
  {
      e.printStackTrace();
  }
}
  //level3
  else if (level == 3)
  {
    try
      {
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Bubblaine_ Underwater - Super Mario Odyssey Soundtrack.wav").getAbsoluteFile());
      clip3 = AudioSystem.getClip();
      clip3.open(audioIn);
      clip3.loop(Clip.LOOP_CONTINUOUSLY);
      }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }
  //level 1
  else if (level == 1)
  {
    try
      {
        //Change the name of the sound file into whatever you want it to be.
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Old RuneScape Soundtrack- Harmony.wav").getAbsoluteFile());
      clip1 = AudioSystem.getClip();
      clip1.open(audioIn);
      clip1.loop(Clip.LOOP_CONTINUOUSLY);
      }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }
    //level 4
    else if (level == 4)
      {
    try
      {
        //Change the name of the sound file into whatever you want it to be.
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/15-Honeycomb-Highway.wav").getAbsoluteFile());
      clip4 = AudioSystem.getClip();
      clip4.open(audioIn);
      clip4.loop(Clip.LOOP_CONTINUOUSLY);
      }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
    {
      e.printStackTrace();
    }

  }
  //level 5
  else if (level == 5)
      {
    try
      {
        //Change the name of the sound file into whatever you want it to be.
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/59. Stadium - Air Glider.wav").getAbsoluteFile());
      clip5 = AudioSystem.getClip();
      clip5.open(audioIn);
      clip5.loop(Clip.LOOP_CONTINUOUSLY);
      }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
    {
      e.printStackTrace();
    }
      }
      //level 6
      else if (level == 6)
      {
    try
      {
        //Change the name of the sound file into whatever you want it to be.
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Final Battle with Bowser  Super Mario Galaxy (2007).wav").getAbsoluteFile());
      clip6 = AudioSystem.getClip();
      clip6.open(audioIn);
      clip6.loop(Clip.LOOP_CONTINUOUSLY);
      }
  catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }
  
  //Main screen
  else if (level == 0)
  {
try
  {
    //Change the name of the sound file into whatever you want it to be.
  AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Menu  Star Fox 64 (1997).wav").getAbsoluteFile());
  clip7 = AudioSystem.getClip();
  clip7.open(audioIn);
  clip7.loop(Clip.LOOP_CONTINUOUSLY);
  }
catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
{
  e.printStackTrace();
}
  }
}
}
