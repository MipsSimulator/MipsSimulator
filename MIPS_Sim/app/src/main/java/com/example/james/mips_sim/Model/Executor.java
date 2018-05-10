package com.example.james.mips_sim.Model;

import com.example.james.mips_sim.CodeParser.CodeParser;
import com.example.james.mips_sim.Executor.Instruction;
import com.example.james.mips_sim.View.ModelListener;

import java.io.FileNotFoundException;
import java.util.Queue;

/**
 * Main model Executor contains sub models memory and register
 * Interprets memory addresses and modifies values in registers and memory
 * for instructions
 * @author James
 */
public class Executor extends AbstractModel{

    private boolean isExecuting;
    private MainMemoryModel memory;
    private RegisterModel register;
    private CodeParser codeParser;
    private static Executor executor;

    private Executor(ModelListener modelListener) {

        addModelListener(modelListener);
        this.memory = MainMemoryModel.getInstance();
        this.register = RegisterModel.getInstance();
        this.codeParser = CodeParser.getInstance();
    }


    /**
     * Singleton of executor
     *
     * @return
     */
    public static Executor getInstance(ModelListener modelListener) {
        if (executor == null) {
            executor = new Executor(modelListener);
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

            // JR instruction
            case 8:
                jr(rs);
                break;
            case 16:
                mfhi(rd);
                break;

            // MFLO instruction
            case 18:
                mflo(rd);
                break;

            // MULT instruction
            case 24:
                mult(rd, rt);
                break;

            // DIV instruction
            case 26:
                div(rs,rt);
                break;

            // DIVU instruction
            case 27:
                divu(rs,rt);
                break;

            // ADD instruction
            case 32:
                add(rd, rs, rt);
                break;

            // ADDU instruction
            case 33:
                addu(rd, rs, rt);
                break;

            // SUB instruction
            case 34:
                sub(rd, rs, rt);
                break;

            // AND instruction
            case 36:
                and(rd, rs, rt);
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

            // BGEZ or BLTZ instruction
            case 1:
                if (rt == 0)
                    bgez(rs, imm);
                else
                    bltz(rs,imm);
                break;

            // BEQ instruction
            case 4:
                beq(rs, rt, imm);
                break;

            // BNE instruction
            case 5:
                bne(rs, rt, imm);
                break;

            // BLEZ instruction
            case 6:
                blez(rs, imm);
                break;

            // BGTZ instruction
            case 7:
                bgtz(rs, imm);
                break;

            // ADDI instruction
            case 8:
                addi(rt, rs, imm);
                break;

            // ADDIU instruction
            case 9:
                addiu(rt, rs, imm);
                break;

            // ANDI instruction
            case 12:
                andi(rt, rs, imm);
                break;

            // LW instruction
            case 35:
                lw(rt, rs, imm);
                break;

            // SW instruction
            case 43:
                sw(rt, rs, imm);
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

    /* Stores the value from $hi into $rd
     * @param rd the destination register
     */
    private void mfhi(int rd)
    {
        register.setRegister(register.getHi(), rd);
    }

    /* Stores the value from $lo into $rd
     * @param rd the destination register
     */
    private void mflo(int rd)
    {
        register.setRegister(register.getLo(), rd);
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
     * divides rs by rt, stores quotient in $lo and
     * remainder in $hi
     * @param rs dividend
     * @param rt divisor
     */
    private void div(int rs, int rt)
    {
        rs = register.getRegister(rs);
        rt = register.getRegister(rt);
        register.setLo(rs/rt);
        register.setHi(rs%rt);
    }

    /**
     * divides rs by rt, stores quotient in $lo and
     * remainder in $hi (UNSIGNED)
     * @param rs dividend
     * @param rt divisor
     */
    private void divu(int rs, int rt)
    {
        rs = Math.abs(register.getRegister(rs));
        rt = Math.abs(register.getRegister(rt));
        register.setLo(rs/rt);
        register.setHi(rs%rt);
    }

    /**
     * Branch on greater than or equal to zero
     * @param rs register of value to compare
     * @param imm branch location
     */
    private void bgez(int rs, int imm)
    {
        if (register.getRegister(rs) >= 0)
        {
            register.setPc(register.getPc() + imm * 4);
        }
    }

    /**
     * Branch on greater than zero
     * @param rs register of value to compare
     * @param imm branch location
     */
    private void bgtz(int rs, int imm)
    {
        if (register.getRegister(rs) > 0)
        {
            register.setPc(register.getPc() + imm * 4);
        }
    }

    /**
     * Branch on less than zero
     * @param rs register of value to compare
     * @param imm branch location
     */
    private void bltz(int rs, int imm)
    {
        if (register.getRegister(rs) < 0)
        {
            register.setPc(register.getPc() + imm * 4);
        }
    }

    /**
     * Branch on less than or equal to zero
     * @param rs register of value to compare
     * @param imm branch location
     */
    private void blez(int rs, int imm)
    {
        if (register.getRegister(rs) <= 0)
        {
            register.setPc(register.getPc() + imm * 4);
        }
    }

    /**
     * Branch when value in reg rs == value in reg rt
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
     * Branch when value in reg rs != value in reg rt
     *
     * @param rs  the first register to compare
     * @param rt  the second register to compare
     * @param imm line to jump to
     */
    private void bne(int rs, int rt, int imm) {
        if (register.getRegister(rs) != register.getRegister(rt)) {
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
     * Stores value of rt + imm in register rs
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
     * Stores unsigned value of rt + imm in register rs
     * @param rt target register
     * @param rs register value to increment
     * @param imm amount to increment rs by
     */
    private void addiu(int rt, int rs, int imm)
    {
        rs = Math.abs(register.getRegister(rs));
        register.setRegister(rs + Math.abs(imm), rt);
    }

    /**
     * Stores unsigned sum of rs + rt into register rd
     * @param rd destination register
     * @param rs register of first term to add
     * @param rt register of second term to add
     */
    private void addu(int rd, int rs, int rt)
    {
        rs = Math.abs(register.getRegister(rs));
        rt = Math.abs(register.getRegister(rt));

        register.setRegister(rs + rt, rd);
    }

    /**
     * Bitwise ands a register and an immediate value storing result in register
     * @param rt target register to store result
     * @param rs register containing first term
     * @param imm second term immediate value
     */
    private void andi(int rt, int rs, int imm)
    {
        rs = register.getRegister(rs);

        register.setRegister(rs & imm, rt);
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
        rt = register.getRegister(rt);
        memory.storeMemory(rs + imm, rt);
    }

    /**
     * Jumps to the address that gets passed
     *
     * @param address
     */
    private void j(int address) {
        register.setPc(register.getPc() + address);
    }

    /**
     * jumps to the address at register RS
     * @param rs
     */
    private void jr(int rs) {
        register.setPc(register.getRegister(rs));
    }

    /**
     * Jumps to the address and stores return address in $31
     *
     * @param address
     */
    private void jal(int address) {
        register.setRegister(register.getPc() + 8,31);
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
     * @param code - array of instructions as Strings. Needs to be converted to binary to decode
     *                     the instruction.
     */
    public void build(String[] code) throws FileNotFoundException {

        memory.setInstructions(code);

        Queue<Long> instructions = codeParser.parseCode(code);

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

            //J
            case 2:
                j(psuedoDirectAddress);
                break;
            // JAL
            case 3:
                jal(psuedoDirectAddress);
                break;
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


    public void reset(){
        register.setPc(MainMemoryModel.TEXT_BOTTOM_ADDRESS);
        for(int i = 0; i<register.getRegisterFile().length; i++){
            if(i >= 28 && i <= 30)
                continue;
            register.setRegister(0,i);
        }
        memory.setInstructions(memory.getInstructions());

        super.reset();
    }

}
