import java.awt.*;

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
}
