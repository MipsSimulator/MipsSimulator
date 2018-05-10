package com.example.james.mips_sim.Executor;

import com.example.james.mips_sim.Model.*;
import com.example.james.mips_sim.View.ModelListener;

import java.util.Queue;

/**
 * Debugs the program based on the users request.
 * Has a handle to MainMemoryModel, RegisterModel, and Executor to be able to fully analyze the state
 * of the program as it executes.
 * @author Ben
 *
 */
public class Debugger extends AbstractModel{

    private MainMemoryModel memory;
    private RegisterModel registerModel;
    private Executor executor;

    private static Debugger debugger;
    private int breakPoint;

    private Debugger(ModelListener modelListener) {

        memory = MainMemoryModel.getInstance();
        registerModel = RegisterModel.getInstance();
        executor = Executor.getInstance(modelListener);

        breakPoint = Integer.MAX_VALUE;

        addModelListener(modelListener);
        // empty
    }

    public static Debugger getInstance(ModelListener modelListener) {
        if(debugger == null)
            debugger = new Debugger(modelListener);

        return debugger;
    }


    public void breakPoint(int breakPoint) {

        this.breakPoint = breakPoint + registerModel.getPc();
        compile();
        update();

    }


    /**
     * After hitting the breakpoint, the user selects continue running. The program finishes
     * executing.
     */
    public void continueRunning() {

        compile();
        update();
    }

    /**
     * Go to the next line of execution and see the state of the program.
     */
    public void step() {

        this.breakPoint = registerModel.getPc()+1;

        compile();
        update();


    }


    private void compile() {

        executor.setIsExecuting(true);
        int lastInstrAddress = memory.getLastInstrAddress();


        // Loop and constraints of program execution
        while(executor.isExecuting() && registerModel.getPc() <= lastInstrAddress && registerModel.getPc() < breakPoint) {
            //System.out.println("Executing Instruction...");
            executor.executeInstruction();
             // For the recent instruction list
        }

        this.breakPoint = Integer.MAX_VALUE;
    }



    public void update() {

        DebugEvent debugEvent = new DebugEvent(registerModel.getRegisterFile(),memory.getRecentValues(),
                memory.getRecentAddresses(), memory.getRecentInstructions(), registerModel.getPc());

       notifyChanged(debugEvent);

        memory.enqueueInstruction(registerModel.getPc() - MainMemoryModel.TEXT_BOTTOM_ADDRESS);


        // Create and send a new debug event to the view to update some table that the user would see.

    }




    // This functions added by James to get data for the View Update

    /**
     * Gets the value of a specific register
     * @param index the register #
     * @return value at register $index
     */
   public int getRegisterValue(int index)
    {
        return registerModel.getRegister(index);
    }
}