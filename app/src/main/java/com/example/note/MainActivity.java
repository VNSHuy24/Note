package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.MissingFormatArgumentException;

public class MainActivity extends AppCompatActivity {
    TabHost myTab;
    ImageButton btnSetting, btnAdd;

    SQLiteDatabase myDataBase;
    String dataBaseName = "note_manament";

    ListView lv;
    ArrayList<Note> myList;
    MyArrayAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDataBase();
        addControl();
        processEvenClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        myList = new ArrayList<>();

        String sql = "SELECT * FROM NOTE";
        try (Cursor cursor = myDataBase.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String noteId = cursor.getString(cursor.getColumnIndexOrThrow("NoteId"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("Title"));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow("Body"));
                    String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("CreatedAt"));

                    myList.add(new Note(noteId, title, body, createdAt));
                } while (cursor.moveToNext());
            }
        }

        myAdapter = new MyArrayAdapter(MainActivity.this, myList, R.layout.layout_note_item);
        lv.setAdapter(myAdapter);
    }

    // create database if is not exists
    private void initDataBase() {
        myDataBase = openOrCreateDatabase(dataBaseName, MODE_PRIVATE, null);
        try {
            String sql = "CREATE TABLE IF NOT EXISTS NOTE ("
                    + "NoteId TEXT PRIMARY KEY, "
                    + "Title TEXT, "
                    + "Body TEXT, "
                    + "CreatedAt TEXT)";
            myDataBase.execSQL(sql);
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }
    }


    private void addControl() {
        btnSetting = findViewById(R.id.btnSetting);
        btnAdd = findViewById(R.id.btnAddNote);
        //btnMore = findViewById(R.id.btnMore);

        // xử  lí tab
        myTab = findViewById(R.id.myTab);
        myTab.setup();
        TabHost.TabSpec spec1, spec2;

        spec1 = myTab.newTabSpec("t1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("", ContextCompat.getDrawable(this, R.drawable.note));
        myTab.addTab(spec1);

        spec2 = myTab.newTabSpec("t2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("", ContextCompat.getDrawable(this, R.drawable.todo));
        myTab.addTab(spec2);

        lv = findViewById(R.id.lvNote);


    }

    private void processEvenClick() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateActivity("");
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = myList.get(position);
                String msg = selectedNote.getId();
                //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                startUpdateActivity(selectedNote.getId());
            }
        });

    }



    private void startUpdateActivity(String nodeId){ // sẽ chỉnh sữa node nếu truyền vào id một note hoặc tạo mới nếu id rỗng
        Bundle myBundle = new Bundle();
        myBundle.putString("id", nodeId);
        Intent myIntent = new Intent(MainActivity.this, CreateAndUpdateActivity.class);
        myIntent.putExtra("myBundle", myBundle);
        startActivity(myIntent);
    }
}