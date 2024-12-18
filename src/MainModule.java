import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.InputStream;

public class MainModule {
    public static GeneratedGrid genGrid = LevelGenerator.Generate(MainSettings.GridX, MainSettings.GridY, MainSettings.MultiplyMines);

    public static void CenteredText(Graphics g, Color col, Font font, String str, int PosX, int PosY) {
        g.setColor(col);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);

        int Width = metrics.stringWidth(str);
        int Height = metrics.getHeight();

        g.drawString(str,
                (PosX - (Width/2)) ,
                (PosY - (Height/2)) + metrics.getAscent()
        );
    }

    public static long getDoubleClock() {
        long TheClock = System.nanoTime();


        return TheClock;
    }

    public static void PlaySound(MainSettings mySettings, String str) {
        if (mySettings.SOUNDAsset.get(str) != null) {
            try (InputStream inputstream = MainModule.class.getResourceAsStream(mySettings.SOUNDAsset.get(str))) {

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputstream);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.out.println(str + " is not available!");
        }
    }
}
