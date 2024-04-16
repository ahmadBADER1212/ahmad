package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskDetail extends AppCompatActivity {
    private EditText titletext;
    private EditText destext;
    public static final String title = "Title";

    public static final String Done = "Done";
    private boolean flag = false;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static final String FLAG = "FLAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        setupViews();
        setupSharedPrefs();
        state();
        saveTasksToSharedPreferences();

        String title = getIntent().getStringExtra("data");

        titletext.setText(title);


    }

    private void setupViews() {
        titletext = findViewById(R.id.titletext);
        destext = findViewById(R.id.disptext);

    }

    public void enterButton(View view) {

        String done = destext.getText().toString();

        if (flag) {

            editor.putString(Done, done);
            editor.putBoolean(FLAG, true);
            editor.apply();

        }
        finish();
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void state() {
        flag = prefs.getBoolean(FLAG, false);


        if (flag) {

            String state = prefs.getString(Done, "");

            destext.setText(state);

        }
    }
    private   void saveTasksToSharedPreferences(){
        String name=prefs.getString(title,null);
        String des=prefs.getString(Done,null);
        if(name!=null||des!=null){
            titletext.setText(name);
            destext.setText(des);
            editor.clear();
            editor.apply();
        }
    }
}
