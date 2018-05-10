package com.example.james.mips_sim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.james.mips_sim.Controller.Controller;
import com.example.james.mips_sim.Executor.DebugEvent;
import com.example.james.mips_sim.Model.RegisterModel;
import com.example.james.mips_sim.View.ModelListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class DebuggerActivity extends FragmentActivity implements ModelListener{

    private Controller controller;
    private TextView inst1;
    private  TextView inst2;
    private TextView inst3;

    private TextView lineNum1;
    private TextView lineNum2;
    private TextView lineNum3;
    private int lineCount = 1;

    private int breakpoint_input;

    private TextView t0;

    // Pager widget
    private ViewPager mPager;
    private FragmentStatePagerAdapter mPagerAdapter;

    /**
     * Number of fragments in our view pager. (For the debugger)
     */
    private static int NUM_PAGES = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugger);

        // Initialize Controller
        controller = new Controller(this);
        controller.build(getIntent().getStringArrayExtra("code"));


        // instantiate View Pager/ Adapter
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new DebuggerStateAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);



        lineNum1 = (TextView) findViewById(R.id.line1);
        lineNum2 = (TextView) findViewById(R.id.line2);
        lineNum3 = (TextView) findViewById(R.id.line3);

        inst1 = (TextView) findViewById(R.id.inst1) ;
        inst2 = (TextView) findViewById(R.id.inst2) ;
        inst3 = (TextView) findViewById(R.id.inst3) ;

        // Play Button
        Button play = (Button) findViewById(R.id.bttn_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.operation("build",getIntent().getStringArrayExtra("code"));
            }
        });

        // Step Button
        Button step = (Button) findViewById(R.id.bttn_step);
        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.operation("step", getIntent().getStringArrayExtra("code"));
            }
        });


        Button brkpoint = (Button) findViewById(R.id.bttn_break);
        brkpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_bp_popup();
            }
        });


        Button resetButton = (Button) findViewById(R.id.bttn_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.operation("reset");
            }
        });
    }

    // Update Register Values the tedius way
    public void update_reg()
    {
        TextView reg;

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_1);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(1)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_2);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(2)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_3);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(3)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_4);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(4)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_5);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(5)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_6);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(6)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_7);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(7)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_8);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(8)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_9);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(9)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_10);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(10)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_11);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(11)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_12);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(12)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_13);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(13)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_14);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(14)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_15);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(15)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_16);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(16)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_17);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(17)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_18);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(18)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_19);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(19)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_20);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(20)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_21);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(21)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_22);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(22)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_23);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(23)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_24);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(24)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_25);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(25)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_26);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(26)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_27);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(27)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_28);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(28)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_29);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(29)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_30);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(30)));

        reg = (TextView)( mPager.getChildAt(0)).findViewById(R.id.r_31);
        reg.setText(Integer.toString(controller.getDebugger().getRegisterValue(31)));
    }


    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem() == 0) {
            super.onBackPressed(); // Page 0, back button operates normally.
        }
        else
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    /**
     * Creates a pop-up dialogue box that allows the user to enter an integer to
     * be set as a breakpoint
     */
    public void create_bp_popup() {
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
    }

    /**
     * Updates the values of activity_debugger instruction labels/line nums
     * @param event
     */
    @Override
    public void update(DebugEvent event) {
        LinkedList<String> recentInstructions = (LinkedList<String>) event.getRecentInstructions();
        ArrayList<TextView> instructionsViews = new ArrayList<>(3);

        instructionsViews.add(inst1);
        instructionsViews.add(inst2);
        instructionsViews.add(inst3);

        ArrayList<TextView> lineViews = new ArrayList<>(3);

        lineViews.add(lineNum1);
        lineViews.add(lineNum2);
        lineViews.add(lineNum3);

        for(int i = 0; i < instructionsViews.size() &&  i < recentInstructions.size(); i++){
            instructionsViews.get(i).setText(recentInstructions.get(i));
            lineViews.get(i).setText(Integer.toString(event.getPc() - RegisterModel.INITIAL_PC + i));
        }
        lineCount++;

//        for(int i = 0; i<event.getRegisterState().length; i++){
//            System.out.println( "update: " + "Register " + i + ":" +  event.getRegisterState()[i]);
//
//        }

        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");

        // Updates all register values! One by One! Isn't that great?
        update_reg();
    }



    @Override
    public void reset() {
        inst1.setText("Instruction 1");
        inst2.setText("Instruction 2");
        inst3.setText("Instruction 3");

        lineNum1.setText("1");
        lineNum2.setText("2");
        lineNum3.setText("3");

        lineCount = 1;
    }

    public class DebuggerStateAdapter extends FragmentStatePagerAdapter {

            public DebuggerStateAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return new TabRegister();
            }

            @Override
            public int getCount() {
                return NUM_PAGES;
            }
        }



        @Override
        public void onResume() {

            super.onResume();

            for(int i = 0; i<mPager.getChildCount(); i++){
                System.out.println("child: " + i + mPager.getChildAt(i));
            }

            mPager = (ViewPager) findViewById(R.id.view_pager);
            mPagerAdapter = new DebuggerStateAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(0);


        }




}
