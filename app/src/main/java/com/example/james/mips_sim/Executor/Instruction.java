package com.example.james.mips_sim.Executor;


/**
 * Packages up both the type of instruction and the actual instruction via an int[].
 * @author Ben
 *
 */
public class Instruction {

    public static final int RTYPE = 1;
    public static final int ITYPE = 2;
    public static final int JTYPE = 3;

    private int type;
    private int[] instruction;

    public Instruction(int type, int[] instruction) {
        this.type = type;
        this.instruction = instruction;
    }

    public int getType() {
        return this.type;
    }

    public int[] getInstruction() {
        return this.instruction;
    }


    // James added this function to get information for the view
    public String getName()
    {
        String com = "";

        switch(type)
        {
            case RTYPE:
                switch(instruction[5]) // switch 'func'
                {
                    case 0:
                        com = "SLL ";
                        break;
                    case 1:
                        com = "SRL ";
                        break;
                    case 24:
                        com = "MULT ";
                        break;
                    case 32:
                        com = "ADD ";
                        break;
                    case 36:
                        com = "AND ";
                        break;
                    case 34:
                        com = "SUB ";
                        break;
                    case 37:
                        com = "OR ";
                        break;
                    default:
                        return "<UNSUPPORTED>";
                }
                break;

            case ITYPE:
                switch (instruction[0])
                {
                    case 4:
                        com = "BEQ ";
                        break;
                    case 8:
                        com = "ADDI ";
                        break;
                    case 35:
                        com = "LW ";
                        break;
                    case 42:
                        com = "SW";
                        break;
                    default:
                        return "<UNSUPPORTED>";
                }
                break;

            case JTYPE:
                return "J ";
        }

        return com + "|" + instruction[1] + "|" + instruction[2] + "|" + instruction[3]+ "|" + instruction[4]+"|"+instruction[5];
    }
}





