package com.example.james.mips_sim.Controller;

import com.example.james.mips_sim.CodeParser.CodeParser;
import com.example.james.mips_sim.Executor.Debugger;
import com.example.james.mips_sim.Model.Executor;
import com.example.james.mips_sim.View.ModelListener;

import java.io.FileNotFoundException;
import java.util.Queue;

/**
 * The Controller class will dictate what the Executor class executes. This class has a
 * CodeParser that helps determine what gets executed by the Executor class.
 * @author Ben
 *
 */
public class Controller {

    /**
     * The controller tells the executor what to do but does not know the underlying implementation.
     * Note: The executor has the Model(Reg model and Main memory model)
     */
    private Executor executor;

    /**
     * Single instance of code parser (CodeParser is a singleton)
     * Code Parser is used as a utility class.
     */
    private CodeParser codeParser;

    /**
     * Instantiates the executor and code parser.
     */

    /**
     * Debugger runs line by line if chosen by the user to do so and examines the memory.
     */
    private Debugger debugger;

    public Controller(ModelListener modelListener) {

        executor = Executor.getInstance(modelListener);
        codeParser = CodeParser.getInstance();
        debugger = Debugger.getInstance(modelListener);
        // instantiate the view here and pass it an instance of controller.
    }


    /**
     * Some action occurs in the view and sends some info of the event to the Controller via this method.
     * The info is encoded in a String. This method deciphers what the operation is and orders the executor
     * to perform the operation.
     * @param operation
     */
    public void operation(String operation, String[] code) {


        // Compiles the program. Calls the executor to fetch instructions from memory and execute them.
        if(operation.equals("run")) {
            executor.run();
        }

        else if(operation.equals("breakpoint")) {
            debugger.breakPoint(5); // TODO change this to take other values
        }

        else if(operation.equals("continue")) {
            debugger.continueRunning();
        }

        else if(operation.equals("step")) {
            debugger.step();
        }
        // Builds the program. Stores the instructions in memory.
        else if(operation.equals("build")) {
            debugger.continueRunning();

        }

        // Other events that may happen in the program..

    }

    /**
     * Resets the model
     * @param operation
     */
    public void operation(String operation){

        if(operation.equals("reset")){
            executor.reset();
        }
    }

    public void build(String[] code){


            try {
                executor.build(code);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

    }




    public CodeParser getCodeParser()
    {
        return this.codeParser;
    }

    public Debugger getDebugger()
    {
        return this.debugger;
    }

    // James added this, didn't know if there was a reason it was left out
    public Executor getExecutor() { return this.executor;}

}