package com.example.james.mips_sim.Model;

import com.example.james.mips_sim.Executor.DebugEvent;
import com.example.james.mips_sim.View.ModelListener;

import java.util.ArrayList;

/**
 * Base abstract class for the Concrete Model. Hides the implementation of
 * notifying the views of model updates and hides adding/removing them.
 * @author Ben
 */
abstract public class AbstractModel{

    /**
     * Model Listeners is anyone that wants to be notified when changes occur in the model.
     */
    private ArrayList<ModelListener> listeners;

    /**
     * Instantiates the ModelListener list to any empty list.
     */
    public AbstractModel(){
        listeners = new ArrayList<>();
    }

    /**
     * Add a listener to the listeners ArrayList.
     * @param listener
     */
    public void addModelListener(ModelListener listener){
        listeners.add(listener);
    }

    /**
     * Remove a listener from the listeners ArrayList.
     * @param listener
     */
    public void removeModelListener(ModelListener listener){
        listeners.remove(listener);
    }

    /**
     * Notify all registered listeners of an update occurring in the model.
     * @param event A ModelEvent containing information about the change that
     * occurred in the model. ,
     */
    public void notifyChanged(DebugEvent event){
        for(ModelListener listener: listeners){
            listener.update(event);
        }
    }

}