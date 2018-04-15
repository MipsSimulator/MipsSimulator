package com.example.ben.mipssimulator.Model;

public class MemoryModel {

    /**
     * The single instance of the MemoryModel
     */
    private MemoryModel memoryModel;

    /**
     * Addressable size of Main Memory. Main memory is 8mb by default.
     * Addressing is only allowed to every word, which is 4 bytes.
     * Note: The bytes are still there, but the memory is only accessible
     * in chunks of 4 bytes.
     */
    private static final int MEMORY_SIZE = 1024*1024*8;

    /**
     * Top address of the Static Segment. 5.5Mb, or 1024*1024*5.5 = 5767168
     */
    private static final int STATIC_TOP_ADDRESS = 5767168;

    /**
     * Size of the static segment is 1mb. 5767168 - 1mb = 4718592
     * or 5767168 - 1024*1024 = 4718592
     */
    private static final int STATIC_BOTTOM_ADDRESS = 4718592;

    /**
     * Size of the text segment is 4mb. 4718592 - 1024*1024*4 or 4mb = 524288
     */
    private static final int TEXT_BOTTOM_ADDRESS = 524288;

    /**
     * Stack Pointer. Holds index (address) to the stack.
     */
    private int sp;
    /**
     * Frame Pointer Holds index (address) to the frame.
     */
    private int fp;

    /**
     * Global pointer that points to the top of the static data segment for easy access
     * to that segment.
     */
    private int gp;

    /**
     * Program counter. Address is initially set to the bottom of the text segment. Keeps track
     * of the address to the current instruction in the Main Memory.
     */
    private int pc;

    /**
     * The main memory. Access is allowed only at every word, which is 32 bits.
     * This is why the data type is an int.
     */
    private byte[] mainMemory;


    /**
     * Initializes the memory model. Sets the stack pointer, frame pointer, and
     * global pointer to their respective locations in memory.
     */
    private MemoryModel() {

        memoryModel = new MemoryModel();
        mainMemory = new byte[MEMORY_SIZE];

        this.sp = MEMORY_SIZE-1;
        this.gp = STATIC_TOP_ADDRESS;
        this.pc = TEXT_BOTTOM_ADDRESS;

    }


    /**
     * Creates an instance of MemoryModel if one does not exist. If one
     * does it exist, that instance is returned. This method implements the
     * Singleton Design Pattern functionality. (Only 1 instance of the Memory Model is allowed
     * to exist at a time)
     * @return
     */
    public MemoryModel getInstance(){
        if(memoryModel == null){
            memoryModel = new MemoryModel();
            return memoryModel;
        }

        return memoryModel;
    }



}
