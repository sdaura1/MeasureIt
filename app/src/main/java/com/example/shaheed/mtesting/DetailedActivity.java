package com.example.shaheed.mtesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedActivity extends AppCompatActivity {

    SQLiteHelper dbhelper;
    SQLiteDatabase databaseRead;
    private static final int REQUEST_PHONE_CALL = 1;
    String name, shirt, shoulder, trouser, hand, phone_number;
    int id1;
    FloatingActionButton fabCall;
    TextView textName, textShoulder, textShirt, textTrouser, textHand, textPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        dbhelper = new SQLiteHelper(this);
        databaseRead = dbhelper.getReadableDatabase();
        textName = findViewById(R.id.textName);
        textShoulder = findViewById(R.id.textShoulder);
        textShirt = findViewById(R.id.textShirt);
        textTrouser = findViewById(R.id.textTrouser);
        textHand = findViewById(R.id.textHand);
        textPhoneNumber = findViewById(R.id.textPhoneNumber);
        fabCall = findViewById(R.id.fabCall);

        name = getIntent().getStringExtra("name");
        id1 = getIntent().getIntExtra("id", id1);

        Cursor mCursor = databaseRead.rawQuery("SELECT * FROM " + SQLiteHelper.TB_NAME + " WHERE _id = " + id1 + "",
                null);

        if(mCursor.moveToFirst()) {
            do {
                name = mCursor.getString(mCursor.getColumnIndex("name"));
                shirt = mCursor.getString(mCursor.getColumnIndex("shirt"));
                shoulder = mCursor.getString(mCursor.getColumnIndex("shoulder"));
                trouser = mCursor.getString(mCursor.getColumnIndex("trouser"));
                hand = mCursor.getString(mCursor.getColumnIndex("hand"));
                phone_number = mCursor.getString(mCursor.getColumnIndex("phone_number"));
            }while(mCursor.moveToNext());

            textName.setText("Name: " + name);
            textHand.setText("Hand: " + hand);
            textTrouser.setText("Trouser: " + trouser);
            textShirt.setText("Shirt: " + shirt);
            textShoulder.setText("Shouder: " + shoulder);
            textPhoneNumber.setText("Phone Number: " + phone_number.trim());
        }

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_CALL);
                mIntent.setData(Uri.parse("tel:+234" + phone_number));
                if (ActivityCompat.checkSelfPermission(DetailedActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailedActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                }else {
                    startActivity(mIntent);
                }
            }
        });
        mCursor.close();
    }
}