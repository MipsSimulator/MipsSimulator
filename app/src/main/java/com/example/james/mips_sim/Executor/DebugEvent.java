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

    public DebugEvent(int[] registerFile, Queue<Long> memValues, Queue<Integer> memAddresses){

        this.registerFile = registerFile;
        this.memAddresses = memAddresses;
        this.memValues = memValues;
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
}