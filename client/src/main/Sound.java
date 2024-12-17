package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundEffectURL = new URL[30];
    URL[] themeURL = new URL[30];

    Clip[] soundtrack = new Clip[10], effect = new Clip[23];


    public float volume = 0.8f;
    public int currentSoundtrackId;
    public boolean soundtrackMute = false, effectMute = false;
    private Clip getClip(String path) {
        System.out.println(path);
        URL url = getClass().getResource(path);
        AudioInputStream audio;

        try {
            assert url != null;
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Sound() {
        soundtrack[0] = getClip("/sound/Soundtrack/Snowland.wav");
        soundtrack[1] = getClip("/sound/Theme/Demon.wav");
        soundtrack[2] = getClip("/sound/Theme/BringerOfDeath.wav");
        soundtrack[3] = getClip("/sound/Theme/Samurai.wav");
        soundtrack[4] = getClip("/sound/Theme/level1.wav");
        soundtrack[5] = getClip("/sound/Theme/level2.wav");
        soundtrack[6] = getClip("/sound/Theme/level3.wav");
        soundtrack[7] = getClip("/sound/Theme/level4.wav");
        soundtrack[8] = getClip("/sound/Theme/SkeletonReaper.wav");
        soundtrack[9] = getClip("/sound/NextLevel/Idle.wav");

        effect[0] = getClip("/sound/Demon/Explosion.wav");
        effect[1] = getClip("/sound/Demon/FireBreath.wav");
        effect[2] = getClip("/sound/Demon/Slash.wav");
        effect[3] = getClip("/sound/Demon/Transform.wav");

        effect[4] = getClip("/sound/Player/GunAttack.wav");
        effect[5] = getClip("/sound/Player/SpearAttack.wav");

        effect[6] = getClip("/sound/Samurai/attack1.wav");
        effect[7] = getClip("/sound/Samurai/attack2.wav");
        effect[15] = getClip("/sound/Samurai/transform.wav");

        effect[8] = getClip("/sound/SkeletonReaper/NormalAttack.wav");
        effect[9] = getClip("/sound/SkeletonReaper/ElectricBurst.wav");

        effect[10] = getClip("/sound/mage/laser.wav");
        effect[11] = getClip("/sound/mage/MagicCircle.wav");

        effect[12] = getClip("/sound/morph/poison.wav");
        effect[13] = getClip("/sound/morph/Spike.wav");

        effect[14] = getClip("/sound/plantMelee/attack1.wav");

        effect[16] = getClip("/sound/Sickle/attack.wav");

        effect[17] = getClip("/sound/Slime/attack.wav");

        effect[18] = getClip("/sound/SwordKnight/attack1.wav");
        effect[19] = getClip("/sound/SwordKnight/dash.wav");

        effect[20] = getClip("/sound/NextLevel/Disappear.wav");
        effect[21] = getClip("/sound/NextLevel/Idle.wav");
        effect[22] = getClip("/sound/NextLevel/Rumble.wav");

    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSoundtrackVolume();
        updateEffectsVolume();
    }

    private void updateSoundtrackVolume() {
        FloatControl gainControl = (FloatControl) soundtrack[currentSoundtrackId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }
    private void updateEffectsVolume() {
        for (Clip c : effect) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
    public void toggleSongMute() {
        this.soundtrackMute = !soundtrackMute;
        for (Clip c : soundtrack) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(soundtrackMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effect) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
    }

    public void stopSong() {
        if (soundtrack[currentSoundtrackId].isActive())
            soundtrack[currentSoundtrackId].stop();
    }
    public void playSE(int effectId) {
        effect[effectId].setMicrosecondPosition(0);
        effect[effectId].start();
    }

    public void playMusic(int soundtrackId) {
        stopSong();
        currentSoundtrackId = soundtrackId;
        updateSoundtrackVolume();
        soundtrack[soundtrackId].setMicrosecondPosition(0);
        soundtrack[soundtrackId].loop(Clip.LOOP_CONTINUOUSLY);
    }
}
