import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class GridSlot {
    int X;
    int Z;
    int V;
}

class GeneratedGrid {
    HashMap<Integer, HashMap<Integer, Integer>> DataGrid;
    HashMap<Integer, HashMap<Integer, Integer>> GameGrid;
    ArrayList<GridSlot> MineSlot;
    int StartSlotX;
    int StartSlotZ;
}

public class LevelGenerator {


    public static GeneratedGrid Generate(int TotalX, int TotalZ, double MultiplyMines){
        HashMap<Integer, HashMap<Integer, Integer>> MyGrid = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> GameGrid = new HashMap<>();
        ArrayList<GridSlot> MinesSlot = new ArrayList<>();
        Random rand = new Random();

        //Total Values
        int TotalMines = Math.toIntExact(Math.round((TotalX * TotalZ) * MultiplyMines));

        // Creating the Grid Hash
        for (int GX=1; GX<=TotalX; GX++) {
            HashMap<Integer, Integer> HashX = new HashMap<>();

            for (int GZ=1; GZ<=TotalZ; GZ++) {
                HashX.put(GZ, 0);
            }

            MyGrid.put(GX, HashX);
        }

        // Set GameGrid
        for (int GX=1; GX<=TotalX; GX++) {
            HashMap<Integer, Integer> HashX = new HashMap<>();

            for (int GZ=1; GZ<=TotalZ; GZ++) {
                HashX.put(GZ, -2);
            }

            GameGrid.put(GX, HashX);
        }

        // Add Mines to randomly position
        for (int i=1; i<=TotalMines; i++) {
            for (int t=1; t<=10; t++) {
                int x = rand.nextInt(1, TotalX);
                int z = rand.nextInt(1, TotalZ);

                HashMap<Integer, Integer> HashX = MyGrid.get(x);

                if (HashX.get(z) == 0) {
                    HashX.put(z, -1);

                    GridSlot MySlot = new GridSlot();

                    MySlot.X = x;
                    MySlot.Z = z;
                    MySlot.V = -1;

                    MinesSlot.add((MinesSlot.size() - 1) + 1, MySlot);

                    break;
                }
            }
        }


        // Add Warning Num
        for (GridSlot MySlot : MinesSlot) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {

                    int GX = MySlot.X + x;
                    int GZ = MySlot.Z + z;

                    if (MyGrid.get(GX) != null) {
                        if (MyGrid.get(GX).get(GZ) != null) {
                            if (MyGrid.get(GX).get(GZ) != -1) {
                                HashMap<Integer, Integer> MyHash = MyGrid.get(GX);

                                MyHash.put(GZ, MyHash.get(GZ) + 1);

                            }
                        }
                    }
                }
            }
        }

        int incrX = 0;
        int incrZ = 0;

        // Set the Start Value
        for (var x : MyGrid.entrySet()) {
            incrZ = 0;
            incrX += 1;

            boolean fond = false;

            for (var z : x.getValue().entrySet()) {
                incrZ += 1;

                if (z.getValue() == 0) {
                    break;
                }
            }

            if (fond) {
                break;
            }
        }

        GeneratedGrid ResultGrid = new GeneratedGrid();
        ResultGrid.DataGrid = MyGrid;
        ResultGrid.GameGrid = GameGrid;
        ResultGrid.MineSlot = MinesSlot;
        ResultGrid.StartSlotX = incrX;
        ResultGrid.StartSlotZ = incrZ;

        return ResultGrid;
    }
}
