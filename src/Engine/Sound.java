package Engine;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.File;

public class Sound {
    private static Clip music;
    private static Clip snd;
    private static final File elec_sfx = new File("Resources/electric-shock.wav").getAbsoluteFile();
    private static final File fire_sfx = new File("Resources/fire_throw.wav").getAbsoluteFile();
    private static final File hurt_sfx = new File("Resources/hurt.wav").getAbsoluteFile();
    private static final File rock_sfx = new File("Resources/rock_throw.wav").getAbsoluteFile();
    private static final File water_sfx = new File("Resources/water-splash.wav").getAbsoluteFile();
    // for playing music
    public static void play(int level) {
        if (music != null) {  music.stop();   music.flush();  }
        try {
            music = AudioSystem.getClip();
            if (level == 0) // title screen
                music.open(AudioSystem.getAudioInputStream(new File("Resources/Menu  Star Fox 64 (1997).wav").getAbsoluteFile()));
            else if (level == 1)
                music.open(AudioSystem.getAudioInputStream(new File("Resources/Old RuneScape Soundtrack- Harmony.wav").getAbsoluteFile()));
            else if (level == 2)
                music.open(AudioSystem.getAudioInputStream(new File("Resources/Echoes-Of-Doom-_Loop_.wav").getAbsoluteFile()));
            else if (level == 3)
                music.open(AudioSystem.getAudioInputStream(new File("Resources/Bubblaine_ Underwater - Super Mario Odyssey Soundtrack.wav").getAbsoluteFile()));
            else if (level == 4)
                music.open(AudioSystem.getAudioInputStream(new File("Resources/15-Honeycomb-Highway.wav").getAbsoluteFile()));
            else if (level == 5)
                music.open(AudioSystem.getAudioInputStream(new File("Resources/59. Stadium - Air Glider.wav").getAbsoluteFile()));
            else if (level == 6)
                music.open(AudioSystem.getAudioInputStream(new File("Resources/Final Battle with Bowser  Super Mario Galaxy (2007).wav").getAbsoluteFile()));
            else if (level == -1) // GAME OVER
                music.open(AudioSystem.getAudioInputStream(new File("Resources/SA1_GAMEOVER.wav").getAbsoluteFile()));
            music.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    // for playing sounds
    public static void startSFX(int sfx) {
        try {
            if (snd != null) {  snd.stop(); snd.flush();  }
            snd = AudioSystem.getClip();
            if (sfx == 0)
                snd.open(AudioSystem.getAudioInputStream(hurt_sfx));
            else if (sfx == 1)
                snd.open(AudioSystem.getAudioInputStream(fire_sfx));
            else if (sfx == 2)
                snd.open(AudioSystem.getAudioInputStream(water_sfx));
            else if (sfx == 3)
                snd.open(AudioSystem.getAudioInputStream(elec_sfx));
            else if (sfx == 5)
                snd.open(AudioSystem.getAudioInputStream(rock_sfx));
            snd.loop(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}