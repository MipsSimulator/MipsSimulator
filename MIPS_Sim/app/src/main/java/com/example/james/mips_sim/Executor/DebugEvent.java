package com.example.james.mips_sim.Executor;

import java.util.Queue;

public class DebugEvent {

    /**
     * The contents of the register file.
     */
    private int[] registerFile;

    /**
     * The addresses of the last 10 accessed memory locations.
     */
    private Queue<Integer> memAddresses;

    /**
     * The values of the last 10 accessed memory locations.
     */
    private Queue<Long> memValues;

    /**
     * Instructions in their human read-able format.
     */
    private Queue<String> instructions;

    /**
     * Pc passed in from DebugEvent to update the views properly.
     */
    private int pc;

    public DebugEvent(int[] registerFile, Queue<Long> memValues, Queue<Integer> memAddresses, Queue<String>  instructions,
        int pc){

        this.registerFile = registerFile;
        this.memAddresses = memAddresses;
        this.memValues = memValues;
        this.instructions = instructions;
        this.pc = pc;
    }


    /**
     * Gets the state of the registers. (At the breakpoint)
     * @return the register file
     */
    public int[] getRegisterState() { return registerFile;}

    /**
     * Gets the addresses of the last 10 accessed memory locations.
     * @return addresses of the last 10 accessed memory locations.
     */
    public Queue<Integer> getMemAddresses(){return memAddresses;}


    /**
     * The values of the last 10 accessed memory locations.
     * @return The values of the last 10 accessed memory locations.
     */
    public Queue<Long> getMemValues(){return memValues;}

    public Queue<String>
    getRecentInstructions(){return instructions;}

    /**
     * Gets the updated pc
     * @return
     */
    public int getPc(){
        return this.pc;
    }
}