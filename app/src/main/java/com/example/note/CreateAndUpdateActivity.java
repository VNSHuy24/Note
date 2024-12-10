package com.example.note;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAndUpdateActivity extends AppCompatActivity {
    ImageButton btnBack, btnUndo, btnDone, btnDelete;
    EditText edtTile, edtBody;
    TextView txt_time;

    SQLiteDatabase myDataBase;
    String dataBaseName = "note_manament";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_update);

        addControl();

        myDataBase = openOrCreateDatabase(dataBaseName, MODE_PRIVATE, null);
        Bundle myBundle = getIntent().getBundleExtra("myBundle");
        String noteId = myBundle.getString("id");
        //Toast.makeText(CreateAndUpdateActivity.this, noteId, Toast.LENGTH_LONG).show();

        if (noteId.equals("")){ // chưa có id -> ghi chú mới
            noteId = Note.getTimeNow();
            upDateDataToActivity("", "", noteId);
        }

        else{ // có id truy vấn lại dữ liệu gán vào các biến txt
            loadNote(noteId);
        }

        setupEventHandlers(noteId);
    }

    private void loadNote(String nodeId) {
        String sql = "SELECT * FROM NOTE WHERE NoteId = ?";

        try {
            Cursor cur = myDataBase.rawQuery(sql, new String[]{nodeId});

            cur.moveToFirst();
            String title = cur.getString(cur.getColumnIndexOrThrow("Title"));
            String body = cur.getString(cur.getColumnIndexOrThrow("Body"));
            String updatedAt = cur.getString(cur.getColumnIndexOrThrow("CreatedAt"));
            upDateDataToActivity(title, body, updatedAt);
        }

        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void upDateDataToActivity(String title, String body, String updatedAt) {
        edtTile.setText(title);
        edtBody.setText(body);
        txt_time.setText(updatedAt);
    }


    private void addControl() {
        btnBack = findViewById(R.id.btnBackToMain);
        btnUndo = findViewById(R.id.btnUndo);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);

        edtTile = findViewById(R.id.edtTile);
        edtBody = findViewById(R.id.edtBody);
        txt_time = findViewById(R.id.txt_time);
    }



    private void setupEventHandlers(String  noteId) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo question save or cancel
                finish();
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload(noteId);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTile.getText().toString();
                String body = edtBody.getText().toString();
                String updatedAt = Note.getTimeNow();


                try {
                    // Câu lệnh SQL chèn dữ liệu vào bảng NOTE
                    String sql = "INSERT OR REPLACE INTO NOTE (NoteId, Title, Body, CreatedAt) VALUES (?, ?, ?, ?)";
                    SQLiteStatement statement = myDataBase.compileStatement(sql);

                    // Gán giá trị cho các tham số
                    statement.bindString(1, noteId);
                    statement.bindString(2, title);
                    statement.bindString(3, body);
                    statement.bindString(4, updatedAt);

                    // Thực thi câu lệnh
                    statement.executeInsert();

                    Toast.makeText(CreateAndUpdateActivity.this, "Ghi chú đã được lưu!", Toast.LENGTH_SHORT).show();

                    // Xóa dữ liệu trong các EditText sau khi lưu
                    edtTile.setText("");
                    edtBody.setText("");
                    finish();

                } catch (Exception e) {
                    Toast.makeText(CreateAndUpdateActivity.this, "Lỗi khi lưu ghi chú: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("DatabaseError", "Error inserting note: " + e.getMessage());
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CreateAndUpdateActivity.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn chắc chắn muốn xóa ghi chú này?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sql = "DELETE FROM NOTE WHERE NoteId = ?";
                                SQLiteStatement statement = myDataBase.compileStatement(sql);
                                statement.bindString(1, noteId);

                                try {
                                    int affectedRows = statement.executeUpdateDelete();
                                    if (affectedRows > 0) {
                                        Toast.makeText(CreateAndUpdateActivity.this,
                                                "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                                        finish(); // Đóng Activity
                                    } else {
                                        Toast.makeText(CreateAndUpdateActivity.this,
                                                "Lỗi xóa ghu chú", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(CreateAndUpdateActivity.this,
                                            "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null) // Đóng dialog khi bấm "Hủy"
                        .show();
            }
        });


    }

    private void reload(String id){
        loadNote(id);

        // todo xử lí khi query id  không hợp có thì để trống

        if(id == ""){
            return;
        }
        // todo nếu query ra đượce update lại

    }
}