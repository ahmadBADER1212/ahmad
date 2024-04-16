package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {
    private EditText input;
    private ListView listView;
    private ArrayList<Task> items;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private TextView customtext;
    private Gson gson;
    private CheckBox checkBox;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        setupViews();
        loadTasks();

        ArrayAdapter<Task> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listAdapter);



        clickListView();
        setupListViewAdapter();
    }

    private void setupViews() {
        input = findViewById(R.id.edittxt);
        items = new ArrayList<>();
        listView = findViewById(R.id.lview_tasks);

    }

    private void setupListViewAdapter() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Task removed", Toast.LENGTH_LONG).show();
                items.remove(i);
                ArrayAdapter<Task>  adapter = (ArrayAdapter<Task>) listView.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                saveTasksToSharedPreferences();
                return false;
            }
        });
    }

    public void saveTasksToSharedPreferences() {
        prefs = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        editor = prefs.edit();
        gson = new Gson();

        String json = gson.toJson(items);
        editor.putString("Tasks", json);
        editor.apply();
    }

    private void loadTasks() {
        prefs = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        gson = new Gson();
        String json = prefs.getString("Tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();

        // Clear the existing list before loading tasks
        items.clear();

        ArrayList<Task> loadedTasks = gson.fromJson(json, type);

        if (loadedTasks != null) {
            items.addAll(loadedTasks);
        }
    }

    private void clickListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                String taskTitle   = items.get(i).getTitle();
                Intent intent = new Intent(TaskActivity.this, TaskDetail.class);
                intent.putExtra("data", taskTitle);
                intent.putExtra("id",id);
                startActivity(intent);
                ArrayAdapter<Task> adapter = (ArrayAdapter<Task>) listView.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                saveTasksToSharedPreferences();

                textView = (TextView) view;
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



                prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(taskTitle, true);
                editor.apply();


            }
        });
    }

    private Task getTaskByTitle(String title) {
        for (Task task : items) {
            if (task != null && task.getTitle() != null && task.getTitle().equals(title)) {
                return task;
            }
        }
        return null;
    }

    public void addTask(Task newItem) {
        items.add(newItem);
        ArrayAdapter<Task>  adapter = (ArrayAdapter<Task>) listView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount() - 1);
        }
        saveTasksToSharedPreferences();
    }

    private void makeToast(String s) {
        Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

    public void enterButton(View view) {
        String text = input.getText().toString();

        if (text.isEmpty()) {
            makeToast("Enter a task.");
        } else {
            addTask(new Task(text));
            input.setText("");
            makeToast("Added: " + text);
        }
    }
}
