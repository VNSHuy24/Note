package com.example.note;

import android.app.Activity;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<Note> {
    Activity contex;
    int idLayout;
    ArrayList<Note> myList;

    public MyArrayAdapter(Activity contex, ArrayList<Note> myList, int idLayout){
        super(contex, idLayout, myList);
        this.contex = contex;
        this.idLayout = idLayout;
        this.myList = myList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myFlater = contex.getLayoutInflater();
        convertView = myFlater.inflate(idLayout, null);
        Note myNote = myList.get(position);

        TextView txt_title = convertView.findViewById(R.id.txt_title_item);
        txt_title.setText(myNote.getTitle());
        TextView txt_body = convertView.findViewById(R.id.txt_body_item);
        txt_body.setText(myNote.getBody());
        TextView txt_time = convertView.findViewById(R.id.txt_time_item);
        txt_time.setText(myNote.getCreatedAt());

        return convertView;
    }
}
