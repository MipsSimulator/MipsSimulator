package com.example.james.mips_sim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.james.mips_sim.Controller.Controller;

import java.util.ArrayList;

public class DebuggerActivity extends AppCompatActivity {

    private Controller controller;
    private TextView inst1, inst2, inst3;


    private int breakpoint_input;
    private ArrayList<DataItem> instList;


    // Pager widget
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugger);

        // Initialize Controller
        controller = new Controller();
        controller.operation("build");

        // get instruction list view
        //instList = new ArrayList<>()

        //instList.add(new DataItem("Instruction",7));

        // Play Button
        Button play = (Button) findViewById(R.id.bttn_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.getDebugger().continueRunning();
                update_view();
            }
        });

        // Step Button
        Button step = (Button) findViewById(R.id.bttn_step);
        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.getDebugger().step();
                update_view();
            }
        });

        // Breakpoint Button
        Button brkpoint = (Button) findViewById(R.id.bttn_break);
        brkpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_bp_popup();

            }
        });
    }

    // Updatest the last 3 instructions
    public void update_view()
    {
        inst1.setText(controller.getExecutor().getNextInstruction(0));
        inst3.setText(controller.getExecutor().getNextInstruction(1));
        inst2.setText(controller.getExecutor().getNextInstruction(2));
    }

    /**
     * Creates a pop-up dialogue box that allows the user to enter an integer to
     * be set as a breakpoint
     */
    public void create_bp_popup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set a new Breakpoint");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // 'set' button
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                breakpoint_input = Integer.parseInt(input.getText().toString());
                controller.getDebugger().breakPoint(breakpoint_input);
            }
        });

        // 'cancel' button
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
