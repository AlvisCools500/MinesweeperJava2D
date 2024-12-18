import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class SlotButton implements MouseListener {
    int PosX = 0;
    int PosY = 0;
    int Size = 10;
    int ID = 1;
    boolean Light;
    private boolean WasGrass = true;
    Vector2Int PosGrid;
    Color Col = new Color(255,0,255);
    private MainSettings mySettings;

    private GeneratedGrid genGrid;
    private HashMap<Integer, HashMap<Integer, Integer>> DataGrid;
    private HashMap<Integer, HashMap<Integer, Integer>> GameGrid;
    private HashMap<Integer, HashMap<Integer, Integer>> FlagSlot;

    private ArrayList<DropParticle> dropList;

    private Random rand = new Random();

    public SlotButton(int posX, int posY, int size, Color col, boolean light, Vector2Int posGrid, GeneratedGrid GENGRID, HashMap<Integer, HashMap<Integer, Integer>> flagSlot, MainSettings MySettings, ArrayList<DropParticle> dropParticle) {
        this.PosX = posX;
        this.PosY = posY;
        this.Size = size;
        this.Light = light;
        this.PosGrid = posGrid;

        genGrid = GENGRID;
        DataGrid = genGrid.DataGrid;
        GameGrid = genGrid.GameGrid;
        FlagSlot = flagSlot;
        mySettings = MySettings;
        dropList = dropParticle;
    }

    public Vector2Int position() {
        return new Vector2Int(PosX - (Size/2), PosY - (Size/2));
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
        g.fillRect(this.position().x, this.position().y, Size, Size);

        if (!Light) {
            g.setColor(new Color(0,0,0, 25));
            g.fillRect(PosX - (Size/2), PosY - (Size/2), Size, Size);
        }

        if (ResVal > 0) {
            MainModule.CenteredText(g, mySettings.WarnColor.get(ResVal), new Font("Arial", Font.BOLD, 40), "" + ResVal, PosX, PosY);
            if (WasGrass) {
                WasGrass = false;
                Vector2Double StartPos = new Vector2Double(this.position().x, this.position().y);
                Vector2Double EndPos = new Vector2Double(StartPos.x, StartPos.y);

                double range = 50;

                EndPos.plus(new Vector2Double(
                        rand.nextDouble(-Size/2 - range, Size/2 + range),
                        rand.nextDouble(-Size/2 - range, Size/2 + range)
                ));

                DropParticle FallGrass = new DropParticle(StartPos, EndPos, mySettings.TileColor.get(1), Size, 0.5);

                dropList.add(FallGrass);
            }

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
                    if (GridLib.GetSlotValue(GameGrid, PosGrid.x, PosGrid.y) == -2) {
                        GridLib.Mine(GameGrid, DataGrid, PosGrid.x, PosGrid.y);
                        MainModule.PlaySound(mySettings, "Dig");

                        var ResVal = GridLib.GetSlotValue(GameGrid, PosGrid.x, PosGrid.y);

                        if (ResVal > 0) {
                            MainModule.PlaySound(mySettings, "Danger" + Math.clamp(ResVal, 1, 4));
                        }
                    }

                }
            }else if (MouseClick == MouseEvent.BUTTON3) {
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

