import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainCanvas extends JPanel {

    private final int MaxRow = MainSettings.GridY;
    private final int MaxColl = MainSettings.GridX;
    private final int CellSiz = 50;

    GeneratedGrid genGrid = MainModule.genGrid;
    HashMap<Integer, HashMap<Integer, Integer>> DataGrid = genGrid.DataGrid;
    HashMap<Integer, HashMap<Integer, Integer>> GameGrid = genGrid.GameGrid;
    HashMap<Integer, HashMap<Integer, Integer>> FlagSlot = new HashMap<>();
    ArrayList<GridSlot> MinesSlot = genGrid.MineSlot;

    MainSettings mySettings = new MainSettings();

    private final int ResolutionX = mySettings.Resolution.x;
    private final int ResolutionY = mySettings.Resolution.y;

    private final Vector2 CenterPos = new Vector2(
            (ResolutionX/2) - (CellSiz/2),
            (ResolutionY/2) - (CellSiz/2)
    );

    ArrayList<SlotButton> ListButtons = new ArrayList<>();
    HashMap<Integer, HashMap<Integer, Vector2>> GridLayout = new HashMap<>();

    private Random rand = new Random();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the canvas before drawing
        for (SlotButton MyButton : ListButtons) {
            MyButton.draw(g);
        }

        g.setColor(Color.BLACK);
        g.drawLine(100, 200, 300, 400);
    }

    public MainCanvas() {
        //GridLib.Print(DataGrid, mySettings.NumberType, FlagSlot);

        Vector2 StartPos = new Vector2(
                CenterPos.x - (MaxRow * CellSiz) / 2,
                CenterPos.y - (MaxColl * CellSiz) / 2
        );

        int incrID = 1;

        boolean isLight = false;

        for (int rows=1; rows<=MaxRow; rows++) {
            HashMap<Integer, Vector2> GridY = new HashMap<>();
            for (int colls=1; colls<=MaxColl; colls++) {
                int x = StartPos.x + colls * CellSiz;
                int y = StartPos.y + rows * CellSiz;

                GridY.put(colls, new Vector2(x, y));

                SlotButton MyButton = new SlotButton(
                        x,
                        y,
                        CellSiz,
                        new Color(rand.nextInt(1,255), rand.nextInt(1,255), rand.nextInt(1,255)),
                        isLight,
                        new Vector2(rows, colls),
                        genGrid,
                        FlagSlot,
                        mySettings
                );

                MyButton.ID = incrID;

                addMouseListener(MyButton);
                ListButtons.add(MyButton);

                incrID += 1;

                if (isLight) {
                    isLight = false;
                }else {
                    isLight = true;
                }
            }

            if (isLight) {
                isLight = false;
            }else {
                isLight = true;
            }

            GridLayout.put(rows, GridY);
        }

        GridLib.Mine(GameGrid, DataGrid, genGrid.StartSlotX, genGrid.StartSlotZ);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                repaint();
            }
        });

        repaint();
    }

    public static void main(String[] args) {
        MainSettings mySettings = new MainSettings();
        JFrame frame = new JFrame("Minesweeper Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(mySettings.Resolution.x, mySettings.Resolution.y);
        frame.add(new MainCanvas()); // Add the canvas to the frame
        frame.setVisible(true);
    }
}
