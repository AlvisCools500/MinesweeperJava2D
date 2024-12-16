import java.util.ArrayList;
import java.util.HashMap;

public class GridLib {
    // Make the print easier to look at it
    public static void Print(HashMap<Integer, HashMap<Integer, Integer>> Hash, ArrayList<String> NumberType, HashMap<Integer, HashMap<Integer, Integer>> FlagSlot) {
        int IncrX = 1;
        int IncrZ = 1;

        String UseStr = "";

        for (int i = 1; i <= Hash.get(1).size(); i++) {
            UseStr += i + " ";
        }

        System.out.println(UseStr);

        UseStr = "";

        for (int i = 1; i <= Hash.get(1).size(); i++) {
            UseStr += "- ";
        }

        System.out.println(UseStr);

        for (var a : Hash.entrySet()) {
           // ArrayList<Object> MyList = new ArrayList<>();

            String str = "|" + IncrX;
            String NumList = "";

            for (var b : a.getValue().entrySet()) {
                var val = b.getValue();

                if (val == -1) {
                    NumList = NumList + NumberType.get(0);
                }else if (val == -2) {
                    if (DoesExists(FlagSlot, IncrX, IncrZ)) {
                        NumList = NumList + NumberType.get(2);
                    }else {
                        NumList = NumList + NumberType.get(1);
                    }

                } else {
                    NumList = NumList + val;
                }

                NumList = NumList + " ";

                IncrZ += 1;
            }

            System.out.println(NumList + str);

            IncrZ = 1;
            IncrX += 1;
        }
    }

    // Check if the slot is exists
    public static boolean DoesExists(HashMap<Integer, HashMap<Integer, Integer>> Grid, int PosX, int PosZ) {

        if (Grid.get(PosX) != null) {
            if (Grid.get(PosX).get(PosZ) != null) {
                return true;
            }
        }

        return false;
    }

    // Get the slot value, -99 means null
    public static int GetSlotValue(HashMap<Integer, HashMap<Integer, Integer>> Grid, int PosX, int PosZ) {
        int ResNum = -99;

        if (DoesExists(Grid, PosX, PosZ)) {
            ResNum = Grid.get(PosX).get(PosZ);
        }

        return ResNum;
    }

    public static void Flag(HashMap<Integer, HashMap<Integer, Integer>> FlagSlot, int PosX, int PosZ) {
        boolean fond = false;

        if (FlagSlot.get(PosX) != null) {
            if (FlagSlot.get(PosX).get(PosZ) != null) {
                fond = true;
            }
        }

        if (!fond) {
            if (FlagSlot.get(PosX) == null) {
                FlagSlot.put(PosX, new HashMap<Integer, Integer>());
            }

            HashMap<Integer, Integer> HashX = FlagSlot.get(PosX);

            HashX.put(PosZ, 1);

        } else {
            HashMap<Integer, Integer> HashX = FlagSlot.get(PosX);

            HashX.remove(PosZ);
        }
    }

    // Mine the Slot
    public static void Mine(HashMap<Integer, HashMap<Integer, Integer>> GameGrid, HashMap<Integer, HashMap<Integer, Integer>> DataGrid, int PosX, int PosZ) {
        if (DoesExists(GameGrid, PosX, PosZ)) {
            HashMap<Integer, Integer> HashX = GameGrid.get(PosX);

            var value = GridLib.GetSlotValue(DataGrid, PosX, PosZ);

            if (HashX.get(PosZ) == -2) {
                HashX.put(PosZ, DataGrid.get(PosX).get(PosZ));

                if (value == 0) {
                    RecursiveZero(GameGrid, DataGrid, PosX, PosZ);
                }
            }
        }
    }

    // Recursive Mine Empty Mines
    public static void RecursiveZero(HashMap<Integer, HashMap<Integer, Integer>> GameGrid, HashMap<Integer, HashMap<Integer, Integer>> DataGrid, int PosX, int PosZ) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                int GX = PosX + x;
                int GZ = PosZ + z;

                if (DoesExists(GameGrid, GX, GZ)) {
                    HashMap<Integer, Integer> HashX = GameGrid.get(GX);

                    int gmValue = GetSlotValue(GameGrid, GX, GZ);
                    int datValue = GetSlotValue(DataGrid, GX, GZ);

                    if (gmValue == -2) {
                        if (datValue != -1) {
                            HashX.put(GZ, datValue);
                            if (datValue == 0) {
                                RecursiveZero(GameGrid, DataGrid, GX, GZ);
                            }
                        }
                    }
                }
            }
        }
    }
}
