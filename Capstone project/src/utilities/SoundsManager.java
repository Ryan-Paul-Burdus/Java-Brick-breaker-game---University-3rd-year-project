package utilities;


public class SoundsManager {
    public static volatile double musicVolume;
    public static volatile double sfxVolume;

    public void updateMusicVolume(double newVolume) { musicVolume = newVolume; }
    public double getMusicVolume() { return musicVolume; }

    public void updateSfxVolume(double newVolume) { sfxVolume = newVolume; }
    public double getSfxVolume() { return sfxVolume; }

}
