import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class mainModule {
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
