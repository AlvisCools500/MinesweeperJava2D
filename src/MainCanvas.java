import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class RectButton implements MouseListener {
    int PosX = 0;
    int PosY = 0;
    int Size = 10;
    int ID = 1;
    Color Col;

    private Random rand = new Random();

    public RectButton(int posX, int posY, int size, Color col) {
        this.PosX = posX;
        this.PosY = posY;
        this.Size = size;
        this.Col = col;
    }

    public void draw(Graphics g) {
        g.setColor(Col);
        g.fillRect(PosX - (Size/2), PosY - (Size/2), Size, Size);

        //g.setColor(Color.black);
        //g.setFont(new Font("Arial", Font.BOLD, 20));
        //g.drawString("" + ID, PosX, PosY);

        mainModule.CenteredText(g, Color.black, new Font("Arial", Font.BOLD, 40), "" + ID, PosX, PosY);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int MouseX = e.getX();
        int MouseY = e.getY();
        var MouseClick = e.getButton();

        if (MouseClick == MouseEvent.BUTTON1) {
            if (MouseX >= PosX - (Size/2) && MouseX <= PosX + (Size/2) && MouseY >= PosY - (Size/2) && MouseY <= PosY + (Size/2)) {
                Col = new Color(rand.nextInt(1,255), rand.nextInt(1,255), rand.nextInt(1,255));
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

class VectorGrid {
    int x;
    int y;
    int width;
    int height;

    public VectorGrid(int X, int Y, int Width, int Height) {
        this.x = X;
        this.y = Y;
        this.width = Width;
        this.height = Height;
    }
}

public class MainCanvas extends JPanel {
    private final int ResolutionX = 800;
    private final int ResolutionY = 800;

    private final int MaxRow = 10;
    private final int MaxColl = 10;
    private final int CellSiz = 50;

    private final Vector2 CenterPos = new Vector2(
            (ResolutionX/2) - (CellSiz/2),
            (ResolutionY/2) - (CellSiz/2)
    );

    ArrayList<RectButton> ListButtons = new ArrayList<>();
    HashMap<Integer, HashMap<Integer, Vector2>> GridLayout = new HashMap<>();

    private Random rand = new Random();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the canvas before drawing
        for (RectButton MyButton : ListButtons) {
            MyButton.draw(g);
        }
    }

    public MainCanvas() {

        Vector2 StartPos = new Vector2(
                CenterPos.x - (MaxRow * CellSiz) / 2,
                CenterPos.y - (MaxColl * CellSiz) / 2
        );

        int incrID = 1;

        for (int rows=1; rows<=MaxRow; rows++) {
            HashMap<Integer, Vector2> GridY = new HashMap<>();
            for (int colls=1; colls<=MaxColl; colls++) {
                int x = StartPos.x + colls * CellSiz;
                int y = StartPos.y + rows * CellSiz;

                GridY.put(colls, new Vector2(x, y));

                RectButton MyButton = new RectButton(
                        x,
                        y,
                        CellSiz,
                        new Color(rand.nextInt(1,255), rand.nextInt(1,255), rand.nextInt(1,255))
                );

                MyButton.ID = incrID;

                addMouseListener(MyButton);
                ListButtons.add(MyButton);

                incrID += 1;
            }
            GridLayout.put(rows, GridY);
        }

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
        JFrame frame = new JFrame("Basic Canvas Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new MainCanvas()); // Add the canvas to the frame
        frame.setVisible(true);
    }
}
