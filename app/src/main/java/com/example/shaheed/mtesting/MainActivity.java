package com.example.shaheed.mtesting;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText name_text, shoulder_text, ph_num_text, hand_text, shirt_text, trouser_text;
    Button save_btn;
    String name_val, shoulder_val, hand_val, trouser_val, shirt_val, ph_val;
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase databaseWrite, databaseRead;
    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHelper = new SQLiteHelper(this);
        databaseWrite = sqLiteHelper.getWritableDatabase();
        databaseRead = sqLiteHelper.getReadableDatabase();
        contentValues = new ContentValues();

        name_text = findViewById(R.id.name_id);
        shoulder_text = findViewById(R.id.shoulder_id);
        shirt_text = findViewById(R.id.shirt_id);
        ph_num_text = findViewById(R.id.ph_num_id);
        hand_text = findViewById(R.id.hand_id);
        trouser_text = findViewById(R.id.trouser_id);

        save_btn = findViewById(R.id.save_id);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_val = name_text.getText().toString();
                shoulder_val = shoulder_text.getText().toString();
                trouser_val = trouser_text.getText().toString();
                shirt_val = shirt_text.getText().toString();
                hand_val = hand_text.getText().toString();
                ph_val = ph_num_text.getText().toString();


                if (name_val.isEmpty() || shoulder_val.isEmpty() || trouser_val.isEmpty() ||
                        shirt_val.isEmpty() || hand_val.isEmpty() || ph_val.isEmpty()) {

                    Toast.makeText(MainActivity.this,
                            "All fields are required!", Toast.LENGTH_LONG).show();

                    return;
                }else{

                    boolean Insert = sqLiteHelper.insertContact(String.valueOf(name_val),
                            Integer.valueOf(shoulder_val), Integer.valueOf(trouser_val),
                            Integer.valueOf(shirt_val), Integer.valueOf(hand_val),
                            Long.valueOf(ph_val));

                    if (Insert) {
                        Toast.makeText(MainActivity.this,
                                "Inserted Successfully!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager) MainActivity.this
                .getSystemService(Context.SEARCH_SERVICE);

        return super.onCreateOptionsMenu(menu);
    }
}