package com.vaibhav.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Choice extends AppCompatActivity {
    private TextView textchoice;
    private TextView listchoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        textchoice=findViewById(R.id.textView2);

        textchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choice.this,Text.class));
            }
        });

        listchoice=findViewById(R.id.textView3);

        listchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choice.this,Checklist.class));
            }
        });
    }
}
