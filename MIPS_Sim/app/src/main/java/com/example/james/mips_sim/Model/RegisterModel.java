package com.example.james.mips_sim.Model;

/**
 * Models MIP's 32 registers. Each register is 32 bits in width.
 * @author Ben
 *
 */
public class RegisterModel extends AbstractModel {



    /**
     * The single RegisterModel instance. (Singleton)
     */
    private static RegisterModel registerModel;

    /**
     * int array that is initialized to 32 to hold the MIPS 32 registers.
     */
    private int[] registerFile;

    /**
     * Special register, program counter.
     */
    private int pc;

    /**
     * Initial PC value.
     */
    public static int INITIAL_PC = MainMemoryModel.TEXT_BOTTOM_ADDRESS;

    /**
     * Special purpose register, hi
     */
    private int hi;

    /**
     * Special purpose register, lo
     */
    private int lo;



    private RegisterModel() {
        registerFile = new int[32];
        for(int i = 0; i<32; i++)
            registerFile[i] = 0;

        setSp(MainMemoryModel.MEMORY_SIZE-1);
        setGp(MainMemoryModel.STATIC_TOP_ADDRESS);
        setPc(MainMemoryModel.TEXT_BOTTOM_ADDRESS);
    }


    /**
     * Singleton method that returns the single instance of RegisterModel
     * @return RegisterModel singleton
     */
    public static RegisterModel getInstance() {
        if(registerModel == null) {
            registerModel = new RegisterModel();
        }
        return registerModel;
    }


    /**
     * Gets the value in the register at the specified register File.
     * @param registerIndex
     * @return
     */
    public int getRegister(int registerIndex) {

        return registerFile[registerIndex];
    }

    /**
     * Updates the register at the given index to the registerFile. Updates it with the data
     * that is passed in.
     * @param data
     * @param registerIndex
     */
    public void setRegister(int data, int registerIndex) {
        registerFile[registerIndex] = data;
    }

    /**
     *
     * @return stack pointer
     */
    public int getSp() { return this.registerFile[29];}
    /**
     *
     * @return frame pointer
     */
    public int getFp() { return this.registerFile[30];}
    /**
     *
     * @return global pointer
     */
    public int getGp() { return this.registerFile[28];}
    /**
     *
     * @return program counter
     */
    public int getPc() { return this.pc;}

    /**
     * Gets the hi special purpose register contents
     * @return
     */
    public int getHi() { return this.hi;}


    /**
     * Gets the lo special purpose register contents
     */
    public int getLo() { return this.lo;}



    /**
     * Updates the program counter
     * @param memoryAddress - The address the PC should be set to.
     */
    public void setPc(int memoryAddress) { this.pc = memoryAddress ;}

    /**
     * Updates the frame pointer
     * @param memoryAddress
     */
    public void setFp(int memoryAddress) {this.registerFile[30] = memoryAddress; }


    /**
     * Updates the global pointer
     * @param memoryAddress
     */
    public void setGp(int memoryAddress) {this.registerFile[28] = memoryAddress; }


    /**
     * Updates the stack pointer
     * @param memoryAddress
     */
    public void setSp(int memoryAddress) {this.registerFile[29] = memoryAddress; }

    /**
     * Sets the hi special purpose register
     * @param value
     */
    public void setHi(int value) { this.hi = value;}

    /**
     * Sets the lo special purpose register
     * @param value
     */
    public void setLo(int value) { this.lo = value; }


    public int[] getRegisterFile() { return registerFile;}


}