import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

class DropParticle {
    private Vector2Double CurrPos, SPos, Vpos;
    double timeEnd;
    double Clock;
    double size;
    Color col;
    private double StartSize;
    private double TargSize;

    public DropParticle(Vector2Double startPos, Vector2Double endPos, Color Col, double Size, double timeEnd) {
        this.CurrPos = startPos;
        this.SPos = startPos;
        this.Vpos = endPos;
        this.timeEnd = timeEnd;
        this.Clock = MainModule.getDoubleClock();
        this.size = Size;
        this.TargSize = 0;
        this.StartSize = size;
        this.col = Col;

    }

    public void update(double alpha) {


        Vector2Double AddingTemp = new Vector2Double(
                (Vpos.x - SPos.x) * alpha,
                (Vpos.y - SPos.y) * alpha
        );


        this.CurrPos = new Vector2Double(SPos.x, SPos.y);
        this.CurrPos.plus(AddingTemp);

        this.size = StartSize + (TargSize - StartSize) * alpha;
    }

    public Vector2Double position() {
        Vector2Double ResPos = new Vector2Double(CurrPos.x, CurrPos.y);
        //ResPos.substract(new Vector2Double(this.size/2, this.size/2));

        return ResPos;
    }


}

public class MainCanvas extends JPanel implements Runnable{

    Random rand = new Random();

    ArrayList<SlotButton> ListButtons = new ArrayList<>();
    HashMap<Integer, HashMap<Integer, Vector2Int>> GridLayout = new HashMap<>();
    ArrayList<DropParticle> dropList = new ArrayList<>();

    Thread mainThread;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Main
        for (SlotButton MyButton : ListButtons) {
            MyButton.draw(g);
        }

        // Draw DropParticle
        for (int i=dropList.size() - 1; i>=0; i--) {
            DropParticle v = dropList.get(i);

            g.setColor(v.col);
            g.fillRect((int) v.position().x, (int) v.position().y, (int) v.size, (int) v.size);
        }


    }

    public MainCanvas(GeneratedGrid genGrid, HashMap<Integer, HashMap<Integer, Integer>> FlagSlot, MainSettings mySettings) {
        final int MaxRow = MainSettings.GridY;
        final int MaxColl = MainSettings.GridX;
        final int CellSiz = MainSettings.CellSize;

        final int ResolutionX = mySettings.Resolution.x;
        final int ResolutionY = mySettings.Resolution.y;

        final Vector2Int CenterPos = new Vector2Int(
                (ResolutionX/2) - (CellSiz/2),
                (ResolutionY/2) - (CellSiz/2)
        );

        final Vector2Int StartPos = new Vector2Int(
                CenterPos.x - (MaxRow * CellSiz) / 2,
                CenterPos.y - (MaxColl * CellSiz) / 2
        );

        int incrID = 1;

        boolean isLight = false;

        for (int rows=1; rows<=MaxRow; rows++) {
            HashMap<Integer, Vector2Int> GridY = new HashMap<>();
            for (int colls=1; colls<=MaxColl; colls++) {
                int x = (colls * CellSiz) - CellSiz/2;
                int y = (rows * CellSiz) - CellSiz/2;

                GridY.put(colls, new Vector2Int(x, y));

                SlotButton MyButton = new SlotButton(
                        x,
                        y,
                        CellSiz,
                        new Color(rand.nextInt(1,255), rand.nextInt(1,255), rand.nextInt(1,255)),
                        isLight,
                        new Vector2Int(rows, colls),
                        genGrid,
                        FlagSlot,
                        mySettings,
                        dropList
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

        StartThread();
    }

    public void StartThread() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        while (mainThread != null) {
            double MyClock = MainModule.getDoubleClock();

            // Draw
            repaint();

            // DropParticle Handler


            if (dropList.size() > 0) {

                for (int i=dropList.size() - 1; i>= 0; i--) {
                    DropParticle v = dropList.get(i);

                    double TotalAlpha = Math.clamp(((MyClock - v.Clock) / 1e9) / v.timeEnd, 0, 1);

                    if (TotalAlpha >= 1) {
                        dropList.remove(i);
                    } else {

                        v.update(TotalAlpha);
                    }



                }


            }

        }
    }

    public static void main(String[] args) {
        // Initiate Values
        MainSettings mySettings = new MainSettings();
        mySettings.OpenResource();

        GeneratedGrid genGrid = MainModule.genGrid;
        HashMap<Integer, HashMap<Integer, Integer>> DataGrid = genGrid.DataGrid;
        HashMap<Integer, HashMap<Integer, Integer>> GameGrid = genGrid.GameGrid;
        HashMap<Integer, HashMap<Integer, Integer>> FlagSlot = new HashMap<>();
        ArrayList<GridSlot> MinesSlot = genGrid.MineSlot;

        // Run Frame
        JFrame frame = new JFrame("Minesweeper Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(mySettings.Resolution.x, mySettings.Resolution.y);
        frame.add(new MainCanvas(genGrid, FlagSlot, mySettings)); // Add the canvas to the frame
        frame.setVisible(true);

        GridLib.Mine(GameGrid, DataGrid, genGrid.StartSlotX, genGrid.StartSlotZ);


    }




}
