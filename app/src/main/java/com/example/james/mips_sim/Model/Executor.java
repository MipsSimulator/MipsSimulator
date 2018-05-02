package com.example.james.mips_sim.Model;

import com.example.james.mips_sim.CodeParser.CodeParser;
import com.example.james.mips_sim.Executor.Instruction;

import java.util.Queue;

/**
 * Main model Executor contains sub models memory and register
 * Interprets memory addresses and modifies values in registers and memory
 * for instructions
 * @author James
 */
public class Executor {

    private boolean isExecuting;
    private MainMemoryModel memory;
    private RegisterModel register;
    private CodeParser codeParser;
    private static Executor executor;

    private Executor() {
        this.memory = MainMemoryModel.getInstance();
        this.register = RegisterModel.getInstance();
        this.codeParser = CodeParser.getInstance();
    }


    /**
     * Singleton of executor
     *
     * @return
     */
    public static Executor getInstance() {
        if (executor == null) {
            executor = new Executor();
        }
        return executor;
    }


    /**
     * Executes an R type instruction based on its function and parameter values
     *
     * @param instruction an array containing values of each bit field
     *                    (op,rd,rs,rt,sh
     */
    public void executeR(int instruction[]) {
        int rs = instruction[1];
        int rt = instruction[2];
        int rd = instruction[3];
        int shamt = instruction[4];
        int func = instruction[5];

        // Determines the operation based on func value
        switch (func) {
            // SLL instruction
            case 0:
                sll(rd, rt, shamt);
                break;

            // SRL instruction
            case 2:
                srl(rd, rt, shamt);
                break;

            // MULT instruction
            case 24:
                mult(rd, rt);
                break;

            // ADD instruction
            case 32:
                add(rd, rs, rt);
                break;

            // AND instruction
            case 36:
                and(rd, rs, rt);
                break;

            // SUB instruction
            case 34:
                sub(rd, rs, rt);
                break;

            // OR instruction
            case 37:
                or(rd, rs, rt);
                break;

            default:

                System.out.println("ERR: Instruction not yet implemented!");
                break;
        }
    }

    /**
     * Executes an I type instruction determined by its OP code
     *
     * @param instruction integer array containing bit fields of the
     *                    I type instruction (op,rs,rt,imm)
     */
    public void executeI(int instruction[]) {
        int op = instruction[0];
        int rs = instruction[1];
        int rt = instruction[2];
        int imm = instruction[3];


        switch (op) {
            // BEQ instruction
            case 4:
                beq(rs, rt, imm);
                break;

            // ADDI instruction
            case 8:
                addi(rs, rt, imm);
                break;

            // LW instruction
            case 35:
                lw(rs, rt, imm);
                break;

            // SW instruction
            case 42:
                sw(rs, rt, imm);
                break;

        }
    }

    /**
     * Performs a system call operation, displays either
     * integer value stored in $a0 (when $v0 == 1) or the
     * string in the memory address stored in $a0.
     *
     * @return the value that should be displayed
     */
    private String syscall() {
        String output = "NA";

        switch (register.getRegister(2)) {
            // print an integer from $a0
            case 1:
                output = Integer.toString(register.getRegister(4));
                break;
            // Print a string from address $a0
            case 4:
                // output = memory.getString(register.getRegister(4));
                break;
        }

        System.out.println(output);
        return output;
    }

    /**
     * Shifts the value in rt by shamt bits to the left
     *
     * @param rd    destination register to store result
     * @param rt    target register value to shift
     * @param shamt amount to shift the value rt
     */
    private void sll(int rd, int rt, int shamt) {
        rt = register.getRegister(rt);

        register.setRegister(rt << shamt, rd);
    }

    /**
     * Shifts the value in rt by shamt bits to the right
     *
     * @param rd    destination register to store result
     * @param rt    target register value to shift
     * @param shamt amount to shift the value rt
     */
    private void srl(int rd, int rt, int shamt) {
        rt = register.getRegister(rt);

        register.setRegister(rt >> shamt, rd);
    }

    /**
     * Multiplies the value in register rd by the value of register rt
     *
     * @param rd destination register
     * @param rt target register (value multiplied to rd)
     */
    private void mult(int rd, int rt) {
        String hi, lo;

        rt = register.getRegister(rt);
        rd = register.getRegister(rd);

        String bits = Integer.toBinaryString(rt * rd);

        if (bits.length() > 32) {
            lo = bits.substring(32, bits.length());
            hi = bits.substring(0, 31);
            register.setRegister(Integer.parseInt(lo, 2), 33);
            register.setRegister(Integer.parseInt(hi, 2), 34);
        }

    }


    /**
     * Moves PC to immediate line if value in register rs == value in rt
     *
     * @param rs  the first register to compare
     * @param rt  the second register to compare
     * @param imm line to jump to
     */
    private void beq(int rs, int rt, int imm) {
        if (register.getRegister(rs) == register.getRegister(rt)) {
            // needs to change program counter
            register.setPc(register.getPc() + imm * 4);
        }
    }

    /**
     * Stores the sum of rs and rt into rd
     *
     * @param rd destination register
     * @param rs source register 1
     * @param rt source register 2
     */
    private void add(int rd, int rs, int rt) {
        rt = register.getRegister(rt);
        rs = register.getRegister(rs);

        register.setRegister(rs + rt, rd);
    }

    /**
     * Stores the difference of rs and rt into rd
     *
     * @param rd destination register
     * @param rs source register 1
     * @param rt source register 2
     */
    private void sub(int rd, int rs, int rt) {
        rt = register.getRegister(rt);
        rs = register.getRegister(rs);

        register.setRegister(rs - rt, rd);
    }

    /**
     * Stores the value of rs AND rt in rd
     *
     * @param rd destination register
     * @param rs source register 1
     * @param rt source register 2
     */
    private void and(int rd, int rs, int rt) {
        rt = register.getRegister(rt);
        rs = register.getRegister(rs);

        register.setRegister(rs & rt, rd);
    }

    /**
     * Stores the value of rs OR rt in rd
     *
     * @param rd destination register
     * @param rs source register 1
     * @param rt source register 2
     */
    private void or(int rd, int rs, int rt) {
        rt = register.getRegister(rt);
        rs = register.getRegister(rs);

        register.setRegister(rs | rt, rd);
    }

    /**
     * Stores value of rt + imm in register rd
     *
     * @param rs  source value to perform addition on
     * @param rt  target register reslut stored in
     * @param imm immediate value to add to rs
     */
    private void addi(int rt, int rs, int imm) {
        rs = register.getRegister(rs);
        register.setRegister(rs + imm, rt);
    }

    /**
     * Places a value from memory into a register
     *
     * @param rs  register address to load from
     * @param rt  register to load word to
     * @param imm offset
     */
    private void lw(int rt, int rs, int imm) {
        int lval;
        rs = register.getRegister(rs);
        lval = (int) memory.loadMemory(rs + imm);
        register.setRegister(lval, rt);
    }

    /**
     * Places a value from register into memory
     *
     * @param rs  source addr in memory
     * @param rt  target register to store word of
     * @param imm offset of address
     */
    private void sw(int rt, int rs, int imm) {
        rs = register.getRegister(rs);

        memory.storeMemory(rs + imm, rt);
    }

    /**
     * Jumps to the address that gets passed
     *
     * @param address
     */
    private void j(int address) {
        register.setPc(address);
    }

    /**
     * Loads immediate value into register
     *
     * @param rd  register to
     * @param imm
     */
    private void li(int rd, int imm) {
        rd = register.getRegister(rd);
        register.setRegister(imm, rd);
    }

    /**
     * Builds the code; Stores the instructions in the Main Memory Model, at the bottom of
     * the text segment. Note the actual pc doesn't get updated, this is just to have a temporary pointer
     * to the main memory.
     *
     * @param instructions - array of instructions as ints. Needs to be converted to binary to decode
     *                     the instruction.
     */
    public void build(Queue<Long> instructions) {

        register.setPc(0);
        int pc = register.getPc();

        int size = instructions.size();
        // copy of program counter is incremented every iteration to store instruction at the next
        // address
        for (int i = 0; i < size; i++, pc++) {
            memory.storeInstruction(pc, instructions.poll());
            memory.setLastInstrAddress(pc);
        }

    }

    /**
     * Begins execution of the assembly program by fetching the instructions from memory.
     * Once it has the instruction and is decoded by the CodeParser and the actual instruction
     * execution is delegated to a method in this class. Either R,I, or J type methods.
     */
    public void run() {

        isExecuting = true;
        int lastInstrAddress = memory.getLastInstrAddress();

        // Loop and constraints of program execution
        while (isExecuting && register.getPc() < lastInstrAddress) {
            executeInstruction();
        }

    }

    /**
     * Executes J type instructions
     *
     * @param
     */
    public void executeJ(int[] instruction) {

        int op = instruction[0];
        int psuedoDirectAddress = instruction[1];

        switch (op) {

            case 2:
                j(psuedoDirectAddress);

        }
    }


    /**
     * Set the execution flag. If it is true, then the program is executing and vice versa.
     *
     * @param isExecuting
     */
    public void setIsExecuting(boolean isExecuting) {
        this.isExecuting = isExecuting;
    }

    /**
     * Find out whether the program is executing
     *
     * @return the execution flag
     */
    public boolean isExecuting() {
        return isExecuting;
    }


    /**
     * Executes one instruction.
     */
    public void executeInstruction() {

        int pc = register.getPc();
        Instruction instruction = codeParser.parseInstruction(memory.loadInstruction(pc));

        /**
         * Execute R, I, or J type instructions
         */
        switch (instruction.getType()) {

            case Instruction.RTYPE:
                executeR(instruction.getInstruction());
                register.setPc(register.getPc() + 1);
                break;
            case Instruction.ITYPE:
                executeI(instruction.getInstruction());
                register.setPc(register.getPc() + 1);
                break;
            case Instruction.JTYPE:
                executeJ(instruction.getInstruction());
                break;
        }
    }

    /**
     * Dirty little evil Encapsulation breaker...
     * Ruins the MVC. But makes it work for now
     * @return the register thats supposed to be hidden
     */
    public RegisterModel getMyRegister()
    {
        return register;
    }

    /**
     * Equally filthy, i is the offset from the current instruction
     * @param  i the amount past the first instruction to look (0 for current)
     */
    public String getNextInstruction(int i)
    {
        int pc = register.getPc();
        Instruction instruction = codeParser.parseInstruction(memory.loadInstruction(pc + i));
        return (pc + ": " + instruction.getName());
    }
}
