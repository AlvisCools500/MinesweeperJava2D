import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class SlotButton implements MouseListener {
    int PosX = 0;
    int PosY = 0;
    int Size = 10;
    int ID = 1;
    boolean Light = false;
    Vector2 PosGrid = new Vector2(0,0);
    Color Col = new Color(255,0,255);

    MainSettings mySettings = new MainSettings();

    private GeneratedGrid genGrid;
    private HashMap<Integer, HashMap<Integer, Integer>> DataGrid;
    private HashMap<Integer, HashMap<Integer, Integer>> GameGrid;
    private HashMap<Integer, HashMap<Integer, Integer>> FlagSlot;

    private Random rand = new Random();

    public SlotButton(int posX, int posY, int size, Color col, boolean light, Vector2 posGrid, GeneratedGrid GENGRID, HashMap<Integer, HashMap<Integer, Integer>> flagSlot) {
        this.PosX = posX;
        this.PosY = posY;
        this.Size = size;
        this.Light = light;
        this.PosGrid = posGrid;

        genGrid = GENGRID;
        DataGrid = genGrid.DataGrid;
        GameGrid = genGrid.GameGrid;
        FlagSlot = flagSlot;
    }

    public void draw(Graphics g) {
        var ResVal = GridLib.GetSlotValue(GameGrid, PosGrid.x, PosGrid.y);

        if (ResVal == -1) {
            Col = mySettings.TileColor.get(2);
        }else if (ResVal == -2) {
            Col = mySettings.TileColor.get(1);
        } else {
            Col = mySettings.TileColor.get(0);
        }
        g.setColor(Col);
        g.fillRect(PosX - (Size/2), PosY - (Size/2), Size, Size);

        if (!Light) {
            g.setColor(new Color(0,0,0, 25));
            g.fillRect(PosX - (Size/2), PosY - (Size/2), Size, Size);
        }

        if (ResVal > 0) {
            MainModule.CenteredText(g, mySettings.WarnColor.get(ResVal), new Font("Arial", Font.BOLD, 40), "" + ResVal, PosX, PosY);
        }else if (ResVal == -2) {
            if (FlagSlot != null) {
                if (GridLib.DoesExists(FlagSlot, PosGrid.x, PosGrid.y)) {
                    Image img = mySettings.IMGAsset.get("Banner");
                    g.drawImage(img, PosX - (Size/2), PosY - (Size/2), Size, Size, null);
                }
            }


        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int MouseX = e.getX();
        int MouseY = e.getY();
        var MouseClick = e.getButton();


        if (MouseX >= PosX - (Size/2) && MouseX <= PosX + (Size/2) && MouseY >= PosY - (Size/2) && MouseY <= PosY + (Size/2)) {
            if (MouseClick == MouseEvent.BUTTON1) {
                if (!GridLib.DoesExists(FlagSlot, PosGrid.x, PosGrid.y)) {
                    GridLib.Mine(GameGrid, DataGrid, PosGrid.x, PosGrid.y);
                }
            }else if (MouseClick == MouseEvent.BUTTON3) {
                System.out.println("Flagging");
                GridLib.Flag(FlagSlot, PosGrid.x, PosGrid.y);
            }

        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}

class Vector2 {
    int x;
    int y;

    public Vector2(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    public void plus(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void substract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
    }
}

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
                        FlagSlot
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
