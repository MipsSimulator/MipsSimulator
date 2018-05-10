package com.example.james.mips_sim.Model;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Model in the MVC architecture
 * The Main Memory Model holds the state of the Assembly Simulator
 */
public class MainMemoryModel extends AbstractModel {

    /**
     * Human Read-able instructions
     */
    private String[] instructions;

    /**
     * Last 3 instructions
     */
    private Queue<String> recentInstructions;

    /**
     * The single instance of the MemoryModel
     */
    private static MainMemoryModel memoryModel;


    /**
     * The addresses of the last 10 accessed memory locations.
     */
    private Queue<Integer> memAddresses;

    /**
     * The values of the last 10 accessed memory locations.
     */
    private Queue<Long> memValues;

    /**
     * The main memory. Access is allowed only at every word, which is 32 bits.
     * This is why the data type is a byte.
     */
    private long[] mainMemory;

    /**
     * Addressable size of Main Memory. Main memory is 8mb by default.
     * Addressing is only allowed to every word, which is 4 bytes.
     * 1024Bytes*1024 = 1Mb*8 = 8mb
     */
    public static final int MEMORY_SIZE = 1024*1024*8;

    /**
     * Top address of the Static Segment. 5.5Mb, or 1024*1024*5.5 = 5767168
     */
    public static final int STATIC_TOP_ADDRESS = 5767168;

    /**
     * Size of the static segment is 1mb. 5767168 - 1mb = 4718592
     * or 5767168 - 1024*1024 = 4718592
     */
    public static final int STATIC_BOTTOM_ADDRESS = 4718592;

    /**
     * Size of the text segment is 4mb. 4718592 - 1024*1024*4 or 4mb = 524288
     */
    public static final int TEXT_BOTTOM_ADDRESS = 524288;

    /**
     * Number of instructions to track at a time for the debugger. (Memory access.. lw/sw)
     */
    public static final int INSTRUCTIONS_TO_TRACK = 10;

    /**
     * When the program gets built, this will be set to the address of the last instruction in memory.
     */
    private int lastInstruction;


    /**
     * Initializes the memory model. Sets the stack pointer, frame pointer, and
     * global pointer to their respective locations in memory.
     */
    private MainMemoryModel() {

        mainMemory = new long[MEMORY_SIZE];

        memAddresses = new LinkedList<>();
        memValues = new LinkedList<>();

        recentInstructions = new LinkedList<>();

    }


    /**
     * Creates an instance of MemoryModel if one does not exist. If one
     * does it exist, that instance is returned. This method implements the
     * Singleton Design Pattern functionality. (Only 1 instance of the Memory Model is allowed
     * to exist at a time)
     * @return
     */
    public static MainMemoryModel getInstance(){
        if(memoryModel == null){
            memoryModel = new MainMemoryModel();
            return memoryModel;
        }

        return memoryModel;
    }




    /**
     * Store the address of the last location in memory. This occurs when the program gets built.
     * @param memoryAddress
     */
    public void setLastInstrAddress(int memoryAddress){ this.lastInstruction = memoryAddress;}


    /**
     * Gets the address of the last instruction in memory.
     * @return - last instruction in memory.
     */
    public int getLastInstrAddress() { return this.lastInstruction;}





    /**
     * [Read Only] Used to read main memory at the given memory address.
     * This can be used as load word.
     * @param memoryAddress
     * @return data at that memory address in the form of an int.
     */
    public long loadMemory(int memoryAddress) {

        if(memAddresses.size() == 10) {
            memAddresses.poll();
            memValues.poll();
        }

        memAddresses.add(memoryAddress);
        memValues.add(mainMemory[memoryAddress]);

        return mainMemory[memoryAddress];

    }

    /**
     * [Read Only] Used to fetch instructions in main memory at the given memory address.
     * This is only used to fetch instructions. It will not record memory changes for the
     * debugger.
     * @param memoryAddress
     * @return data at that memory address in the form of an int.
     */
    public long loadInstruction(int memoryAddress) { return mainMemory[memoryAddress]; }



    /**
     * [Write Only] used to write instructions to memory at the given address. These operations
     * do not appear in the debugger.
     * @param memoryAddress - 32bit word addressable
     * @param value - value to be stored at the given memoryAddress
     */
    public void storeInstruction(int memoryAddress, long value) { mainMemory[memoryAddress] = value;  }


    /**
     * [Write Only] used to write to memory at the given address.
     * This can be used as store word.
     * @param memoryAddress - 32bit word addressable
     * @param value - value to be stored at the given memoryAddress
     */
    public void storeMemory(int memoryAddress, long value) {

        if(memAddresses.size() == 10) {
            memAddresses.poll();
            memValues.poll();
        }

        // The actual store operation
        mainMemory[memoryAddress] = value;

        memAddresses.add(memoryAddress);
        memValues.add(mainMemory[memoryAddress]);

        System.out.println("memoryAddress" + memoryAddress);
        System.out.println("memoryAddress" + value);

    }


    public Queue<Integer> getRecentAddresses() {
        return memAddresses;
    }

    public Queue<Long> getRecentValues(){
        return memValues;
    }

    public void setInstructions(String[] instructions){
        this.instructions = instructions;

        recentInstructions = new LinkedList<String>();
        for(int i = 0; i<3; i++){
            recentInstructions.add(instructions[i]);
        }



    }

    public String[] getInstructions(){return this.instructions;}

    public Queue<String> getRecentInstructions() { return recentInstructions; }

    public void enqueueInstruction(int instructionIndex){

        if(recentInstructions.size() >= 3){
            recentInstructions.poll();
        }

        if(instructions.length > instructionIndex+2){
            recentInstructions.add(instructions[instructionIndex+2]);
        }
        else
        {
            recentInstructions.add("Nop");
        }
    }



}