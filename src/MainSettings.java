import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainSettings {
    public static final int GridX = 10;
    public static final int GridY = 10;
    public static final double MultiplyMines = 0.16;

    public static final Vector2 Resolution = new Vector2(800, 800);

    public static ArrayList<Color> TileColor = new ArrayList<>();
    public static ArrayList<Color> WarnColor = new ArrayList<>();
    public static ArrayList<String> NumberType = new ArrayList<>();

    public static HashMap<String, Image> IMGAsset = new HashMap<>();
    private HashMap<String, String> PathAsset = new HashMap<>();

    public MainSettings() {
        // Set Tile Color
        TileColor.add(new Color(255, 230, 115)); // Sand [0]
        TileColor.add(new Color(107, 255, 92)); // Grass [1]
        TileColor.add(new Color(255, 139, 139)); // Mine [2]

        // Warn Color
        WarnColor.add(new Color(255,255,255)); // 0
        WarnColor.add(new Color(0, 128,255)); // 1
        WarnColor.add(new Color(0, 255, 0)); // 2
        WarnColor.add(new Color(255, 0, 0)); // 3
        WarnColor.add(new Color(47, 0, 255)); // 4
        WarnColor.add(new Color(238, 130, 238)); // 5
        WarnColor.add(new Color(255, 0, 127)); // 6
        WarnColor.add(new Color(255, 0, 60)); // 7
        WarnColor.add(new Color(73, 0, 73)); // 8

        // Number Type
        NumberType.add("M"); // Mine [0]
        NumberType.add("="); // Grass [1]
        NumberType.add("*"); // Flag [2]

        //Image Paths
        PathAsset.put("Banner", "/Asset/Images/Banner.png");

        for (var a : PathAsset.entrySet()) {

            Image img;

            try {
                img = ImageIO.read(getClass().getResource(a.getValue()));
            } catch (IOException e) {
                e.printStackTrace();
                img = null;
            }

            if (img != null) {
                IMGAsset.put(a.getKey(), img);
            }
        }

    }
}
