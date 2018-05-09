package com.example.james.mips_sim;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class  MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText textfield = (EditText)findViewById(R.id.editText);
        Button run = (Button)findViewById(R.id.bttnRun);
        Button debug = (Button)findViewById(R.id.bttnDebug);

        // Set click for debug button
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Writes contents of textfield to a file "code.txt" which is read by the code parser.
                writeToFile(textfield.toString());
                startActivity(new Intent(getApplicationContext(), DebuggerActivity.class));

            }
        });

        // Set click for run button
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void writeToFile(String data)
    {
        try{
            OutputStreamWriter oswWriter = new OutputStreamWriter(this.openFileOutput("code.txt", Context.MODE_PRIVATE));
            oswWriter.write(data);
            oswWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
